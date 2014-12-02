//
//  CDVParse.h
//
//  Created by Xu Li on 11/28/14.
//
//

#import <Cordova/CDVPlugin.h>

@interface CDVParse : CDVPlugin

@property (nonatomic, strong) NSDictionary *notificationMessage;
@property (nonatomic, strong) NSString *jsCallback;

- (BOOL)configWithOptions:(NSDictionary *)options;
- (BOOL)configWithOptions:(NSDictionary *)options withPrefix:(NSString *)prefix;
- (void)notificationReceived;

- (void)setup:(CDVInvokedUrlCommand*)command;
- (void)getCurrentUser:(CDVInvokedUrlCommand*)command;
- (void)signUp:(CDVInvokedUrlCommand*)command;
- (void)linkUsernameToInstallation:(CDVInvokedUrlCommand*)command;

@end
