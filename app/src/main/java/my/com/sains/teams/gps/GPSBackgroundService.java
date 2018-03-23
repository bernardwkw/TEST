package my.com.sains.teams.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 19/3/2018.
 */

public class GPSBackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alaramIntent = new Intent();
        alaramIntent.setAction(Consts.WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this, 666,
                alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        if(Build.VERSION.SDK_INT <= 22){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Consts.WAKE_INTERVAL, operation);
            Log.e("below equal ", "22");
        }
        else{
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), operation);
            Log.e("higher than ", "22");
        }


        return START_STICKY;
    }
}
