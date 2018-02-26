package my.com.sains.teams.utils;

import android.os.Build;

/**
 * Created by User on 30/1/2018.
 */

public class Scanner {

    public static boolean isScanner(){

        if (Consts.SCANNER_BRAND.equals(Build.BRAND)){

            return true;

        }else {

            return false;

        }

    }
}
