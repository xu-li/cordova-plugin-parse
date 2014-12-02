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

## How to use

Set application id and client key(required if [configuration](https://github.com/xu-li/cordova-plugin-parse/blob/master/README.md#plugin-configuration) is not set)

```
cordova.plugins.Parse.setup({
    application_id:              YOUR_PARSE_APPLICATION_ID,
    client_key:                  YOUR_PARSE_CLIENT_KEY,
    request_remote_notification: true,
    notification_callback:       'alert'
}, function () {
    alert("Done");
}, function (error) {
    alert("Something goes wrong." + error);
});
```

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

* Android
* Other API

## LICENSE

[MIT License](http://opensource.org/licenses/MIT)
