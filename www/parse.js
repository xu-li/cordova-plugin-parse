var exec = require('cordova/exec');

var Parse = function() {
};

// do nothing
function noop() {};

/**
 * Set Application ID and Client Key
 *
 * Possible keys in options:
 * 1. application_id(required):    YOUR_PARSE_APPLICATION_ID
 * 2. client_key(required):        YOUR_PARSE_CLIENT_KEY
 * 3. request_remote_notification: true
 * 4. notification_callback:       YOUR_CALLBACK_FUNCTION_NAME
 */
Parse.prototype.setup = function (options, success, fail) {
    success = success || noop;
    fail = fail || noop;
    exec(success, fail, 'Parse', 'setup', [options]);
};

/**
 * Get current user
 */
Parse.prototype.getCurrentUser = function (success, fail) {
    success = success || noop;
    fail = fail || noop;
    exec(success, fail, 'Parse', 'getCurrentUser', []);
};

/**
 * Link user to current installation
 */
Parse.prototype.linkUsernameToInstallation = function (username, key, success, fail) {
    // format #1: linkUsernameToInstallation(username, success, fail);
    if (typeof key == "function" || typeof key == "undefined") {
        fail = success || noop;
        success = key || noop;

        exec(success, fail, 'Parse', 'linkUsernameToInstallation', [username]);
    } else {
        // format #2: linkUsernameToInstallation(username, key, success, fail);
        success = success || noop;
        fail = fail || noop;

        exec(success, fail, 'Parse', 'linkUsernameToInstallation', [username, key]);
    }
};
               
// export it
module.exports = new Parse();
