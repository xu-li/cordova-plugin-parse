var exec = require('cordova/exec');

require('cordova/channel').onCordovaReady.subscribe(function() {
    exec(function (message) {
        // console.log("Received message: " + message);
        eval(message);
    }, null, 'Parse', 'setCallbackContext', []);
});
