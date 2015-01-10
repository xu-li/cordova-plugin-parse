package xu.li.cordova.parse;

import android.app.Activity;
import android.content.Intent;

import com.parse.ParseInstallation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xu.li<AthenaLightenedMyPath> on 1/9/15.
 */

public class ParsePlugin extends CordovaPlugin {
	public final String TAG = "ParsePlugin";
	
	protected String notificationCallback = "";

    public static final String KEY_PUSH_NOTIFICATION = "com.parse.Data";

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
		}
		
		return super.execute(action, args, callbackContext);
	}

    @Override
    public void onNewIntent(Intent intent) {
        Activity activity = cordova.getActivity();
        Intent oldIntent = activity.getIntent();

        String pushNotification = intent.getExtras().getString(KEY_PUSH_NOTIFICATION);
        if (!pushNotification.isEmpty()) {
            oldIntent.putExtra(KEY_PUSH_NOTIFICATION, pushNotification);
            activity.setIntent(oldIntent);

            // callback
//            webView.sendJavascript();
        }

        super.onNewIntent(intent);
    }

    public String getNotificationCallback() {
		return notificationCallback;
	}

	public void setNotificationCallback(String notificationCallback) {
		this.notificationCallback = notificationCallback;
	}
}
