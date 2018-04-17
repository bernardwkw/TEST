package my.com.sains.teams.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.greendao.query.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.activities.App;
import my.com.sains.teams.activities.DownloadedSummaryActivity;
import my.com.sains.teams.adapters.DownloadAdapter;
import my.com.sains.teams.http.Http;
import my.com.sains.teams.modal.DownloadModal;
import my.com.sains.teams.utils.Consts;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<MobileDoc> downloadedMobileDocList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Http http;

    public DownloadFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);
        // Inflate the layout for this fragment
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        mRecyclerView = view.findViewById(R.id.download_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        Button downloadBtn = view.findViewById(R.id.download_btn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exchIds = ((DownloadAdapter) mAdapter).getSelectedExchId();
                http = new Http("http://172.26.80.12:8041/timber/instruct_processMobile?",
                        getActivity(), Consts.DOWNLOAD, exchIds);
                http.execute();

                countDownTimer.start();

                Log.e("exch_id", exchIds);
            }
        });

        return view;
    }


    CountDownTimer countDownTimer = new CountDownTimer(1000,200) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {

            if (http.getStatus() == AsyncTask.Status.FINISHED){
                getData();
                Log.e("countdown", "finish");
            } else{
                countDownTimer.start();
                Log.e("countdown", "continue");
            }

        }
    };

    private List<DownloadModal> mobileDocList;

    private void getData(){

        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        final MobileDocDao mobileDocDao = daoSession.getMobileDocDao();


        Query<MobileDoc> mobileDocQuery = mobileDocDao.queryBuilder().build();

        downloadedMobileDocList = mobileDocQuery.list();

        mobileDocList = new ArrayList<>();
        mobileDocList.clear();

        // display downloaded data even there is no data return

        for (MobileDoc mobileDoc: downloadedMobileDocList){

            DownloadModal downloadModal = new DownloadModal();
            downloadModal.setMobileDoc(mobileDoc);
            downloadModal.setDownloaded(true);

            mobileDocList.add(downloadModal); // list which downloaded

        }

        Http http = new Http("http://172.26.80.12:8041/timber/instruct_processMobile?",
                getActivity(), Consts.DOWNLOAD_LIST);
        http.execute();

        http.setOnHttpExecutedListener(new Http.OnHttpExecuted() {
            @Override
            public void onHttpExecutedCallback(String jsonReturn) {

                List<MobileDoc> tmpMobileArry = new ArrayList<>();

                if (jsonReturn != null){
                    Log.e("json return", jsonReturn);
                    // to get list that not downloaded
                    try {
                        JSONObject jsonObject = new JSONObject(jsonReturn);
                        JSONArray jsonArray = jsonObject.getJSONArray(Consts.EXCH_MOBILE_DOC);
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject mobileDocObject = jsonArray.getJSONObject(i);
                            MobileDoc mobileDoc = new MobileDoc();
                            mobileDoc.setBatch_no(mobileDocObject.getString(Consts.REF_NO));//batch_no-->ref_no
                            mobileDoc.setDoc_type(mobileDocObject.getString(Consts.DOC_TYPE));
                            mobileDoc.setTotal_logs(mobileDocObject.getInt(Consts.TOTAL_LOGS));
                            mobileDoc.setAcct_code(mobileDocObject.getString(Consts.ACCT_CODE));
                            mobileDoc.setExch_id(mobileDocObject.getString(Consts.EXCH_ID));
                            mobileDoc.setName(mobileDocObject.getString(Consts.NAME));

                            tmpMobileArry.add(mobileDoc);

                            for (int j=0; j<downloadedMobileDocList.size(); j++){

                                if (downloadedMobileDocList.get(j).getExch_id()
                                        .equals(mobileDocObject.getString(Consts.EXCH_ID))){
                                    tmpMobileArry.remove(mobileDoc);
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // add batch list not downloaded at top
                for (int i=0; i< tmpMobileArry.size(); i++){

                    DownloadModal downloadModal = new DownloadModal();
                    downloadModal.setMobileDoc(tmpMobileArry.get(i));
                    downloadModal.setDownloaded(false);

                    mobileDocList.add(i, downloadModal);
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        mAdapter = new DownloadAdapter(mobileDocList);
                        mRecyclerView.setAdapter(mAdapter);

                        ((DownloadAdapter) mAdapter).setOnItemClickListener(new DownloadAdapter
                                .MyClickListener() {
                            @Override
                            public void onItemClick(String exchId, View v) {
                                Log.i("Clicked", " Clicked on Item " + exchId);
                                //upload();
                                Intent i = new Intent(getActivity(), DownloadedSummaryActivity.class);
                                i.putExtra(Consts.SUMMARY_ACTIVITY, Consts.MOBILE_LOG_REGISTER_SUMMARY);
                                i.putExtra(Consts.EXCH_ID, exchId);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
            getData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAdapter != null){
            ((DownloadAdapter) mAdapter).setOnItemClickListener(null);
        }
    }

}
