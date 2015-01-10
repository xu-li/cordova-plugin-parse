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

For android, you should set the ```android:name="xu.li.cordova.parse.ParsePluginApplication"``` to ```<application>``` in _AndroidManifest.xml_.

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
  <preference name="parse_request_remote_notification" value="true" />
</widget>
```

Set Javascript callback
```
<!-- config.xml -->
<!-- See http://docs.phonegap.com/en/edge/config_ref_index.md.html#The%20config.xml%20File-->
<widget>
  <preference name="parse_notification_callback" value="YOUR_CALLBACK_FUNCTION_NAME" />
</widget>
```

Enable Crash Reporting
```
<!-- config.xml -->
<!-- See http://docs.phonegap.com/en/edge/config_ref_index.md.html#The%20config.xml%20File-->
<widget>
  <preference name="parse_enable_crash_reporting" value="true" />
</widget>
```

## How to use

Link username to installation
```
var username = 'lixu'; // can be anything.

cordova.plugins.Parse.linkUsernameToInstallation(
    username,
    function () {
        alert('Done');
    }, function (error) {
        alert("Something goes wrong." + error);
    }
);
```

## Roadmap

- [x] Android
- [] Other API

## LICENSE

[MIT License](http://opensource.org/licenses/MIT)
