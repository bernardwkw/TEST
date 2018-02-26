package my.com.sains.teams.http;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import my.com.sains.teams.activities.App;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.DeviceSetupDao;
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.InspectUploadDao;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterDao;
import my.com.sains.teams.db.LogRegisterQuery;
import my.com.sains.teams.db.LogRegisterQueryDao;
import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.db.MobileDocDao;
import my.com.sains.teams.db.User;
import my.com.sains.teams.db.UserDao;
import my.com.sains.teams.utils.CipherAES;
import my.com.sains.teams.utils.Consts;
import my.com.sains.teams.utils.Pref;

/**
 * Created by User on 6/12/2017.
 */

public class Http extends AsyncTask<String, String, String>{

    private HttpURLConnection conn;
    private URL url = null;
    private String urlStr;
    private String jsonString;
    private String mode;
    private Activity activity;
    private MobileDocDao mobileDocDao;
    private LogRegisterDao logRegisterDao;
    private DeviceSetupDao deviceSetupDao;
    private UserDao userDao;
    private LogRegisterQueryDao logRegisterQueryDao;
    private InspectUploadDao inspectUploadDao;
    private ProgressDialog dialogLoad;
    private String exchDocId="";
    private JSONObject jsonObject;

    // download_list = M, user_profile = UP, download= U, upload= D
    public Http(String url, Activity activity, String mode){

        this.urlStr = url;
        this.activity = activity;
        this.mode = mode;

    }

    public Http(String url, Activity activity, String mode, String exchDocId){
        this.urlStr = url;
        this.activity = activity;
        this.mode = mode;
        this.exchDocId = exchDocId;
    }

    public Http(String url, Activity activity, String mode, JSONObject jsonObject){
        this.urlStr = url;
        this.activity = activity;
        this.mode = mode;
        this.jsonObject = jsonObject;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialogLoad = new ProgressDialog(activity);
        dialogLoad.setMessage("Please Wait...");
        dialogLoad.setCancelable(false);
        dialogLoad.show();

    }

    @Override
    protected String doInBackground(String... strings) {
        //save(Consts.json);
        return query();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialogLoad.dismiss();
    }

