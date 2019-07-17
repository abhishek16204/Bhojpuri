package com.example.utsav.bhojpuri;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

/*import com.google.android.gms.vision.CameraSource;*/
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.regex.Pattern;

public class ScannerActivity extends AppCompatActivity {
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private Camera camera;
    private SurfaceView cameraView;
    private TextView barcodeInfo;
    AlertDialog.Builder builder1;
    SurfaceHolder holder;
    String s1;
    ScannerDB db;
    int count=0;
    private int mCameraId = -1;
    int maxZoom;
    String selected_position1;
    int currentZoomLevel=0;
    float mDist = 0;
    public final Pattern u_id = Pattern.compile(
            "[0-9]{1,256}"
    );
    public final Pattern optn_id = Pattern.compile(
            " [a-zA-Z]+"
    );
    private ScaleGestureDetector scaleGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        db= new ScannerDB(ScannerActivity.this);
        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeInfo = (TextView)findViewById(R.id.code_info);
        builder1=new AlertDialog.Builder(ScannerActivity.this);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        Intent intent=getIntent();
        selected_position1=intent.getStringExtra("qid");
        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int width = displayMetrics.widthPixels;
        final int height = displayMetrics.heightPixels;
        cameraSource = new CameraSource
                .Builder(ScannerActivity.this, barcodeDetector)
                .setRequestedFps(60.0f)
                //.setRequestedPreviewSize(1920,1600)
                .setRequestedPreviewSize(height,width)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                // .setAutoFocusEnabled(true)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .build();

        Snackbar.make(findViewById(android.R.id.content), "Pinch/Stretch to zoom",
                Snackbar.LENGTH_LONG)
                .show();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @SuppressLint("MissingPermission")
            @Override
            public void surfaceChanged(final SurfaceHolder holder, int format, final int width, final int height) {

            }
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                s1=barcodes.valueAt(0).displayValue;
                String[]words=s1.split("-");
                Boolean codeqr =db.codeqr(selected_position1,words[0]);
                if (check(words[0])) {
                    if (codeqr == true) {

                     //   db.insert(selected_position1, barcodes.valueAt(0).displayValue);
                        db.insert1(selected_position1, words[0], words[1]);
                         db.dummy(selected_position1, words[0], words[1]);

                    }
                    if (barcodes.size() != 0) {
                        barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                            public void run() {
                                barcodeInfo.setText(    // Update the TextView
                                        barcodes.valueAt(0).displayValue
                                );
                            }
                        });
                    }
                    // barcodeInfo.setText("ScanCompleted");

                }
                else{
                    if (barcodes.size() != 0) {
                        barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                            public void run() {
                                barcodeInfo.setText(
                                        "Not a valid QR"
                                );
                            }
                        });
                    }

                }

            }

        });

        findViewById(R.id.btt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, ScannerList.class);
                intent.putExtra("qid", selected_position1);
                startActivity(intent);
                barcodeInfo.setText("Nothing to read.");
            }
        });

    }
    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            cameraSource.doZoom(detector.getScaleFactor());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean b = scaleGestureDetector.onTouchEvent(e);
        return b || super.onTouchEvent(e);
    }
    private boolean check(String userid) {
        return u_id.matcher(userid).matches();
    }
    private boolean checkopt(String optid) {
        return optn_id.matcher(optid).matches();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }
}
