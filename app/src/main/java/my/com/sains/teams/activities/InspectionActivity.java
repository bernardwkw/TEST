package my.com.sains.teams.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import my.com.sains.teams.R;
import my.com.sains.teams.databinding.ActivityInspectionBinding;
import my.com.sains.teams.databinding.ItemInspectDialogBinding;
import my.com.sains.teams.databinding.ItemSlideviewBinding;
import my.com.sains.teams.db.DaoSession;
import my.com.sains.teams.db.LogRegister;
import my.com.sains.teams.db.LogRegisterDao;
import my.com.sains.teams.db.MobileDoc;
import my.com.sains.teams.db.MobileDocDao;
import my.com.sains.teams.db.MyInspectUpload;
import my.com.sains.teams.db.MyInspectUploadDao;
import my.com.sains.teams.modal.StatusModal;
import my.com.sains.teams.services.GPSTracker;
import my.com.sains.teams.slider.SlideView;
import my.com.sains.teams.utils.Consts;
import my.com.sains.teams.utils.DateTime;
import my.com.sains.teams.utils.Permission;
import my.com.sains.teams.utils.Pref;
import my.com.sains.teams.utils.Scanner;

public class InspectionActivity extends AppCompatActivity implements
        SlideView.OnSlideCompleteListener , LocationListener{

    private String lpi = null;
    private String pm = null;
    private EditText lpiEt, pmEt;
    private DaoSession daoSession;
    private List<StatusModal> statusModalList;
    private List<LogRegister> logRegisterList;
    private List<MobileDoc> mobileDocList;
    private DateTime dateTime;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private double longitude = 0;
    private double latitude = 0;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private boolean isModelOpen= false;

    private ScanInterface scanDecode;

    private Drawable pass, fail, unknown;

    private ActivityInspectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_inspection);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_inspection);

        lpiEt = findViewById(R.id.lpi_et);
        pmEt = findViewById(R.id.pm_et);

        statusModalList = new ArrayList<>();

        daoSession = ((App) getApplication()).getDaoSession();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        progressDialog = new ProgressDialog(InspectionActivity.this);
        progressDialog.setMessage("Getting GPS Location. \nPlease Wait...");
        progressDialog.setCancelable(false);

        pass = getResources().getDrawable(R.drawable.ic_check_circle);
        fail = getResources().getDrawable(R.drawable.ic_cancel_circle);
        unknown = getResources().getDrawable(R.drawable.ic_unknown);

        Log.e("device name", Build.MODEL + "\n"+ Build.BRAND + "\n" + Build.PRODUCT);

        if(Scanner.isScanner()){

            scanDecode = new ScanDecode(this);
            scanDecode.initService("true");

            scanDecode.getBarCode(new ScanInterface.OnScanListener() {
                @Override
                public void getBarcode(String s) {
                    String[] pieces = s.split(",");
                    if (pieces.length == 4){

                        lpi = pieces[0];
                        pm = pieces[1];

                        getData(lpi, pm);
                    }
                }
            });
        }

    }



    private GPSTracker gpsTracker;

    private void getLocation(){

        if (!Permission.checkLocationPermissions(getApplicationContext())) {
            Permission.startLocationPermissionRequest(InspectionActivity.this);
        }else {
            gpsTracker = new GPSTracker(InspectionActivity.this);
            if (gpsTracker.canGetLocation()){

                //to disable user from using the app after location loaded, uncomment to enable
                //progressDialog.show();
                //countDownTimer.start();

                gpsTracker.setOnLocationListener(new GPSTracker.OnLocationChangedListener() {
                    @Override
                    public void onLocationCallback(Location location) {

                        if (location.getLongitude()>0 && location.getLatitude()>0){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            stopLocationTracking();
                            progressDialog.dismiss();
                            //alertDialog.dismiss();
                        }
                    }
                });

            }else {

                gpsTracker.showSettingsAlert();
            }
        }
    }

    private void stopLocationTracking(){

        if (gpsTracker != null)
            gpsTracker.stopUsingGPS();
        gpsTracker.setOnLocationListener(null);
    }

    private AlertDialog alertDialog;
    CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            if (longitude == 0 ){

                AlertDialog.Builder builder = new AlertDialog.Builder(InspectionActivity.this);
                builder.setMessage("Fail to get GPS location");
                builder.setCancelable(false);
                builder.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getLocation();

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                if (alertDialog != null){
                    alertDialog.dismiss();
                }
                alertDialog = builder.create();
                alertDialog.show();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if(longitude != 0 && latitude != 0)
            getLocation();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(scanDecode != null){

            scanDecode.onDestroy();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
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

    private List<MyInspectUpload> myInspectUploadList;

    private void getData(String lpi, String pm) {

        LogRegisterDao logRegisterDao = daoSession.getLogRegisterDao();
        Query<LogRegister> registerQuery = logRegisterDao.queryBuilder().where(
                LogRegisterDao.Properties.Lpi_no.eq(lpi),
                LogRegisterDao.Properties.Pro_mark_reg_no.eq(pm)
        ).build();
        logRegisterList = registerQuery.list();


        // retrieve inspection data matched with lpi and pm
        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
        QueryBuilder<MyInspectUpload> inspectQb = myInspectUploadDao.queryBuilder();
        inspectQb.join(LogRegisterDao.Properties.Exch_det_id,LogRegister.class)
                .where(LogRegisterDao.Properties.Lpi_no.eq(lpi),
                        LogRegisterDao.Properties.Pro_mark_reg_no.eq(pm));
        myInspectUploadList = inspectQb.list();

        //
        MobileDocDao mobileDocDao = daoSession.getMobileDocDao();
        QueryBuilder<MobileDoc> mobileQb = mobileDocDao.queryBuilder();
        mobileQb.join(LogRegister.class, LogRegisterDao.Properties.Exch_id)
                .where(LogRegisterDao.Properties.Lpi_no.eq(lpi),
                        LogRegisterDao.Properties.Pro_mark_reg_no.eq(pm));
        mobileDocList = mobileQb.list();

        if (mobileDocList.size()>0 && logRegisterList.size()>0){

            displayText(logRegisterList.get(0), mobileDocList.get(0));
            binding.resultLl.setVisibility(View.VISIBLE);

            if(myInspectUploadList.size() >0){

                displayLastInspection(myInspectUploadList.get(0));

                binding.scrollView.post(new Runnable() {
                    public void run() {
                        binding.scrollView.fullScroll(binding.scrollView.FOCUS_UP);
                    }
                });

            }else {

                openDialog();

            }

        }else {
            //resultLL.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No Data Found !", Toast.LENGTH_LONG).show();
        }


    }

    private void displayText(LogRegister logRegister, MobileDoc mobileDoc) {

        binding.campTv.setText(logRegister.getCamp_code());
        binding.coupeTv.setText(logRegister.getCoupe_no());
        binding.blockTv.setText(logRegister.getBlock_no());

        binding.batchTv.setText(mobileDoc.getBatch_no());
        binding.licNoTv.setText(mobileDoc.getAcct_code());
        binding.licenseeTv.setText(mobileDoc.getName());

        binding.docTypeTv.setText(mobileDoc.getDoc_type());
        if (mobileDoc.getDoc_type().equals("R")){
            binding.licenseeTransporterTittle.setText("Licensee");
            if (mobileDoc.getTrp_ref_no() != null){
                binding.refTv.setText(mobileDoc.getTrp_ref_no());
            }
        }else if (mobileDoc.getDoc_type().equals("T")){
            binding.licenseeTransporterTittle.setText("Transporter");
            if (mobileDoc.getTrp_ref_no() != null){
                binding.refTv.setText(mobileDoc.getSecurity_no());
            }
        }

        binding.lpiTv.setText(logRegister.getLpi_no());
        binding.pmTv.setText(logRegister.getPro_mark_reg_no());

        binding.speciesTv.setText(logRegister.getSpecies_code());
        binding.diameterTv.setText(logRegister.getDiameter().toString());
        binding.lengthTv.setText(logRegister.getLength().toString());

        binding.jhMarkTv.setText(mobileDoc.getHammer_mark_no());

        //resultLL.setVisibility(View.GONE);
    }

    private void displayLastInspection(MyInspectUpload myInspectUpload){

        statusModalList = null;
        statusModalList = new ArrayList<>();

        StatusModal lpiStatus = new StatusModal();
        lpiStatus.setTextView(binding.lpiTv);
        lpiStatus.setStatus(myInspectUpload.getLpi_chk());
        lpiStatus.setTittleTv(binding.lpiTittle);
        statusModalList.add(0, lpiStatus);

        StatusModal pmStatus = new StatusModal();
        pmStatus.setStatus(myInspectUpload.getPro_mark_chk());
        pmStatus.setTextView(binding.pmTv);
        pmStatus.setTittleTv(binding.pmTittle);
        statusModalList.add(1, pmStatus);

        StatusModal jhStatus = new StatusModal();
        jhStatus.setStatus(myInspectUpload.getJh_hammer_chk());
        jhStatus.setTextView(binding.jhMarkTv);
        jhStatus.setTittleTv(binding.jhTittle);
        statusModalList.add(2, jhStatus);

        StatusModal specStatus = new StatusModal();
        specStatus.setStatus(myInspectUpload.getSpecies_chk());
        specStatus.setTextView(binding.speciesTv);
        specStatus.setTittleTv(binding.speciesTittle);
        statusModalList.add(3, specStatus);

        StatusModal diameterStatus = new StatusModal();
        diameterStatus.setStatus(myInspectUpload.getDiameter_chk());
        diameterStatus.setTextView(binding.diameterTv);
        diameterStatus.setTittleTv(binding.diameterTittle);
        statusModalList.add(4, diameterStatus);

        StatusModal lengthStatus = new StatusModal();
        lengthStatus.setStatus(myInspectUpload.getLength_chk());
        lengthStatus.setTextView(binding.lengthTv);
        lengthStatus.setTittleTv(binding.lengthTittle);
        statusModalList.add(5, lengthStatus);

//        binding.resultLL.setVisibility(View.VISIBLE);
//        binding.usernameTv.setText(myInspectUpload.getUser_name());
//        binding.dateTv.setText(myInspectUpload.getInspect_date_time());
//        binding.latTv.setText(myInspectUpload.getGps_lat().toString());
//        binding.longTv.setText(myInspectUpload.getGps_long().toString());
//        binding.remarksTv.setText(myInspectUpload.getRemarks());

        for (StatusModal statusModal : statusModalList){
            TextView textView = statusModal.getTextView();
//            TextView tittleTv = statusModal.getTittleTv();

            if (statusModal.getStatus().equals("P")){
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, pass, null);
//                tittleTv.setBackground(getResources().getDrawable(R.drawable.info_green));
            }else if (statusModal.getStatus().equals("F")){
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, fail, null);
//                tittleTv.setBackground(getResources().getDrawable(R.drawable.info_maroon));
            }else if (statusModal.getStatus().equals("N")){
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, unknown, null);
//                tittleTv.setBackground(getResources().getDrawable(R.drawable.info_grey));
            }
        }
    }

    private void openDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

