var exec = require('cordova/exec');

var Parse = function() {
};

// do nothing
function noop() {};

/**
 * Set Application ID and Client Key
 */
Parse.prototype.setApplicationIDAndClientKey = function (applicationID, clientKey, requestRemoteNotifications, success, fail) {
    // format #1: setApplicationIDAndClientKey(applicationID, clientKey, success, fail);
    if (typeof requestRemoteNotifications == "function" || typeof requestRemoteNotifications == "undefined") {
        fail = success || noop;
        success = requestRemoteNotifications || noop;
        requestRemoteNotifications = true;
    } else {
        // format #2: setApplicationIDAndClientKey(applicationID, clientKey, requestRemoteNotifications, success, fail);
        success = success || noop;
        fail = fail || noop;
    }
    exec(success, fail, 'Parse', 'setApplicationIDAndClientKey', [applicationID, clientKey, requestRemoteNotifications]);
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
