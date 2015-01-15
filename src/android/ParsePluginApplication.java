package xu.li.cordova.parse;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by xu.li<AthenaLightenedMyPath@gmail.com> on 1/9/15.
 */
public class ParsePluginApplication extends android.app.Application {
    public static final String TAG = "ParsePluginApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        ParsePluginConfig config = ParsePluginConfig.getInstance();
        config.init(this, ParsePluginConfig.XML_CONFIG_PREFIX);

        if (config.isCrashReportingEnabled()) {
            ParseCrashReporting.enable(this);
        }

        if (config.getApplicationId() != null && !config.getApplicationId().isEmpty()) {
            Parse.initialize(this, config.getApplicationId(), config.getClientKey());
        }
    }
}
