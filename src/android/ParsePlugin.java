package xu.li.cordova.parse;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;

public class ParsePlugin extends CordovaPlugin {
	public final String TAG = "Parse";

	public final String KEY_PARSE_APP_ID = "application_id";
	public final String KEY_PARSE_CLIENT_KEY = "client_key";
	public final String KEY_PARSE_ENABLE_CRASH_REPORTING = "enable_crash_reporting";
	public final String KEY_PARSE_NOTIFICATION_CALLBACK = "notification_callback";
	
	protected String notificationCallback = "";

	@Override
	protected void pluginInitialize() {
		Context ctx = this.cordova.getActivity();
        String prefix = "parse_";

		// Enable Crash Reporting
		String enableCrashReporting = webView.getProperty(prefix.concat(KEY_PARSE_ENABLE_CRASH_REPORTING), "");
		if ("true".equals(enableCrashReporting)) {
			ParseCrashReporting.enable(ctx);
            Log.d(TAG, "Parse crash reporting is enabled.");
		} else {
            Log.d(TAG, "Parse crash reporting is not enabled.");
        }

		// Setup Parse
		String appId = webView.getProperty(prefix.concat(KEY_PARSE_APP_ID), "");
		if (!appId.isEmpty()) {
			Parse.initialize(ctx, appId,
					webView.getProperty(prefix.concat(KEY_PARSE_CLIENT_KEY), ""));
		}
		
		// Callback
		String callback = webView.getProperty(prefix.concat(KEY_PARSE_NOTIFICATION_CALLBACK), "");
		if (callback != null && !callback.isEmpty()) {
			this.notificationCallback = callback;
		}

		super.pluginInitialize();
	}

	@Override
	public boolean execute(String action, CordovaArgs args,
			CallbackContext callbackContext) throws JSONException {

		if ("setup".equals(action)) {
			
			// setup
			JSONObject options = args.getJSONObject(0);
			String appId = options.isNull(KEY_PARSE_APP_ID) ? null : options.getString(KEY_PARSE_APP_ID);
			String clientKey = options.isNull(KEY_PARSE_CLIENT_KEY) ? null : options.getString(KEY_PARSE_CLIENT_KEY);
			
			if (appId != null && clientKey != null) {
				Parse.initialize(cordova.getActivity(), appId, clientKey);
			}
			
			String callback = options.isNull(KEY_PARSE_NOTIFICATION_CALLBACK) ? null : options.getString(KEY_PARSE_NOTIFICATION_CALLBACK);
			if (callback != null && !callback.isEmpty()) {
				this.notificationCallback = callback;
			}
			
			callbackContext.success();
			return true;
			
		} else if ("linkUsernameToInstallation".equals(action)) {
			
			// linkUsernameToInstallation
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
	
	public String getNotificationCallback() {
		return notificationCallback;
	}

	public void setNotificationCallback(String notificationCallback) {
		this.notificationCallback = notificationCallback;
	}
}
