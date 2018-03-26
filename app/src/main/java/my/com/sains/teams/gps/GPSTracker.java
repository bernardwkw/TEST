package my.com.sains.teams.gps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import my.com.sains.teams.modal.LocationModal;
import my.com.sains.teams.utils.Pref;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    private LocationModal lastLocation;
    double latitude; // latitude
    double longitude; // longitude
    private Pref pref;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 30*1000; // 30 seconds

    private GoogleApiClient googleApiClient;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        pref = new Pref(mContext);
        getLocation();
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //&& !isNetworkEnabled
            if (!isGPSEnabled ) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // Get location from Network Provider
                // If network location is preferred, uncomment

/*                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }*/

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        /* if want ot repeat to get gps update*/

//                        locationManager.requestLocationUpdates(
//                                LocationManager.GPS_PROVIDER,
//                                MIN_TIME_BW_UPDATES,
//                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            // get last known location from android memory
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }else { // get last known from preference (in case device restart, no location in memory)
                                latitude = pref.getLastLocation().getLatitude();
                                longitude = pref.getLastLocation().getLongitude();

                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private CountDownTimer countDownTimer = new CountDownTimer(20*1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onFinish() {

        }
    };

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){

        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){

        return longitude;
    }


    public LocationModal getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(LocationModal lastLocation) {
//        this.longitude = lastLocation.getLongitude();
//        this.latitude = lastLocation.getLatitude();
        this.lastLocation = lastLocation;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("location changes", ": "+location.getLatitude());
        if(GPSTracker.this.onLocationListener != null){
            GPSTracker.this.onLocationListener.onLocationCallback(location);
        }

        LocationModal locationModal = new LocationModal();
        locationModal.setLongitude((float) location.getLongitude());
        locationModal.setLatitude((float) location.getLatitude());
        setLastLocation(locationModal);

        //save latest location into preference
        pref.setLastLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private GPSTracker.OnLocationChangedListener onLocationListener;


    public void setOnLocationListener(GPSTracker.OnLocationChangedListener onLocationListener){
        this.onLocationListener = onLocationListener;
    }

    public interface OnLocationChangedListener{
        void onLocationCallback(Location location);
    }


}