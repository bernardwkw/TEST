package my.com.sains.teams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.adapters.EnquiryResultAdapter;
import my.com.sains.teams.adapters.UploadSummaryAdapter;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.InspectUploadDao;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterQuery;
import my.com.sains.teams.db.LogRegisterQueryDao;
import my.com.sains.teams.modal.EnquiryResultModal;
import my.com.sains.teams.utils.Consts;

public class EnquiryResultActivity extends AppCompatActivity {

    private DaoSession daoSession;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EnquiryResultModal enquiryResult;
    private RadioButton passRadioBtn, failRadioBtn, allRadioBtn;
    private List<InspectUpload> allInspectionLists;
    private LogRegisterQuery logRegisterQuery;

    
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

        enquiryResult = new EnquiryResultModal();

        String regisId;
        Intent i = getIntent();
        regisId = i.getStringExtra(Consts.REGIS_ID);

        if (regisId != null){
            getData(regisId);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new EnquiryResultAdapter(allInspectionLists, logRegisterQuery);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getData(String regisId){

        InspectUploadDao inspectUploadDao = daoSession.getInspectUploadDao();
        Query<InspectUpload> inspectUploadQuery = inspectUploadDao.queryBuilder()
                .orderDesc(InspectUploadDao.Properties.Inspect_id)
                .where(InspectUploadDao.Properties.Regis_id.eq(regisId))
                .build();

        allInspectionLists = inspectUploadQuery.list();

        // At least one fail will be fall into "fail" category

        Query<InspectUpload> failInspectionQb = inspectUploadDao.queryBuilder().whereOr(
                InspectUploadDao.Properties.Species_chk.eq(Consts.FAIL),
                InspectUploadDao.Properties.Diameter_chk.eq(Consts.FAIL),
                InspectUploadDao.Properties.Jh_hammer_chk.eq(Consts.FAIL),
                InspectUploadDao.Properties.Length_chk.eq(Consts.FAIL),
                InspectUploadDao.Properties.Lpi_chk.eq(Consts.FAIL),
                InspectUploadDao.Properties.Pro_mark_chk.eq(Consts.FAIL)
        ).where(InspectUploadDao.Properties.Regis_id.eq(regisId))
                .build();

        List<InspectUpload> failInspectionLists = failInspectionQb.list();


        LogRegisterQueryDao logRegisterQueryDao = daoSession.getLogRegisterQueryDao();
        Query<LogRegisterQuery> logRegisterQueryQuery = logRegisterQueryDao.queryBuilder()
                .where(LogRegisterQueryDao.Properties.Regis_id.eq(regisId))
                .build();

        List<LogRegisterQuery> logRegisterQueryList = logRegisterQueryQuery.list();

        if (logRegisterQueryList.size() > 0){
            logRegisterQuery = logRegisterQueryList.get(0);
            enquiryResult.setLogRegisterQuery(logRegisterQueryList.get(0));

            if(allInspectionLists.size() > 0){
                enquiryResult.setInspectUpload(allInspectionLists);
            }
        }


        int passSize = allInspectionLists.size() - failInspectionLists.size();
        passRadioBtn.setText("Pass : "+ passSize);
        failRadioBtn.setText("Fail : " + failInspectionLists.size());
        allRadioBtn.setText("All : "+ allInspectionLists.size());

    }
}
