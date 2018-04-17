package my.com.sains.teams.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.activities.App;
import my.com.sains.teams.activities.UploadSummaryActivity;
import my.com.sains.teams.adapters.UploadAdapter;
import my.com.sains.teams.http.Http;
import my.com.sains.teams.modal.UploadModal;
import my.com.sains.teams.utils.Consts;


public class UploadFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private MyInspectUploadDao myInspectUploadDao;
    private MobileDocDao mobileDocDao;
    private LogRegisterDao logRegisterDao;
    private List<UploadModal> uploadModalList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout relativeLayout;
    private DaoSession daoSession;

    public UploadFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }
    Snackbar snackbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_upload, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData getData = new GetData();
                getData.execute();
            }
        });

        final Button uploadBtn = view.findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });

        daoSession = ((App) getActivity().getApplication()).getDaoSession();

        mRecyclerView = view.findViewById(R.id.upload_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }


    private void getData(){
        uploadModalList = new ArrayList<>();
        uploadModalList.clear();
        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
        MobileDocDao mobileDocDao = daoSession.getMobileDocDao();
        LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();

        //get mobileDoc data
        Query<MobileDoc> mobileDocQuery = mobileDocDao.queryBuilder()
                .orderAsc(MobileDocDao.Properties.Exch_id)
                .build();

        List<MobileDoc> mobileDocList = mobileDocQuery.list();

        //add all data into uploadModal
        for (MobileDoc mobileDoc: mobileDocList){

            QueryBuilder<MyInspectUpload> qb = myInspectUploadDao.queryBuilder()
                    .orderAsc(MyInspectUploadDao.Properties.Inspect_id);
            qb.join( LogRegisterDao.Properties.Exch_det_id,LogRegister.class)
                    .where(LogRegisterDao.Properties.Exch_id.eq(mobileDoc.getExch_id()),
                            LogRegisterDao.Properties.Spec_check.eq("Y"));

            QueryBuilder<MyInspectUpload> totalQb = myInspectUploadDao.queryBuilder()
                    .orderAsc(MyInspectUploadDao.Properties.Inspect_id);
            totalQb.join( LogRegisterDao.Properties.Exch_det_id,LogRegister.class)
                    .where(LogRegisterDao.Properties.Exch_id.eq(mobileDoc.getExch_id()));

            List<MyInspectUpload> mySpecCheckLists = qb.list();
            List<MyInspectUpload> myTotalLists = totalQb.list();

            Query<LogRegister> logRegisterQuery = logRegisterDao.queryBuilder()
                    .where(LogRegisterDao.Properties.Spec_check.eq("Y"),
                            LogRegisterDao.Properties.Exch_id.eq(mobileDoc.getExch_id())).build();

            if(myTotalLists.size()>0){

                float checkProgress = ((float) mySpecCheckLists.size()/ (float)logRegisterQuery.list().size())*100;

                UploadModal uploadModal = new UploadModal();
                uploadModal.setMobileDocMember(mobileDoc);
                uploadModal.setMyInspectUploadList(myTotalLists);
                uploadModal.setSpecCheckProgress((int) checkProgress);
                uploadModal.setSpecChecked(mySpecCheckLists.size());
                uploadModal.setTotalSpecCheck(logRegisterQuery.list().size());
                uploadModalList.add(uploadModal);

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GetData getData = new GetData();
        getData.execute();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private Http http;
    private List<MyInspectUpload> checkedInspectionLists;
    public void upload(){

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        Gson gson = builder.create();

        final JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        checkedInspectionLists = ((UploadAdapter)mAdapter).getCheckedData();

        if (checkedInspectionLists.size()>0){
            try {
            for (MyInspectUpload myInspectUpload : checkedInspectionLists){
                String jsonStr = gson.toJson(myInspectUpload);
                Log.e("jsonStr", jsonStr);
                JSONObject myInspectionObject = new JSONObject();

                myInspectionObject.put(Consts.INSPECT_ID, myInspectUpload.getInspect_id());
                myInspectionObject.put(Consts.REGIS_ID, myInspectUpload.getRegis_id());
                myInspectionObject.put(Consts.EXCH_DET_ID, myInspectUpload.getExch_det_id());
                myInspectionObject.put(Consts.USER_LOGIN_ID, myInspectUpload.getUser_login_id());
                myInspectionObject.put(Consts.USER_NAME, myInspectUpload.getUser_name());
                myInspectionObject.put(Consts.INSPECT_DATE_TIME, myInspectUpload.getInspect_date_time());
                myInspectionObject.put(Consts.GPS_LAT, myInspectUpload.getGps_lat());
                myInspectionObject.put(Consts.GPS_LONG, myInspectUpload.getGps_long());
                myInspectionObject.put(Consts.LPI_CHK, myInspectUpload.getLpi_chk());
                myInspectionObject.put(Consts.SPECIES_CHK, myInspectUpload.getSpecies_chk());
                myInspectionObject.put(Consts.PRO_MARK_CHK, myInspectUpload.getPro_mark_chk());
                myInspectionObject.put(Consts.JH_HAMMER_CHK, myInspectUpload.getJh_hammer_chk());
                myInspectionObject.put(Consts.DIAMETER_CHK, myInspectUpload.getDiameter_chk());
                myInspectionObject.put(Consts.LENGTH_CHK, myInspectUpload.getLength_chk());
                myInspectionObject.put(Consts.REMARKS, myInspectUpload.getRemarks());

                jsonArray.put(myInspectionObject);
//                jsonArray.put(jsonStr.toString());
            }

            jsonObject.put("upload_data", jsonArray);
                Log.e("JsonArray", jsonObject.toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            http = new Http("http://172.26.80.12:8041/timber/instruct_processMobile?"
                    ,getActivity(),Consts.UPLOAD, jsonObject);
            http.execute();
            http.setOnHttpExecutedListener(new Http.OnHttpExecuted() {
                @Override
                public void onHttpExecutedCallback(String jsonReturn) {

                    if (jsonReturn != null){
                        Log.e("JsonArray", jsonReturn);

                        try {
                            JSONObject returnObject = new JSONObject(jsonReturn);
                            if (returnObject.getString(Consts.RETCODE).equals("0") ||
                                    returnObject.getString(Consts.RETMESSAGE)
                                            .contains("Inspection result exist")){
                                Log.e("delte", "here");
                                for (MyInspectUpload myInspectUpload: checkedInspectionLists){

                                    LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();
                                    Query<LogRegister> logRegisterQuery = logRegisterDao
                                            .queryBuilder()
                                            .where(LogRegisterDao.Properties.Exch_det_id.eq(myInspectUpload.getExch_det_id()))
                                            .build();
                                    LogRegister logRegister = logRegisterQuery.list().get(0);

                                    logRegisterDao.delete(logRegister);


                                    MobileDocDao mobileDocDao = daoSession.getMobileDocDao();
                                    Query<MobileDoc> mobileDocQuery = mobileDocDao
                                            .queryBuilder()
                                            .where(MobileDocDao.Properties.Exch_id.eq(logRegister.getExch_id()))
                                            .build();

                                    if (mobileDocQuery.list().size()>0){
                                        mobileDocDao.delete(mobileDocQuery.list().get(0));
                                    }

                                    MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
                                    myInspectUploadDao.delete(myInspectUpload);

                                }


                                countDownTimer.start();
                            }else {
                                Log.e("returnMsg",returnObject.getString("retMessage"));
                                //Looper.prepare();
//                                Toast.makeText(getActivity()
//                                        , returnObject.getString("retMessage").toString()
//                                        , Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Snackbar snackbar = Snackbar.make(relativeLayout
                                , "Connection Error! Try Again Later", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                    }

                }
            });
            //countDownTimer.start();
        }


    }

    CountDownTimer countDownTimer = new CountDownTimer(1000,200) {
        @Override
        public void onTick(long l) {

            if(http.getStatus() == AsyncTask.Status.FINISHED){
                GetData getData = new GetData();
                getData.execute();
            }
        }

        @Override
        public void onFinish() {

            if (http.getStatus() != AsyncTask.Status.FINISHED){
                countDownTimer.start();
            }
        }
    };

    private class GetData extends AsyncTask<Void, Void, Void>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading Data");
            progressDialog.setCancelable(false);
            //progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                    mAdapter = new UploadAdapter(uploadModalList);
                    mRecyclerView.setAdapter(mAdapter);

                    ((UploadAdapter) mAdapter).setOnItemClickListener(new UploadAdapter
                            .MyClickListener() {
                        @Override
                        public void onItemClick(String exchId, View v) {
                            Log.i("Clicked", " Clicked on Item " + exchId);
                            Intent i = new Intent(getActivity(), UploadSummaryActivity.class);
                            i.putExtra(Consts.EXCH_ID, exchId);
                            startActivity(i);
                        }

                        @Override
                        public void onCheckClick(final CheckBox checkBox) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Inspection Incomplete");
                            builder.setMessage("Batch data will be remove from device after upload. Are you sure to proceed?");
                            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    checkBox.setChecked(true);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });


                }
            });
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.menu_chat_fragment, menu);
//            super.onCreateOptionsMenu(menu, inflater);
//    }

}
