//
//  CDVParse.m
//
//  Created by Xu Li on 11/28/14.
//
//

#import "CDVParse.h"

#import <Parse/Parse.h>

@implementation CDVParse

- (void)setApplicationIDAndClientKey:(CDVInvokedUrlCommand *)command
{
    // get application id and client key
    NSString *applicationID = command.arguments[0];
    NSString *clientKey = command.arguments[1];
    
    if ([applicationID length] == 0 || [clientKey length] == 0) {
        [self failWithCallbackID:command.callbackId withMessage:@"Application ID and Client Key are both required."];
        return ;
    }
    
    NSLog(@"Parse Application ID: %@, Client Key: %@.", applicationID, clientKey);
    [Parse setApplicationId:applicationID clientKey:clientKey];
    
    // optional
    BOOL requestRemoteNotifications = YES;
    if ([command.arguments count] > 2) {
        requestRemoteNotifications = [command.arguments[2] boolValue];
    }
    
    if (requestRemoteNotifications) {
        // enable push notifiction
        UIUserNotificationType userNotificationTypes = (UIUserNotificationTypeAlert |
                                                        UIUserNotificationTypeBadge |
                                                        UIUserNotificationTypeSound);
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:userNotificationTypes
                                                                                 categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    }
    
    [self successWithCallbackID:command.callbackId];
}

- (void)getCurrentUser:(CDVInvokedUrlCommand *)command
{
    PFUser *user = [PFUser currentUser];
    
    NSMutableDictionary *properties = [[NSMutableDictionary alloc] init];
    for (NSString *key in user.allKeys) {
        properties[key] = user[key];
    }
    
    CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:properties];
    [self.commandDelegate sendPluginResult:commandResult callbackId:command.callbackId];
}

- (void)signUp:(CDVInvokedUrlCommand*)command;
{
    // get username and password
    NSString *username = command.arguments[0];
    NSString *password = command.arguments[1];
    
    if ([username length] == 0 || [password length] == 0) {
        [self failWithCallbackID:command.callbackId withMessage:@"Username and Password are both required."];
        return ;
    }
    
    PFUser *user = [PFUser user];
    user.username = username;
    user.password = password;
    
    // optional
    NSDictionary *properties = nil;
    if ([command.arguments count] > 2) {
        properties = command.arguments[2];
        for (NSString *key in properties) {
            user[key] = properties[key];
        }
    }
    
    // call api
    [user signUpInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (error) {
            [self failWithCallbackID:command.callbackId withError:error];
        } else {
            [self successWithCallbackID:command.callbackId];
        }
    }];
}

- (void)linkUsernameToInstallation:(CDVInvokedUrlCommand*)command
{
    // get username and key
    NSString *username = command.arguments[0];
    
    // optional
    NSString *key = nil;
    if ([command.arguments count] > 1) {
        key = [command.arguments objectAtIndex:1];
    }
    
    // default to users
    key = key ? key : @"users";
    
    if ([username length] == 0) {
        [self failWithCallbackID:command.callbackId withMessage:@"Username is required."];
        return ;
    }
    
    PFInstallation *currentInstallation = [PFInstallation currentInstallation];
    NSMutableArray *users = currentInstallation[key];
    
    if (!users) {
        users = [[NSMutableArray alloc] init];
    }
    
    [users addObject:username];
    currentInstallation[key] = users;
    
    [currentInstallation saveInBackgroundWithBlock:^(BOOL succeeded, NSError *error) {
        if (error) {
            [self failWithCallbackID:command.callbackId withError:error];
        } else {
            [self successWithCallbackID:command.callbackId];
        }
    }];
}

- (void)successWithCallbackID:(NSString *)callbackID
{
    [self successWithCallbackID:callbackID withMessage:@"OK"];
}

- (void)successWithCallbackID:(NSString *)callbackID withMessage:(NSString *)message
{
    CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    [self.commandDelegate sendPluginResult:commandResult callbackId:callbackID];
}

- (void)failWithCallbackID:(NSString *)callbackID withError:(NSError *)error
{
    [self failWithCallbackID:callbackID withMessage:[error localizedDescription]];
}

- (void)failWithCallbackID:(NSString *)callbackID withMessage:(NSString *)message
{
    CDVPluginResult *commandResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
    [self.commandDelegate sendPluginResult:commandResult callbackId:callbackID];
}

@end
