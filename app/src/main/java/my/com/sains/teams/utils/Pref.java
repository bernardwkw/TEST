package my.com.sains.teams.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import my.com.sains.teams.modal.LocationModal;

/**
 * Created by User on 18/12/2017.
 */

public class Pref {

    private Context context;
    private String Username;
    private String loginId;
    private SharedPreferences prefs;
    private String encryptedDeviceId;
    private Location lastLocation;

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

    public LocationModal getLastLocation() {

        prefs = context.getSharedPreferences(Consts.LAST_LOCATION_PREF, Context.MODE_PRIVATE);
        LocationModal location = new LocationModal();
        location.setLatitude(prefs.getFloat(Consts.LAST_LATITUDE,0));
        location.setLongitude(prefs.getFloat(Consts.LAST_LONGITUDE,0));

        return location;
    }

    public void setLastLocation(Location lastLocation) {

        prefs = context.getSharedPreferences(Consts.LAST_LOCATION_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Consts.DEVICE_ID, encryptedDeviceId);
        editor.putFloat(Consts.LAST_LATITUDE, (float) lastLocation.getLatitude());
        editor.putFloat(Consts.LAST_LONGITUDE, (float) lastLocation.getLongitude());

        //this.lastLocation = lastLocation;
    }

}
