package my.com.sains.teams.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.senter.support.openapi.StBarcodeScanner;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import my.com.sains.teams.R;

public class SdkBarcodeScanActivity extends Activity {
    public static final String Tag = "MainActivity";
    AtomicBoolean isScanning = new AtomicBoolean(false);
    private TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        tView = (TextView) findViewById(R.id.idMainActivity_tvScan);

        //scan button on ui
        Button btnButton = (Button) findViewById(R.id.idMainActivity_btnScan);
        btnButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
    }

    /**
     * response to physical button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 235://ST327
            case 272://ST327A
            case 212://ST307
            case 221://ST308 ST309
            case 224://908
                scan();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * trig a scan action,show the result on textview,which is at the top of activity.
     */
    private void scan() {

        new Thread() {
            public void run() {
                if (isScanning.compareAndSet(false, true) == false) {//at the same time only one thread can be allowed to scan
                    return;
                }

                try {
                    StBarcodeScanner scanner = StBarcodeScanner.getInstance();
                    if (scanner == null) {
                        Log.e(Tag, "!!!!!!!!!!!!sdk is to old to work，please update sdk");
                        return;
                    }
                    StBarcodeScanner.BarcodeInfo rslt = scanner.scanBarcodeInfo();//scan ,if failed,null will be return
                    
                    final AtomicReference<String> show = new AtomicReference<String>("no barcode scanned");
                    if (rslt!=null){
                        show.set("Utf8: "+new String(rslt.getBarcodeValueAsBytes(),"utf-8")+"\r\n"+
                                "GBK:"+new String(rslt.getBarcodeValueAsBytes(),"GBK")+"\r\n");
                    }
                    
                    
                    
                    Log.e("barcodeDemo", "scan result:" + show);

                    //update ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tView.setText(show.get());
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

            ;
        }.start();
    }
}