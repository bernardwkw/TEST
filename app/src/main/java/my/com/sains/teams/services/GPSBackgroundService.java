package my.com.sains.teams.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alaramIntent = new Intent();
        alaramIntent.setAction("com.teams.wake");
        PendingIntent operation = PendingIntent.getBroadcast(this, 666,
                alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Consts.WAKE_INTERVAL, operation);

//        if(Build.VERSION.SDK_INT < 23){
//            if(Build.VERSION.SDK_INT >= 19){
//                setExact(...);
//            }
//            else{
//                set(...);
//            }
//        }
//        else{
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), operation);
//        }


        return super.onStartCommand(intent, flags, startId);
    }
}
