package my.com.sains.teams.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.databinding.ActivityEnquiryBinding;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.InspectUpload;
import my.com.sains.teams.db.InspectUploadDao;
import my.com.sains.teams.db.LogRegisterQuery;
import my.com.sains.teams.db.LogRegisterQueryDao;
import my.com.sains.teams.modal.EnquiryModal;
import my.com.sains.teams.utils.BarcodeScanner;

public class EnquiryActivity extends AppCompatActivity implements BarcodeScanner.OnBarcodeScan{

//    private TextView batchNoTv, refNoTv, nameTv, licenseeTv, lpiTv, pmTv, campTv, coupeTv, blockTv,
//            jhTv, specTv, diameterTv, lengthTv, usernameTv, dateTv, latTv, longTv, remarksTv;
//    private TextView lpiTittle, pmTittle, jhTittle, specTittle, diameterTittle, lengthTittle, remarksTittle;
//    private LinearLayout resultLL;
//    private EditText lpiEt, pmEt;
    private DaoSession daoSession;
    private List<InspectUpload> inspectUpload;
    private List<LogRegisterQuery> logRegister;
    private List<EnquiryModal> enquiryLists;

    private ActivityEnquiryBinding binding;

    private BarcodeScanner barcodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enquiry);

        daoSession = ((App) getApplication()).getDaoSession();

        barcodeScanner = new BarcodeScanner(EnquiryActivity.this);
        barcodeScanner.setOnBarcodeScan(this);
        barcodeScanner.initScanner();

        Log.e("brand", Build.BRAND);
        Log.e("model", Build.MODEL);
        Log.e("product", Build.PRODUCT);

    }

    public void qrscan(View view) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CustomScannerActivity.class);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    public void search(View v){

        String lpiText = binding.lpiEt.getText().toString().toUpperCase();
        String pmText = binding.pmEt.getText().toString().toUpperCase();

        binding.scrollView.post(new Runnable() {
            public void run() {
                binding.scrollView.fullScroll(binding.scrollView.FOCUS_UP);
            }
        });

        getData(lpiText, pmText);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        barcodeScanner.destroyBarcode();
    }

    private void getData(String lpiText, String pmText){

//        logRegister = new LogRegisterQuery();
//        inspectUpload = new InspectUpload();

        LogRegisterQueryDao logRegisterQueryDao = daoSession.getLogRegisterQueryDao();
        InspectUploadDao inspectUploadDao = daoSession.getInspectUploadDao();

        QueryBuilder<InspectUpload> qb = inspectUploadDao.queryBuilder();
        qb.join(LogRegisterQueryDao.Properties.Regis_id,LogRegisterQuery.class)
                .where(LogRegisterQueryDao.Properties.Lpi_no.eq(lpiText),
                        LogRegisterQueryDao.Properties.Property_mark.eq(pmText));

        Query<LogRegisterQuery> logRegisterQuery = logRegisterQueryDao.queryBuilder()
                .where(LogRegisterQueryDao.Properties.Lpi_no.eq(lpiText),
                        LogRegisterQueryDao.Properties.Property_mark.eq(pmText))
                .build();
        logRegister = logRegisterQuery.list();

        inspectUpload = qb.list();


        if(logRegister.size() > 0){
            if (inspectUpload.size() >0){
//                changeInspectionTittle(true);
                show();
            }else {

//                changeInspectionTittle(false);
            }
        }else {
            Toast.makeText(getApplicationContext(), "No record found", Toast.LENGTH_LONG).show();
            binding.resultLl.setVisibility(View.GONE);
        }
    }

    private void show(){

        Drawable pass = getResources().getDrawable(R.drawable.ic_check_circle);
        Drawable fail = getResources().getDrawable(R.drawable.ic_cancel_circle);
        Drawable unknown = getResources().getDrawable(R.drawable.ic_unknown);

        if (logRegister.size()>0){
            LogRegisterQuery tmp = logRegister.get(0);
            binding.campTv.setText(tmp.getCamp_code());
            binding.coupeTv.setText(tmp.getCoupe_no());
            binding.blockTv.setText(tmp.getBlock_no());

            if (tmp.getPec_ref_no() != null){
                binding.raRefTv.setText(tmp.getRa_ref_no());
            }

            binding.dprRefNoTv.setText(tmp.getDpr_ref_no());
            binding.trpRefNoTv.setText(tmp.getTrp_ref_no());
            binding.logSerialNoTv.setText(tmp.getLog_serial_no());
            binding.hammerMarkNoTv.setText(tmp.getHammer_mark_no());

            binding.licNameTv.setText(tmp.getName());
        }


        if(inspectUpload.size()>0){

            binding.resultLl.setVisibility(View.VISIBLE);

            InspectUpload tmp = inspectUpload.get(0);
            LogRegisterQuery logTmp = logRegister.get(0);

            binding.lpiTv.setText(logTmp.getLpi_no());
            binding.pmTv.setText(logTmp.getProperty_mark());
            binding.diameterTv.setText(logTmp.getDiameter().toString());
            binding.defectDiameterTv.setText(logTmp.getDefect_dia().toString());

            binding.speciesTv.setText(logTmp.getSpecies_code());
            binding.lengthTv.setText(logTmp.getLength().toString());

            binding.jhMarkTv.setText(logTmp.getHammer_mark_no());

            if(tmp.getUser_name() != null)
                binding.usernameTv.setText(tmp.getUser_name());

            if(tmp.getInspect_date_time() != null)
                binding.dateTv.setText(tmp.getInspect_date_time());

            if(tmp.getGps_lat() != null){
                binding.latTv.setText(tmp.getGps_lat().toString());
            }

            if(tmp.getGps_long() != null){
                binding.longTv.setText(tmp.getGps_long().toString());
            }

            enquiryLists = new ArrayList<>();

            EnquiryModal lpiInfo = new EnquiryModal();
            lpiInfo.setCheck_status(tmp.getLpi_chk());
            lpiInfo.setTextview(binding.lpiTv);
            lpiInfo.setTitteTv(binding.lpiTittle);
            enquiryLists.add(0, lpiInfo);

            EnquiryModal pmInfo = new EnquiryModal();
            pmInfo.setCheck_status(tmp.getPro_mark_chk());
            pmInfo.setTextview(binding.pmTv);
            pmInfo.setTitteTv(binding.pmTittle);
            enquiryLists.add(1,pmInfo);

            EnquiryModal jhInfo = new EnquiryModal();
            jhInfo.setCheck_status(tmp.getJh_hammer_chk());
            jhInfo.setTextview(binding.jhMarkTv);
            jhInfo.setTitteTv(binding.jhTittle);
            enquiryLists.add(2,jhInfo);

            EnquiryModal speciesInfo = new EnquiryModal();
            speciesInfo.setCheck_status(tmp.getSpecies_chk());
            speciesInfo.setTextview(binding.speciesTv);
            speciesInfo.setTitteTv(binding.speciesTittle);
            enquiryLists.add(3,speciesInfo);

            EnquiryModal diameterInfo = new EnquiryModal();
            diameterInfo.setCheck_status(tmp.getDiameter_chk());
            diameterInfo.setTextview(binding.diameterTv);
            diameterInfo.setTitteTv(binding.diameterTittle);
            enquiryLists.add(4,diameterInfo);

            EnquiryModal lengthInfo = new EnquiryModal();
            lengthInfo.setCheck_status(tmp.getLength_chk());
            lengthInfo.setTextview(binding.lengthTv);
            lengthInfo.setTitteTv(binding.lengthTittle);
            enquiryLists.add(5, lengthInfo);
}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String[] pieces = result.getContents().split(",");
                if (pieces.length == 4){

                    String lpi = pieces[0];
                    String pm = pieces[1];

                    getData(lpi, pm);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid QR Code!", Toast.LENGTH_LONG).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBarcodeCallback(String decodedString) {
        Log.e("Callback", decodedString);
        String[] pieces = decodedString.split(",");
        if (pieces.length == 4){

            String lpi = pieces[0];
            String pm = pieces[1];
            getData(lpi, pm);

        }else {

        }

        binding.scrollView.post(new Runnable() {
            public void run() {
                binding.scrollView.fullScroll(binding.scrollView.FOCUS_UP);
            }
        });
    }
}
