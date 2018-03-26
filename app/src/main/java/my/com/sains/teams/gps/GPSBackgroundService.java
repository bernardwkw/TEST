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
        PendingIntent operation = PendingIntent.getBroadcast(this, Consts.WAKE_REQUEST_CODE,
                alaramIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        if(Build.VERSION.SDK_INT <= 22){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Consts.WAKE_INTERVAL, operation);
        }
        else{// for marshmallow and above, use setExactAndAllowWhileIdle to wake up android from doze mode
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), operation);
        }


        return START_STICKY;
    }
}