//        View layout = inflater.inflate(R.layout.item_inspect_dialog,
//                (ViewGroup) findViewById(R.id.root));

        ItemInspectDialogBinding dialogBinding = ItemInspectDialogBinding.inflate(inflater);

//        SlideView lpiSlideView = layout.findViewById(R.id.lpi_slider);
//        SlideView pmSlideView = layout.findViewById(R.id.pm_slider);
//        SlideView jhSlideView = layout.findViewById(R.id.jh_slider);
//        SlideView specSlideView = layout.findViewById(R.id.spec_slider);
//        SlideView diameterSlideView = layout.findViewById(R.id.diameter_slider);
//        SlideView lengthSlideView = layout.findViewById(R.id.length_slider);

//        TextView lpiChkTv = layout.findViewById(R.id.lpi_chk_tv);
//        TextView pmChkTv = layout.findViewById(R.id.pm_chk_tv);
//        TextView jhChkTv = layout.findViewById(R.id.jh_mark_chk_tv);
//        TextView specChkTv = layout.findViewById(R.id.spec_chk_tv);
//        TextView diameterChkTv = layout.findViewById(R.id.diameter_chk_tv);
//        TextView lengthChkTv = layout.findViewById(R.id.length_chk_tv);
//
//        final EditText remarkEt = layout.findViewById(R.id.remark_et);
//        remarkEt.clearFocus();

        statusModalList = null;
        statusModalList = new ArrayList<>();

        StatusModal lpiStatus = new StatusModal();
        lpiStatus.setSlideView(dialogBinding.lpiSlider);
        lpiStatus.setTittleTv(binding.lpiTittle);
        lpiStatus.setTextView(binding.lpiTv);
        statusModalList.add(0, lpiStatus);

        StatusModal pmStatus = new StatusModal();
        pmStatus.setSlideView(dialogBinding.pmSlider);
        pmStatus.setTittleTv(binding.pmTittle);
        pmStatus.setTextView(binding.pmTv);
        statusModalList.add(1, pmStatus);

        StatusModal jhStatus = new StatusModal();
        jhStatus.setSlideView(dialogBinding.jhSlider);
        jhStatus.setTittleTv(binding.jhTittle);
        jhStatus.setTextView(binding.jhMarkTv);
        statusModalList.add(2, jhStatus);

        StatusModal specStatus = new StatusModal();
        specStatus.setSlideView(dialogBinding.specSlider);
        specStatus.setTittleTv(binding.speciesTittle);
        specStatus.setTextView(binding.speciesTv);
        statusModalList.add(3, specStatus);

        StatusModal diameterStatus = new StatusModal();
        diameterStatus.setSlideView(dialogBinding.diameterSlider);
        diameterStatus.setTittleTv(binding.diameterTittle);
        diameterStatus.setTextView(binding.diameterTv);
        statusModalList.add(4, diameterStatus);

        StatusModal lengthStatus = new StatusModal();
        lengthStatus.setSlideView(dialogBinding.lengthSlider);
        lengthStatus.setTittleTv(binding.lengthTittle);
        lengthStatus.setTextView(binding.lengthTv);
        statusModalList.add(5, lengthStatus);

        final TextView textView = dialogBinding.remarkEt;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this,
                android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setCancelable(false);
        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                remarks = textView.getText().toString();

                if(myInspectUploadList.size() <= 0){
                    saveIntoDB();
                }
                //refreshSearch();
                binding.resultLl.setVisibility(View.VISIBLE);

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        LogRegister logRegister = logRegisterList.get(0);

        dialogBinding.lpiChkTv.setText("LPI : "+ logRegister.getLpi_no());
        dialogBinding.pmChkTv.setText("Property Mark : "+ logRegister.getPro_mark_reg_no());
        dialogBinding.jhMarkChkTv.setText("JH Hammer Mark : "+ mobileDocList.get(0).getHammer_mark_no());
        dialogBinding.specChkTv.setText("Species : "+ logRegister.getSpecies_code());
        dialogBinding.diameterChkTv.setText("Diameter : "+ logRegister.getDiameter());
        dialogBinding.lengthChkTv.setText("Length : "+ logRegister.getLength());

        dialogBinding.lpiSlider.setOnSlideCompleteListener(this);
        dialogBinding.pmSlider.setOnSlideCompleteListener(this);
        dialogBinding.jhSlider.setOnSlideCompleteListener(this);
        dialogBinding.specSlider.setOnSlideCompleteListener(this);
        dialogBinding.diameterSlider.setOnSlideCompleteListener(this);
        dialogBinding.lengthSlider.setOnSlideCompleteListener(this);

        dialog.setView(dialogBinding.root);
        dialog.create();
        dialog.show();
    }


    public void saveIntoDB() {

        Log.e("save","db");
        dateTime= new DateTime();
        Pref pref = new Pref(getApplicationContext());
        String dateTimeStr = dateTime.getCurrentDateTime("yyyy-MM-dd HH:mm:ss");

        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();

        MyInspectUpload myInspectUpload = new MyInspectUpload();
        myInspectUpload.setLpi_chk(statusModalList.get(0).getStatus());
        myInspectUpload.setPro_mark_chk(statusModalList.get(1).getStatus());
        myInspectUpload.setJh_hammer_chk(statusModalList.get(2).getStatus());
        myInspectUpload.setSpecies_chk(statusModalList.get(3).getStatus());
        myInspectUpload.setDiameter_chk(statusModalList.get(4).getStatus());
        myInspectUpload.setLength_chk(statusModalList.get(5).getStatus());
        myInspectUpload.setGps_lat(latitude);
        myInspectUpload.setGps_long(longitude);
        myInspectUpload.setUser_login_id(pref.getLoginId());
        myInspectUpload.setUser_name(pref.getUserName());
        myInspectUpload.setRegis_id(logRegisterList.get(0).getRegis_id());
        myInspectUpload.setExch_det_id(logRegisterList.get(0).getExch_det_id());
        myInspectUpload.setInspect_date_time(dateTimeStr);
        myInspectUpload.setRemarks(remarks);

        myInspectUploadDao.insert(myInspectUpload);

//        longTv.setText(Double.toString(longitude));
//        latTv.setText(Double.toString(latitude));
//        usernameTv.setText(pref.getUserName());
//        dateTv.setText(dateTimeStr);

        getData(lpi, pm); //  refresh data

    }

