package xu.li.cordova.parse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by xu.li<AthenaLightenedMyPath@gmail.com> on 1/8/15.
 */
public class ParsePluginPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    public static final String TAG = "ParsePluginPushBroadcastReceiver";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.d(TAG, "onPushOpen");

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            launchIntent.putExtras(extras);
        }
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(launchIntent);
    }
}
