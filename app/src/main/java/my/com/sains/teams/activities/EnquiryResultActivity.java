package my.com.sains.teams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.InspectUploadDao;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterDao;
import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.db.MobileDocDao;
import my.com.sains.teams.db.MyInspectUpload;
import my.com.sains.teams.db.MyInspectUploadDao;
import my.com.sains.teams.modal.UploadSummaryModal;
import my.com.sains.teams.utils.Consts;

public class EnquiryResultActivity extends AppCompatActivity {

    private DaoSession daoSession;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<UploadSummaryModal> uploadSummaryModalLists;
    private RadioButton passRadioBtn, failRadioBtn, allRadioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_result);

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
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("pass"); // filter only pass inspection
                }
            }
        });

        failRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("fail");
                }
            }
        });

        allRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked){
                    ((UploadSummaryAdapter)adapter).getFilter().filter("");// remove filter
                }
            }
        });

        daoSession = ((App) getApplication()).getDaoSession();
        uploadSummaryModalLists = new ArrayList<>();

        String regisId;
        String exchId;
        String callingPage;
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

//        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
//        Query<MyInspectUpload> myInspectUploadQuery = myInspectUploadDao.queryBuilder()
//                .orderDesc(MyInspectUploadDao.Properties.Inspect_id).build();
        InspectUploadDao inspectUploadDao = daoSession.getInspectUploadDao();
        Query<InspectUpload> inspectUploadQuery = inspectUploadDao.queryBuilder()
                .orderDesc(InspectUploadDao.Properties.Inspect_id)
                .where(InspectUploadDao.Properties.Regis_id.eq(regisId))
                .build();

//        QueryBuilder<MyInspectUpload> myInspectUploadQb = myInspectUploadDao.queryBuilder().orderDesc();
//        Join logRegisterJoin = myInspectUploadQb.join(LogRegisterDao.Properties.Exch_det_id, LogRegister.class);
//        myInspectUploadQb.join(logRegisterJoin, LogRegisterDao.Properties.Exch_id,
//                MobileDoc.class, MobileDocDao.Properties.Exch_id)
//                .where(MobileDocDao.Properties.Exch_id.eq(exchId));

        List<InspectUpload> allInspectionLists = inspectUploadQuery.list();

        // At least one fail will be fall into "fail" category
        QueryBuilder<InspectUpload> failInspectionQb = inspectUploadDao.queryBuilder()
                .whereOr(InspectUploadDao.Properties.Species_chk.eq(Consts.FAIL),
                        InspectUploadDao.Properties.Diameter_chk.eq(Consts.FAIL),
                        InspectUploadDao.Properties.Jh_hammer_chk.eq(Consts.FAIL),
                        InspectUploadDao.Properties.Length_chk.eq(Consts.FAIL),
                        InspectUploadDao.Properties.Lpi_chk.eq(Consts.FAIL),
                        InspectUploadDao.Properties.Pro_mark_chk.eq(Consts.FAIL));
        Join registerJoin = failInspectionQb.join(LogRegisterDao.Properties.Exch_det_id, LogRegister.class);
        failInspectionQb.join(registerJoin, LogRegisterDao.Properties.Exch_id,
                MobileDoc.class, MobileDocDao.Properties.Exch_id)
                .where(MobileDocDao.Properties.Exch_id.eq(exchId));

        List<InspectUpload> failInspectionLists = failInspectionQb.list();


        int passSize = allInspectionLists.size() - failInspectionLists.size();
        passRadioBtn.setText("Pass : "+ passSize);
        failRadioBtn.setText("Fail : " + failInspectionLists.size());
        allRadioBtn.setText("All : "+ allInspectionLists.size());

    }
}