//    public void updateDB(){
//        Log.e("update","db");
//        dateTime= new DateTime();
//        Pref pref = new Pref(getApplicationContext());
//
//        MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
//
//        MyInspectUpload myInspectUpload = myInspectUploadList.get(0);
//        myInspectUpload.setInspect_id(myInspectUploadList.get(0).getInspect_id());
//        myInspectUpload.setLpi_chk(statusModalList.get(0).getStatus());
//        myInspectUpload.setPro_mark_chk(statusModalList.get(1).getStatus());
//        myInspectUpload.setJh_hammer_chk(statusModalList.get(2).getStatus());
//        myInspectUpload.setSpecies_chk(statusModalList.get(3).getStatus());
//        myInspectUpload.setDiameter_chk(statusModalList.get(4).getStatus());
//        myInspectUpload.setLength_chk(statusModalList.get(5).getStatus());
//        myInspectUpload.setGps_lat(latitude);
//        myInspectUpload.setGps_long(longitude);
//        myInspectUpload.setUser_login_id(pref.getLoginId());
//        myInspectUpload.setUser_name(pref.getUserName());
//        myInspectUpload.setRegis_id(logRegisterList.get(0).getRegis_id());
//        myInspectUpload.setExch_det_id(logRegisterList.get(0).getExch_det_id());
//        myInspectUpload.setInspect_date_time(dateTime.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//
//        Log.e("date time", dateTime.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
//        myInspectUpload.setRemarks(remarks);
//
//        myInspectUploadDao.update(myInspectUpload);
//
//    }

