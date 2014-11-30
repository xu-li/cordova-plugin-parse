//
//  CDVParse.h
//
//  Created by Xu Li on 11/28/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface CDVParse : CDVPlugin

- (void)setApplicationIDAndClientKey:(CDVInvokedUrlCommand*)command;
- (void)getCurrentUser:(CDVInvokedUrlCommand*)command;
- (void)signUp:(CDVInvokedUrlCommand*)command;
- (void)linkUsernameToInstallation:(CDVInvokedUrlCommand*)command;

@end
