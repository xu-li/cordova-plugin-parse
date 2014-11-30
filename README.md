# cordova-plugin-parse

A cordova plugin for Parse.com

## How to install

```
// cordova
cordova plugin add https://github.com/xu-li/cordova-plugin-parse

// phonegap
phonegap local plugin add https://github.com/xu-li/cordova-plugin-parse

// ionic
ionic plugin add https://github.com/xu-li/cordova-plugin-parse
```

## Plugin Configuration

Automatically set application id and client key when starts up
```
<!-- config.xml -->
<!-- See http://docs.phonegap.com/en/edge/config_ref_index.md.html#The%20config.xml%20File-->
<widget>
  <preference name="parse_application_id" value="YOUR_PARSE_APPLICATION_ID" />
  <preference name="parse_client_key" value="YOUR_PARSE_CLIENT_KEY" />
</widget>
```

Automatically popup the remote notification dialog(iOS)
```
<!-- config.xml -->
<!-- See http://docs.phonegap.com/en/edge/config_ref_index.md.html#The%20config.xml%20File-->
<widget>
  <preference name="request_remote_notification" value="true" />
</widget>
```

## How to use

Set application id and client key(required if [configuration](https://github.com/xu-li/cordova-plugin-parse/blob/master/README.md#plugin-configuration) is not set)

```
var PARSE_APPLICATION_ID = 'YOUR_PARSE_APPLICATION_ID',
  PARSE_CLIENT_KEY = 'YOUR_PARSE_CLIENT_KEY',
  REQUEST_REMOTE_NOTIFICATION = true;
  
cordova.plugins.Parse.setApplicationIDAndClientKey(PARSE_APPLICATION_ID, PARSE_CLIENT_KEY, REQUEST_REMOTE_NOTIFICATION, function () {alert('Done');}, function (message) {alert("Something goes wrong." + message);});
```

Link username to installation
```
var username = 'lixu'; // can be anything.

cordova.plugins.Parse.linkUsernameToInstallation(username, function () {alert('Done');}, function (message) {alert("Something goes wrong." + message);});
```

## Roadmap

* Android
* Other API

## LICENSE

[MIT License](http://opensource.org/licenses/MIT)
