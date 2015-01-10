package xu.li.cordova.parse;

/**
 * Created by xu.li<AthenaLightenedMyPath> on 1/9/15.
 */
public class ParsePluginApplication extends android.app.Application {

    ParsePluginHelper helper;

    @Override
    public void onCreate() {
        super.onCreate();

        helper = new ParsePluginHelper(this);
        helper.setupParse();
    }
}
