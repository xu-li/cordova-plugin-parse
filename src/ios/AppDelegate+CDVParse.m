//
//  AppDelegate+CDVParse.m
//
//  Created by Xu Li on 11/28/14.
//
//

#import "AppDelegate+CDVParse.h"

#import "CDVParse.h"
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

    CDVParse *parsePlugin = [self.viewController getCommandInstance:@"Parse"];
    [parsePlugin configWithOptions:self.viewController.settings withPrefix:@"parse_"];
    
    // in case this app is not running
    if (launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey] && parsePlugin.jsCallback) {
        parsePlugin.notificationMessage = launchOptions[UIApplicationLaunchOptionsRemoteNotificationKey];
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
    
    // Default behavior
    [PFPush handlePush:userInfo];
    
    // Check if callback is set
    CDVParse *parsePlugin = [self.viewController getCommandInstance:@"Parse"];
    if (parsePlugin.jsCallback) {
        parsePlugin.notificationMessage = userInfo;
        [parsePlugin performSelectorOnMainThread:@selector(notificationReceived) withObject:parsePlugin waitUntilDone:NO];
    }
}

- (void)noop_application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo
{
}

@end
