package com.example.utsav.bhojpuri;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;

public class CRPDash extends AppCompatActivity {
    LinearLayout atd,mdm,grv,lv,assess,hlp,not,fb,l2;
    private BottomNavigationView mBottomNavigationView;
    AlertDialog.Builder builder1,builder2;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cluster_dash);
        l2=findViewById(R.id.l2);

        atd=findViewById(R.id.m1);
        mdm=findViewById(R.id.m2);
        grv=findViewById(R.id.m3);
        lv=findViewById(R.id.m4);
        assess=findViewById(R.id.m5);
        hlp=findViewById(R.id.m6);
        not=findViewById(R.id.m7);
        fb=findViewById(R.id.m8);
        mBottomNavigationView = findViewById(R.id.navigation);
        builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(false);
        builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(false);
        mBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(mBottomNavigationView);
        assess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CRPDash.this,B_Survey.class));
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(CRPDash.this,UserProfileCluster.class));
                return true;
            case R.id.helpline:

                return true;
            case R.id.reset:
                Boolean isconnected = ConnectivityReceiver.isConnected();
                if(isconnected) {
                    builder1.setTitle("Logout!");
                    builder1.setMessage("Do you want to logout?");

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // startActivity(new Intent(getContext(),BlockTabbedteacher.class));
                                    dialog.cancel();
                                    deleteCache(CRPDash.this);
                                    deleteDatabase(CRPDash.this);
                                    Splash_Screen.savePreferences("userlogin","6");
                                    deleteCache(CRPDash.this);
                                    Intent intent = new Intent(CRPDash.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    File sharedPreferenceFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
                                    File[] listFiles = sharedPreferenceFile.listFiles();
                                    for (File file : listFiles) {
                                        file.delete();
                                    }
                                    startActivity(intent);
                                }

                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else{
                    builder2.setTitle("No Internet Connection");
                    //    builder1.setMessage("");

                    builder2.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // startActivity(new Intent(getContext(),BlockTabbedteacher.class));
                                    dialog.cancel();


                                }

                            });
                    AlertDialog alert12 = builder2.create();
                    alert12.show();

                }
                return true;
            case R.id.sclm:

                return true;
            case R.id.version:

                return true;
            default:
                return false;

        }
    };

    public void deleteDatabase(Context context) {
        try {
            String destPath = getFilesDir().getPath();
            destPath = destPath.substring(0, destPath.lastIndexOf("/")) + "/databases";
            File dir = context.getDatabasePath(destPath);
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