//    private void showGPSEnableAlert() {
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Enable Location")
//                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
//                        "use this app")
//                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(myIntent);
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                    }
//                });
//        dialog.show();
//    }


    @Override
    public void onSlideComplete(SlideView slideView, boolean isPass) {

        for (int i = 0; i < statusModalList.size(); i++) {

            if (slideView == statusModalList.get(i).getSlideView()) {
                if (isPass) {

                    StatusModal tmp = new StatusModal();
                    tmp.setStatus("P");
                    tmp.setSlideView(statusModalList.get(i).getSlideView());
                    tmp.setTextView(statusModalList.get(i).getTextView());
                    statusModalList.set(i, tmp);

                    slideView.removeText();
                    slideView.setSlideBackgroundColor(R.drawable.slider_background_green);
                } else {

                    StatusModal tmp = new StatusModal();
                    tmp.setStatus("F");
                    tmp.setSlideView(statusModalList.get(i).getSlideView());
                    tmp.setTextView(statusModalList.get(i).getTextView());
                    statusModalList.set(i, tmp);

                    slideView.removeText2();
                    slideView.setSlideBackgroundColor(R.drawable.slider_background_red);
                }
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == Consts.REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.

                getLocation();

            } else {
                // Permission denied.

            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public void search(View v){

        refreshSearch();
    }

    private void refreshSearch(){
        lpi = lpiEt.getText().toString().toUpperCase();
        pm = pmEt.getText().toString().toUpperCase();

        lpiEt.clearFocus();
        pmEt.clearFocus();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lpiEt.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(pmEt.getWindowToken(), 0);

        binding.scrollView.post(new Runnable() {
            public void run() {
                binding.scrollView.fullScroll(binding.scrollView.FOCUS_UP);
            }
        });

        getData(lpi, pm);
    }

    public void qrscan(View view) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CustomScannerActivity.class);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();


//        scanDecode.starScan();
    }

    public void lpiClick(View v){

        setSlideViewData( Consts.LPI_CHK, "LPI : " +binding.lpiTv.getText().toString());

    }

    public void pmClick(View v){
        setSlideViewData( Consts.PRO_MARK_CHK, "Property Mark : " +binding.pmTv.getText().toString());

    }

    public void jhClick(View v){
        setSlideViewData(Consts.JH_HAMMER_CHK, "JH Hammer Mark : " + binding.jhMarkTv.getText().toString());

    }

    public void specClick(View v){
        setSlideViewData(Consts.SPECIES_CHK, "Species : " + binding.speciesTv.getText().toString());

    }

    public void diameterClick(View v){
        setSlideViewData(Consts.DIAMETER_CHK, "Diameter : "+binding.diameterTv.getText().toString());

    }

    public void lengthClick(View v){
        setSlideViewData(Consts.LENGTH_CHK, "Length : " + binding.lengthTv.getText().toString());

    }

    private String remarks;
    public void remarksClick(View v){

        if(myInspectUploadList.size()>0){
            final MyInspectUpload myInspectUpload = myInspectUploadList.get(0);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.item_edittext_dialog, null);
            final EditText editText = layout.findViewById(R.id.edit_text);
            if(myInspectUpload.getRemarks() != null){
                editText.setText(myInspectUpload.getRemarks());
            }

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setCancelable(false);
            dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
                    myInspectUpload.setRemarks(editText.getText().toString());
                    myInspectUploadDao.update(myInspectUpload);
                    binding.remarksTv.setText(editText.getText());

                }
            });

            dialog.setView(layout);
            dialog.create();
            dialog.show();
        }

    }

    private String slideResult;

    private void setSlideViewData(final String type, String text){

        slideResult = "N";

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                MyInspectUpload myInspectUpload;

                if (myInspectUploadList.size()>0){// record found
                    myInspectUpload = myInspectUploadList.get(0);
                }else {
                    myInspectUpload = new MyInspectUpload();
                }

                if (type.equals(Consts.LPI_CHK)){
                    myInspectUpload.setLpi_chk(slideResult);
                    setResultView(binding.lpiTittle, binding.lpiTv, slideResult);

                } else if (type.equals(Consts.DIAMETER_CHK)){
                    myInspectUpload.setDiameter_chk(slideResult);
                    setResultView(binding.diameterTittle, binding.diameterTv, slideResult);
                }
                else if (type.equals(Consts.JH_HAMMER_CHK)){
                    myInspectUpload.setJh_hammer_chk(slideResult);
                    setResultView(binding.jhTittle, binding.jhMarkTv, slideResult);
                }
                else if (type.equals(Consts.LENGTH_CHK)){
                    myInspectUpload.setLength_chk(slideResult);
                    setResultView(binding.lengthTittle, binding.lengthTv, slideResult);
                }
                else if (type.equals(Consts.PRO_MARK_CHK)){
                    myInspectUpload.setPro_mark_chk(slideResult);
                    setResultView(binding.pmTittle, binding.pmTv, slideResult);
                }
                else if (type.equals(Consts.SPECIES_CHK)){
                    myInspectUpload.setSpecies_chk(slideResult);
                    setResultView(binding.speciesTittle, binding.speciesTv, slideResult);
                }

                Pref pref = new Pref(getApplicationContext());
                dateTime =  new DateTime();

                myInspectUpload.setGps_lat(latitude);
                myInspectUpload.setGps_long(longitude);
                myInspectUpload.setUser_login_id(pref.getLoginId());
                myInspectUpload.setUser_name(pref.getUserName());
                myInspectUpload.setInspect_date_time(dateTime.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));

                MyInspectUploadDao myInspectUploadDao = daoSession.getMyInspectUploadDao();
                myInspectUploadDao.update(myInspectUpload);
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        ItemSlideviewBinding singleSlideBinding = ItemSlideviewBinding.inflate(inflater);


