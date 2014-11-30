//
//  AppDelegate+CDVParse.m
//
//  Created by Xu Li on 11/28/14.
//
//

#import "AppDelegate+CDVParse.h"

#import <Parse/Parse.h>
#import <objc/runtime.h>

@implementation AppDelegate (CDVParse)

void swizzleMethod(Class c, SEL originalSelector)
{
    NSString *original = NSStringFromSelector(originalSelector);
    
    SEL swizzledSelector = NSSelectorFromString([@"swizzled_" stringByAppendingString:original]);
    SEL noopSelector = NSSelectorFromString([@"noop_" stringByAppendingString:original]);

    Method originalMethod, swizzledMethod, noop;
    originalMethod = class_getInstanceMethod(c, originalSelector);
    swizzledMethod = class_getInstanceMethod(c, swizzledSelector);
    noop = class_getInstanceMethod(c, noopSelector);
    
    BOOL didAddMethod = class_addMethod(c,
                    originalSelector,
                    method_getImplementation(swizzledMethod),
                    method_getTypeEncoding(swizzledMethod));
    
    if (didAddMethod) {
        class_replaceMethod(c,
                            swizzledSelector,
                            method_getImplementation(noop),
                            method_getTypeEncoding(originalMethod));
    } else {
        method_exchangeImplementations(originalMethod, swizzledMethod);
    }
}

+ (void)load
{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class cls = [self class];
        
        swizzleMethod(cls, @selector(application:didFinishLaunchingWithOptions:));
        swizzleMethod(cls, @selector(application:didRegisterForRemoteNotificationsWithDeviceToken:));
        swizzleMethod(cls, @selector(application:didReceiveRemoteNotification:));
    });
}

- (BOOL)swizzled_application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions
{
    BOOL ret = [self swizzled_application:application didFinishLaunchingWithOptions:launchOptions];

    // set parse application id
    CDVViewController *viewController = self.viewController;
    NSString *applicationId = [viewController.settings objectForKey:@"parse_application_id"];
    NSString *clientKey = [viewController.settings objectForKey:@"parse_client_key"];
    BOOL requestRemoteNotifications = [[viewController.settings objectForKey:@"request_remote_notification"] boolValue];
    
    if ([applicationId length] > 0 && [clientKey length] > 0) {
        [Parse setApplicationId:applicationId clientKey:clientKey];
        
        NSLog(@"Parse Application ID: %@, Client Key: %@.", applicationId, clientKey);
    }
    
    if (requestRemoteNotifications) {
        // enable push notifiction
        UIUserNotificationType userNotificationTypes = (UIUserNotificationTypeAlert |
                                                        UIUserNotificationTypeBadge |
                                                        UIUserNotificationTypeSound);
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:userNotificationTypes
                                                                                 categories:nil];
        [application registerUserNotificationSettings:settings];
        [application registerForRemoteNotifications];
    }
    
    return ret;
}

- (BOOL)noop_application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions
{
    return YES;
}

- (void)swizzled_application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)newDeviceToken
{
    // Call existing method
    [self swizzled_application:application didRegisterForRemoteNotificationsWithDeviceToken:newDeviceToken];
    
    // Store the deviceToken in the current installation and save it to Parse.
    PFInstallation *currentInstallation = [PFInstallation currentInstallation];
    [currentInstallation setDeviceTokenFromData:newDeviceToken];
    [currentInstallation saveInBackground];
}

- (void)noop_application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)newDeviceToken
{
}

- (void)swizzled_application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
    // Call existing method
    [self swizzled_application:application didReceiveRemoteNotification:userInfo];
    
    [PFPush handlePush:userInfo];
}

- (void)noop_application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
}

@end
