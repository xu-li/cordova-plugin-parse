package xu.li.cordova.parse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by xu.li<AthenaLightenedMyPath> on 1/8/15.
 */
public class ParsePluginPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    public static final String TAG = "ParsePluginPushBroadcastReceiver";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.i(TAG, "onPushOpen");

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        launchIntent.putExtras(intent.getExtras());
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(launchIntent);
    }

    public ParsePluginPushBroadcastReceiver() {
        super();
    }
}