//        //todo
//        View layout = inflater.inflate(R.layout.item_slideview,
//                (ViewGroup) findViewById(R.id.root));
//
//
//
//        TextView textView = layout.findViewById(R.id.slideview_tv);

        singleSlideBinding.slideviewTv.setText(text);

        //SlideView slideView = layout.findViewById(R.id.slider_view);
        singleSlideBinding.sliderView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView, boolean status) {
                if (status){
                    slideResult = "P";
                    slideView.removeText();
                    slideView.setSlideBackgroundColor(R.drawable.slider_background_green);

                }else {
                    slideResult = "F";
                    slideView.removeText2();
                    slideView.setSlideBackgroundColor(R.drawable.slider_background_red);
                }
            }
        });

        dialog.setView(singleSlideBinding.root);
        dialog.create();
        dialog.show();

    }

    public void setResultView(TextView tittle, TextView v, String results){

        if (results != null){
            if (results.equals("P")){
                v.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, pass, null);
//                tittle.setBackground(getResources().getDrawable(R.drawable.info_green));
            }else if (results.equals("F")){
                v.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, fail, null);
//                tittle.setBackground(getResources().getDrawable(R.drawable.info_maroon));
            }else {
                v.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, unknown, null);
//                tittle.setBackground(getResources().getDrawable(R.drawable.info_grey));
            }

        }

    }

}
