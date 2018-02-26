package my.com.sains.teams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.adapters.UploadSummaryAdapter;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterDao;
import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.db.MobileDocDao;
import my.com.sains.teams.db.MyInspectUpload;
import my.com.sains.teams.db.MyInspectUploadDao;
import my.com.sains.teams.modal.UploadSummaryModal;
import my.com.sains.teams.utils.Consts;

public class UploadSummaryActivity extends AppCompatActivity {

    private DaoSession daoSession;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UploadSummaryModal> uploadSummaryModalLists;
    private RadioButton passRadioBtn, failRadioBtn, allRadioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_summary);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.upload_summary_recycle_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(layoutManager);

        passRadioBtn = findViewById(R.id.pass_rb);
        failRadioBtn = findViewById(R.id.fail_rb);
        allRadioBtn = findViewById(R.id.all_rb);

        passRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("pass");
                }
            }
        });

        failRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("fail");
                }
            }
        });

        allRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("");
                }
            }
        });

        daoSession = ((App) getApplication()).getDaoSession();
        uploadSummaryModalLists = new ArrayList<>();

        String exchId;
        Intent i = getIntent();
        exchId = i.getStringExtra(Consts.EXCH_ID);

        if (exchId != null){
            getData(exchId);

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new UploadSummaryAdapter(uploadSummaryModalLists);
                recyclerView.setAdapter(adapter);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void getData(String exchId){

        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
        Query<MyInspectUpload> myInspectUploadQuery = myInspectUploadDao.queryBuilder()
                .orderDesc(MyInspectUploadDao.Properties.Inspect_id).build();

        QueryBuilder<MyInspectUpload> myInspectUploadQb = myInspectUploadDao.queryBuilder().orderDesc();
        Join logRegisterJoin = myInspectUploadQb.join(LogRegisterDao.Properties.Exch_det_id, LogRegister.class);
        myInspectUploadQb.join(logRegisterJoin, LogRegisterDao.Properties.Exch_id,
                MobileDoc.class, MobileDocDao.Properties.Exch_id)
                .where(MobileDocDao.Properties.Exch_id.eq(exchId));

        List<MyInspectUpload> allInspectionLists = myInspectUploadQb.list();
        for (MyInspectUpload myInspectUpload : allInspectionLists){
            Log.e("lists", myInspectUpload.getRegis_id());
        }

        // At least one fail will be fall into "fail" category
        QueryBuilder<MyInspectUpload> failInspectionQb = myInspectUploadDao.queryBuilder()
                .whereOr(MyInspectUploadDao.Properties.Species_chk.eq(Consts.FAIL),
                        MyInspectUploadDao.Properties.Diameter_chk.eq(Consts.FAIL),
                        MyInspectUploadDao.Properties.Jh_hammer_chk.eq(Consts.FAIL),
                        MyInspectUploadDao.Properties.Length_chk.eq(Consts.FAIL),
                        MyInspectUploadDao.Properties.Lpi_chk.eq(Consts.FAIL),
                        MyInspectUploadDao.Properties.Pro_mark_chk.eq(Consts.FAIL));
        Join registerJoin = failInspectionQb.join(LogRegisterDao.Properties.Exch_det_id, LogRegister.class);
        failInspectionQb.join(registerJoin, LogRegisterDao.Properties.Exch_id,
                MobileDoc.class, MobileDocDao.Properties.Exch_id)
                .where(MobileDocDao.Properties.Exch_id.eq(exchId));


        List<MyInspectUpload> failInspectionLists = failInspectionQb.list();

        for (MyInspectUpload myInspectUpload : failInspectionLists){
            Log.e("fail Lists", myInspectUpload.getRegis_id());
        }

        List<MyInspectUpload> myInspectUploadList = myInspectUploadQuery.list();
        if (myInspectUploadList.size()>0){

            LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();
            MobileDocDao mobileDocDao = daoSession.getMobileDocDao();

            for (MyInspectUpload myInspectUpload: myInspectUploadList){

                Query<LogRegister> logRegisterQuery = logRegisterDao.queryBuilder()
                        .where(LogRegisterDao.Properties.Exch_det_id.eq(myInspectUpload.getExch_det_id()))
                        .build();

                List<LogRegister> logRegisterList = logRegisterQuery.list();
                if (logRegisterList.size()>0){
                    Query<MobileDoc> mobileDocQuery = mobileDocDao.queryBuilder()
                            .where(MobileDocDao.Properties.Exch_id.eq(logRegisterList.get(0).getExch_id()))
                            .build();

                    List<MobileDoc> mobileDocList = mobileDocQuery.list();

                    if(mobileDocList.size()>0 && mobileDocList.get(0).getExch_id().equals(exchId)){

                        UploadSummaryModal uploadSummaryModal = new UploadSummaryModal();
                        uploadSummaryModal.setLogRegister(logRegisterList.get(0));
                        uploadSummaryModal.setMyInspectUpload(myInspectUpload);

                        uploadSummaryModalLists.add(uploadSummaryModal);
                    }



                }

            }

        }

        int passSize = allInspectionLists.size() - failInspectionLists.size();

        passRadioBtn.setText("Pass : "+ passSize);
        failRadioBtn.setText("Fail : " + failInspectionLists.size());
        allRadioBtn.setText("All : "+ allInspectionLists.size());

    }
}
