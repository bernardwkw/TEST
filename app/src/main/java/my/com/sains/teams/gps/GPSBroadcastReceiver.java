package my.com.sains.teams.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 19/3/2018.
 */

public class GPSBroadcastReceiver extends BroadcastReceiver {

    private GPSTracker gpsTracker;

    @Override
    public void onReceive(Context context, Intent intent) {

        // turn off these function if mobile user (not saat and speedata) complains battery drain
        if (Build.VERSION.SDK_INT > 22){ // fire alarm  intent for nxt gps tracking
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alaramIntent = new Intent();
            alaramIntent.setAction(Consts.WAKE_ACTION);
            PendingIntent operation = PendingIntent.getBroadcast(context, Consts.WAKE_REQUEST_CODE,
                    alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT );
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis()+ Consts.WAKE_INTERVAL,
                    operation);
        }


        gpsTracker = new GPSTracker(context); // get the latest gps location

    }
}
