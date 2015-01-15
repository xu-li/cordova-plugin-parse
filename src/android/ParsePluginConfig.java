package xu.li.cordova.parse;

import android.content.Context;
import android.content.res.Resources;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.LOG;

/**
 * Created by xu.li<AthenaLightenedMyPath@gmail.com> on 1/8/15.
 */
public class ParsePluginConfig {
    public static final String TAG = "ParsePluginHelper";

    public static final String XML_CONFIG_PREFIX = "parse_";

    public static final String KEY_PARSE_APP_ID = "application_id";
    public static final String KEY_PARSE_CLIENT_KEY = "client_key";
    public static final String KEY_PARSE_ENABLE_CRASH_REPORTING = "enable_crash_reporting";
    public static final String KEY_PARSE_NOTIFICATION_CALLBACK = "notification_callback";

    protected String applicationId;
    protected String clientKey;
    protected Boolean enableCrashReporting;
    protected String notificationCallback;

    private static ParsePluginConfig instance = null;

    protected ParsePluginConfig() {
        // do nothing
    }

    public static ParsePluginConfig getInstance() {
        if (instance == null) {
            instance = new ParsePluginConfig();
        }

        return instance;
    }

    public void init(Context context) {
        init(context, "");
    }

    public void init(Context context, String prefix) {
        // First checking the class namespace for config.xml
        Resources res = context.getResources();
        int id = res.getIdentifier("config", "xml", context.getPackageName());
        if (id == 0) {
            LOG.e(TAG, "res/xml/config.xml is missing!");
            return;
        }

        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(res.getXml(id));

        CordovaPreferences prefs = parser.getPreferences();
        if (prefs == null) {
            LOG.e(TAG, "Failed to parse res/xml/config.xml!");
            return;
        }

        // read all the values
        applicationId = prefs.getString(prefix.concat(KEY_PARSE_APP_ID), "");
        clientKey = prefs.getString(prefix.concat(KEY_PARSE_CLIENT_KEY), "");
        enableCrashReporting = "true".equals(prefs.getString(prefix.concat(KEY_PARSE_ENABLE_CRASH_REPORTING), ""));
        notificationCallback = prefs.getString(XML_CONFIG_PREFIX.concat(KEY_PARSE_NOTIFICATION_CALLBACK), "");
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public Boolean isCrashReportingEnabled() {
        return enableCrashReporting;
    }

    public String getNotificationCallback() {
        return notificationCallback;
    }
}
