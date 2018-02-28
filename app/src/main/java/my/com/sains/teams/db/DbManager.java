package my.com.sains.teams.db;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import my.com.sains.teams.activities.App;
import my.com.sains.teams.utils.CipherAES;
import my.com.sains.teams.utils.Consts;

/**
 * Created by User on 27/2/2018.
 */

public class DbManager extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private File file;
    private String json;
    private ProgressDialog dialogLoad;

    public DbManager(Activity activity, String json){
        this.activity = activity;
        this.json = json;
    }

    public DbManager(Activity activity, File file){
        this.activity = activity;
        this.file = file;
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
    protected Void doInBackground(Void... voids) {

        if (json != null){
            saveJsonToDb(json);
        }else {
            saveJsonToDb(readJson(file));
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialogLoad.dismiss();
    }

    private void saveJsonToDb(String json){//import into device

        Gson gson = new Gson();

        DaoSession daoSession = ((App) activity.getApplication()).getDaoSession();
        MobileDocDao mobileDocDao = daoSession.getMobileDocDao();
        LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();
        LogRegisterQueryDao logRegisterQueryDao = daoSession.getLogRegisterQueryDao();
        UserDao userDao = daoSession.getUserDao();
        DeviceSetupDao deviceSetupDao = daoSession.getDeviceSetupDao();
        InspectUploadDao inspectUploadDao = daoSession.getInspectUploadDao();

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


    }

    private String readJson(File file){

        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (file.getAbsolutePath());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
        }
        catch(IOException ex) {
        }


        return CipherAES.aesDecode(line);
    }

}
