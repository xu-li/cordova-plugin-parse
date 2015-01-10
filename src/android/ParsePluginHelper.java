package xu.li.cordova.parse;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.LOG;

/**
 * Created by xu.li<AthenaLightenedMyPath> on 1/8/15.
 */
public class ParsePluginHelper {
    public static final String TAG = "ParsePluginHelper";

    public static final String XML_CONFIG_PREFIX = "parse_";
    public static final String KEY_PARSE_APP_ID = "application_id";
    public static final String KEY_PARSE_CLIENT_KEY = "client_key";
    public static final String KEY_PARSE_ENABLE_CRASH_REPORTING = "enable_crash_reporting";
    public static final String KEY_PARSE_NOTIFICATION_CALLBACK = "notification_callback";

    protected Context context;
    protected ConfigXmlParser parser;
    protected Boolean parseInitialized;

    public ParsePluginHelper(Context ctx) {
        context = ctx.getApplicationContext();
        parseInitialized = false;
    }

    public void setupParse() {
        if (parser == null) {
            this.loadConfiguration();
        }

        if (parser == null) {
            return ;
        }

        this.setupParse(parser.getPreferences());
    }

    public void setupParse(CordovaPreferences prefs) {
        String appId = prefs.getString(XML_CONFIG_PREFIX.concat(KEY_PARSE_APP_ID), "");
        String clientKey = prefs.getString(XML_CONFIG_PREFIX.concat(KEY_PARSE_CLIENT_KEY), "");
        String enableCrashReporting = prefs.getString(XML_CONFIG_PREFIX.concat(KEY_PARSE_ENABLE_CRASH_REPORTING), "");

        if ("true".equals(enableCrashReporting) && !ParseCrashReporting.isCrashReportingEnabled()) {
            ParseCrashReporting.enable(context);
            Log.d(TAG, "Parse crash reporting is enabled.");
        } else {
            Log.d(TAG, "Parse crash reporting is disabled.");
        }

        // Setup Parse
        if (!appId.isEmpty() && !parseInitialized) {
            Parse.initialize(context, appId, clientKey);
            parseInitialized = true;
        }
    }

    /**
     * Load configuration from res/xml/config.xml
     * Sometimes called in the BroadcastReceiver, the CordovaPlugin hasn't been initialized.
     */
    protected void loadConfiguration() {
        // First checking the class namespace for config.xml
        Resources res = context.getResources();
        int id = res.getIdentifier("config", "xml", context.getPackageName());
        if (id == 0) {
            LOG.e(TAG, "res/xml/config.xml is missing!");
            return;
        }

        parser = new ConfigXmlParser();
        parser.parse(res.getXml(id));
    }
}