    @SuppressLint("MissingPermission")
    private String query(){

        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            //String encoded = Base64.encodeToString(jsonString.getBytes(), Base64.DEFAULT);

            url = new URL(urlStr);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Consts.READ_TIMEOUT);
            conn.setConnectTimeout(Consts.CONNECTION_TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            OutputStream os = conn.getOutputStream();

            ContentValues values = new ContentValues();

            Pref pref = new Pref(activity.getApplicationContext());

//            values.put(Consts.MOBILE_PHONE_ID, "IhbFddtVMKR9ZAxcix0wKQ==");
            TelephonyManager telephonyManager = (TelephonyManager)activity.getApplicationContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            values.put(Consts.MOBILE_PHONE_ID, CipherAES.aesEncode(telephonyManager.getDeviceId()));
            Log.e("device id", telephonyManager.getDeviceId());
            Log.e("encrypted", CipherAES.aesEncode(telephonyManager.getDeviceId()));

            if (mode.equals(Consts.DOWNLOAD_LIST)){ // get json without save
                values.put(Consts.TRAN_TYPE, Consts.DOWNLOAD_LIST);
                values.put(Consts.LOGINID, pref.getLoginId());
                values.put(Consts.PASSWORD, pref.getPassWord());

            }else if (mode.equals(Consts.USER_PROFILES)){ // get User profile and save
                values.put(Consts.TRAN_TYPE, Consts.USER_PROFILES);

            }else if (mode.equals(Consts.DOWNLOAD)){// get all data and save

                values.put(Consts.TRAN_TYPE, Consts.DOWNLOAD);
                values.put(Consts.SELECTED_DOC_ID, exchDocId);
                values.put(Consts.ADDONS, "U,E");
                values.put(Consts.LOGINID, pref.getLoginId());
                values.put(Consts.PASSWORD, pref.getPassWord());

            }else if (mode.equals(Consts.UPLOAD)){ //upload all data
                values.put(Consts.TRAN_TYPE, Consts.UPLOAD);
                values.put(Consts.INSPECTION_RESULT, jsonObject.toString());
                values.put(Consts.LOGINID, pref.getLoginId());
                values.put(Consts.PASSWORD, pref.getPassWord());
            }

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(values));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                this.jsonString = result.toString();

            }else {

                this.jsonString = null;
            }

            if (onHttpExecuted != null){
                onHttpExecuted.onHttpExecutedCallback(jsonString);
            }

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            if (onHttpExecuted != null){
                onHttpExecuted.onHttpExecutedCallback(null);
            }
        } finally {
            conn.disconnect();
        }

        //Log.e("query", jsonString);
        if(jsonString != null){
            if (mode.equals(Consts.USER_PROFILES) || mode.equals(Consts.DOWNLOAD)){
                save(jsonString);
            }

        }

        return jsonString;
    }

    private String getQuery(ContentValues values) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String key: values.keySet()){

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(values.get(key).toString(), "UTF-8"));
        }

        return result.toString();
    }

    private void save(String json){

        Log.e("json", json);
        Gson gson = new Gson();

        DaoSession daoSession = ((App) activity.getApplication()).getDaoSession();
        mobileDocDao = daoSession.getMobileDocDao();
        logRegisterDao = daoSession.getLogRegisterDao();
        logRegisterQueryDao = daoSession.getLogRegisterQueryDao();
        userDao = daoSession.getUserDao();
        deviceSetupDao = daoSession.getDeviceSetupDao();
        inspectUploadDao = daoSession.getInspectUploadDao();

        try {

            JSONObject jsonObject = new JSONObject(json);
            JSONArray userArray = jsonObject.getJSONArray(Consts.SECURITY_APP_USER);
            Log.e("User Array", "array");
            if (userArray != null){

                for (int i=0; i< userArray.length(); i++){
                    JSONObject userObject = userArray.getJSONObject(i);

                    User user = gson.fromJson(userObject.toString(), User.class);

                    userDao.insertOrReplace(user);
                }
            }

            JSONArray mobileDocArray = jsonObject.getJSONArray(Consts.EXCH_MOBILE_DOC);
            Log.e("mobile", "array");
            if (mobileDocArray != null){

                for (int i=0; i< mobileDocArray.length(); i++){
                    JSONObject mobileDocObject = mobileDocArray.getJSONObject(i);

                    MobileDoc mobileDoc = gson.fromJson(mobileDocObject.toString(), MobileDoc.class);

                    mobileDocDao.insertOrReplace(mobileDoc);
                }
            }

            JSONArray logRegisterArray = jsonObject.getJSONArray(Consts.MOBILE_LOG_REGISTER);
            Log.e("logRegister", "array");
            if (logRegisterArray != null){

                Log.e("logregister json", logRegisterArray.toString());

                for (int i=0; i< logRegisterArray.length(); i++){
                    JSONObject logRegisterObject = logRegisterArray.getJSONObject(i);

                    LogRegister logRegister = gson.fromJson(logRegisterObject.toString(), LogRegister.class);

                    logRegisterDao.insertOrReplace(logRegister);
                }
            }


            JSONArray logRegisterQueryArray = jsonObject.getJSONArray(Consts.LOG_REGISTRY_QUERY);
            Log.e("LogRegister Query", "array");
            if (logRegisterQueryArray != null){


                for (int i=0; i< logRegisterQueryArray.length(); i++){
                    JSONObject logRegisterQueryObject = logRegisterQueryArray.getJSONObject(i);

                    LogRegisterQuery logRegisterQuery = gson.fromJson(logRegisterQueryObject.toString(),
                            LogRegisterQuery.class);

                    logRegisterQueryDao.insertOrReplace(logRegisterQuery);
                }
            }

            JSONArray tranUploadArray = jsonObject.getJSONArray(Consts.INSPECT_TRAN_UPLAOD);
            if (tranUploadArray != null){

                for (int i=0; i< tranUploadArray.length(); i++){
                    JSONObject tranUploadObject = tranUploadArray.getJSONObject(i);

                    InspectUpload inspectUpload = gson.fromJson(tranUploadObject.toString(),
                            InspectUpload.class);

                    inspectUploadDao.insertOrReplace(inspectUpload);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (onFinishListener != null){
//            onFinishListener.onFinish();
//        }

    }


    private OnHttpExecuted onHttpExecuted;
//    private OnFinishListener onFinishListener;

    public void setOnHttpExecutedListener(OnHttpExecuted onHttpExecutedListener){
        this.onHttpExecuted = onHttpExecutedListener;
    }

    public interface OnHttpExecuted{
        void onHttpExecutedCallback(String jsonReturn);
    }

//    public void setOnFinishListener(OnFinishListener onFinishListener){
//        this.onFinishListener = onFinishListener;
//    }
//
//    public interface OnFinishListener{
//        Void onFinish();
//    }
}
