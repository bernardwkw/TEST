package my.com.sains.teams.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 19/3/2018.
 */

public class GPSBroadcastReceiver extends BroadcastReceiver {

    private GPSTracker gpsTracker;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT > 22){
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent alaramIntent = new Intent();
            alaramIntent.setAction("com.teams.wake");
            PendingIntent operation = PendingIntent.getBroadcast(context, 666,
                    alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT );
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis()+ Consts.WAKE_INTERVAL,
                    operation);
        }

        gpsTracker = new GPSTracker(context);
        if (gpsTracker.canGetLocation){
            Log.e("location", "here");
            gpsTracker.setOnLocationListener(new GPSTracker.OnLocationChangedListener() {
                @Override
                public void onLocationCallback(Location location) {
                    gpsTracker.stopUsingGPS();
                    gpsTracker.setOnLocationListener(null);
                }
            });
        }
    }
}
