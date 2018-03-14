package my.com.sains.teams.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.scandecode.ScanDecode;
import com.scandecode.inf.ScanInterface;
import com.senter.support.openapi.StBarcodeScanner;
import com.senter.support.openapi.StKeyManager;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by User on 30/1/2018.
 */

public class BarcodeScanner {

    //speedData = 5.0 inch, saat = 7.0 inch

    private Activity activity;
    private StKeyManager.ShortcutKeyMonitor  f2KeyMonitor;
    private Handler handler;
    private AtomicBoolean isScanning;
    private ScanInterface scanDecode;

    public BarcodeScanner(Activity activity){
        this.activity = activity;

    }

    private boolean isSpeedDataScanner(){

        if (Consts.SPEEDATA_SCANNER_MODEL.equals(Build.MODEL)){

            return true;

        }else {

            return false;
        }

    }

    private boolean isSaatScanner(){

        if (Consts.SAAT_SCANNER_MODEL.equals(Build.MODEL)){
            return true;
        }else {
            return false;
        }
    }


    public void initScanner(){

        if (isSaatScanner()){
            //when f2 button is pressed

            f2KeyMonitor = StKeyManager.ShortcutKeyMonitor.isShortcutKeyAvailable(StKeyManager.ShortcutKey.F2) ?
                    StKeyManager.getInstanceOfShortcutKeyMonitor(StKeyManager.ShortcutKey.F2):null;
            isScanning = new AtomicBoolean(false);
            HandlerThread handlerThread=new HandlerThread("");
            handlerThread.start();
            handler=new Handler(handlerThread.getLooper());
            onUiCbF2(true);

        }else if (isSpeedDataScanner()){

            scanDecode = new ScanDecode(activity);
            scanDecode.initService("true");

            scanDecode.getBarCode(new ScanInterface.OnScanListener() {
                @Override
                public void getBarcode(String s) {
                    onBarcodeScan.onBarcodeCallback(s);
                }
            });
        }

    }

    StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener f2KeyListener=new StKeyManager.ShortcutKeyMonitor.ShortcutKeyListener(){
        @Override
        public void onKeyDown(int keycode, int repeatCount, StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event) {
            //at.showToastShort("F1:Down repeatCount:"+repeatCount);
            scan();
        }

        @Override
        public void onKeyUp(int keycode, int repeatCount, StKeyManager.ShortcutKeyMonitor.ShortcutKeyEvent event) {
            //at.showToastShort("F1:Up repeatCount:"+repeatCount);
        }

    };

    // F2 key pressed
    // to listen to the hardware press
    protected void onUiCbF2(boolean isChecked){
        if (isChecked) {
            f2KeyMonitor.reset(activity, f2KeyListener, handler);
            f2KeyMonitor.startMonitor();
        }else {
            f2KeyMonitor.stopMonitor();
        }
    }

    //scan barcode (saat 7 inch)
    private void scan() {

        new Thread() {
            public void run() {
                if (isScanning.compareAndSet(false, true) == false) {//at the same time only one thread can be allowed to scan
                    return;
                }

                try {
                    StBarcodeScanner scanner = StBarcodeScanner.getInstance();
                    if (scanner == null) {
                        Log.e("sdk", "!!!!!!!!!!!!sdk is to old to work，please update sdk");
                        return;
                    }
                    StBarcodeScanner.BarcodeInfo rslt = scanner.scanBarcodeInfo();//scan ,if failed,null will be return

                    final AtomicReference<String> show = new AtomicReference<String>("no barcode scanned");
                    if (rslt!=null){
                        show.set(new String(rslt.getBarcodeValueAsBytes(),"utf-8"));
                    }else {
                        show.set("No result");
                    }

                    Log.e("barcodeDemo", "scan result:" + show);

                    //update ui
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tView.setText(show.get());
                            Log.e("return", show.get());
                            //String[] pieces = show.get().split(",");
                            onBarcodeScan.onBarcodeCallback(show.get());
                        }
                    });
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } finally {
                    isScanning.set(false);
                }
            }

        }.start();
    }

    public void destroyBarcode(){

        if (isSpeedDataScanner()){
            if (scanDecode != null)
                scanDecode.onDestroy();
        }
    }

    public OnBarcodeScan onBarcodeScan;

    public void setOnBarcodeScan(OnBarcodeScan barcodeListener){
        this.onBarcodeScan = barcodeListener;
    }

    public interface OnBarcodeScan{
        void onBarcodeCallback(String decodedString);
    }
}
