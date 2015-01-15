package xu.li.cordova.parse;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseInstallation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu.li<AthenaLightenedMyPath> on 1/9/15.
 */

public class ParsePlugin extends CordovaPlugin {
	public final String TAG = "ParsePlugin";

    public static final String KEY_INTENT_PARSE_PUSH_NOTIFICATION = "com.parse.Data";

    // Saved callbackContext
    CallbackContext pushNotificationCallbackContext = null;

	@Override
	protected void pluginInitialize() {
        super.pluginInitialize();

        // installation
        ParseInstallation.getCurrentInstallation().saveInBackground();
	}

	@Override
	public boolean execute(String action, CordovaArgs args,
			CallbackContext callbackContext) throws JSONException {

		if ("linkUsernameToInstallation".equals(action)) {
			String name = args.getString(0);
			String key = args.isNull(1) ? "users" : args.getString(1);

			if (name.isEmpty()) {
				callbackContext.error("Username is required.");
				return true;
			}
			
			ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		    List<String> users = installation.getList(key);
		    if (users == null) {
		    	users = new ArrayList<String>();
		    	users.add(name);
		    } else {
		    	if (!users.contains(name)) {
		    		users.add(name);
		    	}
		    }
		    installation.put(key, users);
		    installation.saveInBackground();

		    callbackContext.success();
		    return true;
		} else if ("setCallbackContext".equals(action)) {
            pushNotificationCallbackContext = callbackContext;

            PluginResult result = new PluginResult(PluginResult.Status.OK);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);

            return true;
        }
		
		return super.execute(action, args, callbackContext);
	}

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            super.onNewIntent(intent);
            return ;
        }

        String pushNotification = extras.getString(KEY_INTENT_PARSE_PUSH_NOTIFICATION);
        if (pushNotification == null || pushNotification.isEmpty()) {
            super.onNewIntent(intent);
            return ;
        }

        Log.d(TAG, String.format("Get push notification: %s", pushNotification));

        // call callback
        if (pushNotificationCallbackContext != null) {
            ParsePluginConfig config = ParsePluginConfig.getInstance();
            String js = String.format("%s(%s)", config.getNotificationCallback(), pushNotification);
            PluginResult result = new PluginResult(PluginResult.Status.OK, js);
            result.setKeepCallback(true);
            pushNotificationCallbackContext.sendPluginResult(result);
        }

        try {
            JSONObject notification = new JSONObject(pushNotification);
            // send postMessage
            webView.postMessage(KEY_INTENT_PARSE_PUSH_NOTIFICATION, notification);
        } catch (JSONException e) {
            e.printStackTrace();
            super.onNewIntent(intent);
            return ;
        }

        super.onNewIntent(intent);
    }

    @Override
    public void onDestroy() {
        pushNotificationCallbackContext = null;

        super.onDestroy();
    }
}
