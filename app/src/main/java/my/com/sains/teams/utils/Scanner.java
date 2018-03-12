package my.com.sains.teams.utils;

import android.app.Activity;
import android.os.Build;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by User on 30/1/2018.
 */

public class Scanner {

    AtomicBoolean isScanning = new AtomicBoolean(false);

    private Activity activity;

    public Scanner(Activity activity){

    }

    public static boolean isSpeedDataScanner(){

        if (Consts.SPEEDATA_SCANNER_MODEL.equals(Build.MODEL)){

            return true;

        }else {

            return false;
        }

    }

    public static boolean isSaatScanner(){

        if (Consts.SAAT_SCANNER_MODEL.equals(Build.MODEL)){

            return true;

        }else {

            return false;

        }

    }
}
