package my.com.sains.teams.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 18/12/2017.
 */

public class Pref {

    private Context context;
    private String Username;
    private String loginId;
    private SharedPreferences prefs;
    private String encryptedDeviceId;

    public Pref(Context context){

        this.context = context;
    }

    public String getUserName() {

        prefs = context.getSharedPreferences(Consts.USER_INFO_PREF, Context.MODE_PRIVATE);
        return prefs.getString(Consts.USER_NAME, "");
    }

    public void setUserInfo(String username, String loginId, String password) {
        prefs = context.getSharedPreferences(Consts.USER_INFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Consts.USER_NAME, username);
        editor.putString(Consts.USER_LOGIN_ID, loginId);
        editor.putString(Consts.PASSWORD, password);
        editor.commit();

    }

    public String getLoginId() {
        prefs = context.getSharedPreferences(Consts.USER_INFO_PREF, Context.MODE_PRIVATE);
        return prefs.getString(Consts.USER_LOGIN_ID, "");
    }

    public String getEncryptedDeviceId() {
        prefs = context.getSharedPreferences(Consts.DEVICE_ID, Context.MODE_PRIVATE);
        return prefs.getString(Consts.DEVICE_ID, "");
    }

    public void setEncryptedDeviceId(String encryptedDeviceId) {
        prefs = context.getSharedPreferences(Consts.USER_INFO_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Consts.DEVICE_ID, encryptedDeviceId);
    }

    public String getPassWord(){
        prefs = context.getSharedPreferences(Consts.USER_INFO_PREF, Context.MODE_PRIVATE);
        return prefs.getString(Consts.PASSWORD, "");
    }
}
