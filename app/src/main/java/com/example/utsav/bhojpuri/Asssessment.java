package com.example.utsav.bhojpuri;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class Asssessment extends Fragment {
    TextView qtn, a, b, c, d;
    Button scan, next, prev,sub;
    Random random;

    int i,j;
    ScannerDB sdb;
    TeacherDB td;
    SubmitScanDB ssdb;
    int que_id,st_id;
    String resp;
    Cursor ques;
    List<String> question_list=new ArrayList<>();
    List<Integer> question_id=new ArrayList<>();
    List<Integer> optn_id=new ArrayList<>();
    List<String> option_list = new ArrayList<String>();
    List<String> counter=new ArrayList<>();
    int user_id;
    String date;
    Cursor res,dummy;
    List<Integer> q_id=new ArrayList<>();
    List<Integer> s_id=new ArrayList<>();
    List<String> response = new ArrayList<String>();
    AlertDialog.Builder builder1,builder2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assessment, container, false);
        qtn = (TextView) view.findViewById(R.id.question);
        a = (TextView) view.findViewById(R.id.opt1);
        b = (TextView) view.findViewById(R.id.opt2);
        c = (TextView) view.findViewById(R.id.opt3);
        d = (TextView) view.findViewById(R.id.opt4);
        scan = (Button) view.findViewById(R.id.scan);
        next = (Button) view.findViewById(R.id.next);
        prev = (Button) view.findViewById(R.id.prev);
        sub = (Button) view.findViewById(R.id.submit);
        builder1 = new AlertDialog.Builder(getContext());
        builder2 = new AlertDialog.Builder(getContext());

        final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);
        random = new Random();
        td=new TeacherDB(getContext());
        sdb= new ScannerDB(getContext());
        ssdb= new SubmitScanDB(getContext());

        Cursor cursor=td.getAllData3();
        cursor.moveToFirst();
        user_id=cursor.getInt(3);
        SimpleDateFormat datet = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        date=datet.format(new Date());
        q_id.clear();
        s_id.clear();
        response.clear();
        option_list.clear();counter.clear();
        ques=td.getQuestion();
        question_list.clear();
        question_id.clear();
     /*   question_list.add("emptty");
        question_id.add(0);*/
        optn_id.clear();
      /*  option_list.add("A");
        optn_id.add(2);*/
        while (ques.moveToNext()) {
            question_list.add(ques.getString(1)+"."+ques.getString(2));
            question_id.add(ques.getInt(0));

        }

        for(int i=0;i<question_list.size();i++){
            Cursor option=td.getOption();
            //priceList.clear();
            int g=0;
            while (option.moveToNext()) {
                if((question_id.get(i))==option.getInt(0)){
                    option_list.add( option.getString(3)+"."+option.getString(2));
                    optn_id.add(option.getInt(1));
                }

            }
        }
        qtn.setText(question_list.get(0));
        a.setText(option_list.get(0));
        b.setText( option_list.get(1));
        c.setText( option_list.get(2));
        d.setText(option_list.get(3));

             i=1;
             j=4;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* i=i+1;
                j=j+4;*/
                dummy=sdb.scanned_single_data();
                dummy.moveToFirst();
               if (dummy!=null&&dummy.getCount()>0) {
                   if (i < question_list.size()) {

                       qtn.setText(question_list.get(i));
                       a.setText(option_list.get(j));
                       b.setText(option_list.get(j + 1));
                       c.setText(option_list.get(j + 2));
                       d.setText(option_list.get(j + 3));

                       prev.setVisibility(View.VISIBLE);
                       i = i + 1;
                       j = j + 4;
                       sdb.deleteData("dummy");
                       if (i >= question_list.size()) {
                           next.setVisibility(View.INVISIBLE);
                           sub.setVisibility(View.VISIBLE);
                       /* i = i - 1;
                        j=j-4;*/
                       }
                   } else {
                       next.setVisibility(View.INVISIBLE);
                   /* i = i - 1;
                    j=j-4;*/
                   }
               }
               else {
                   builder2.setMessage("Please Scan QR values before going to next Question");
                   //builder1.setMessage("Connect tO N");
                   builder2.setCancelable(false);
                   builder2.setPositiveButton(
                           "ok",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   // startActivity(new Intent(view.getContext(),BlockTabbedteacher.class));
                                   dialog.cancel();
                               }
                           });
                   AlertDialog alert11 = builder2.create();
                   alert11.show();
                  // Toast.makeText(getContext(),"Please Scan answers" , Toast.LENGTH_SHORT).show();
               }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=i-1;
                j=j-4;
                if (i < question_list.size()) {
                    if (i!=0) {
                        qtn.setText(question_list.get(i - 1));
                        a.setText(option_list.get(j - 4));
                        b.setText(option_list.get(j - 3));
                        c.setText(option_list.get(j - 2));
                        d.setText(option_list.get(j - 1));
                   /* i = i - 1;
                    j=j-4;*/
                        next.setVisibility(View.VISIBLE);
                        sub.setVisibility(View.INVISIBLE);
                    }
                    else {
                        prev.setVisibility(View.INVISIBLE);
                        i=i+1;
                        j=j+4;
                    }

                }
               /* if (i == 0) {

                }*/

            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dummy=sdb.scanned_single_data();
                dummy.moveToFirst();
                if (dummy != null && dummy.getCount() > 0) {
                    res = sdb.scanned_data();
                    while (res.moveToNext()) {
                          /*question_list.add(res.getString(1)+"."+res.getString(2));
                          question_id.add(ques.getInt(0));*/
                        q_id.add(res.getInt(0));
                        s_id.add(res.getInt(1));
                        response.add(res.getString(2));

                    }
                    ssdb.insert_table1(user_id, date, 2);
                    Cursor cursor1 = ssdb.getAllData3();
                    cursor1.moveToLast();
                    int assessment_id = cursor1.getInt(0);
                    for (int i = 0; i < q_id.size(); i++) {
                        que_id = q_id.get(i);
                        st_id = s_id.get(i);
                        resp = response.get(i);

                        ssdb.insert_table2(assessment_id, que_id, st_id, resp);
                    }

                    builder1.setTitle("Preliminary Assessment Done");
                    builder1.setMessage("Please sync to complete the process");
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    pager.setCurrentItem(1);
                                    sdb.deleteData("dummy");
                                    // startActivity(new Intent(getContext(),BlockTab1Fragment.class));
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else{
                    builder2.setMessage("Please Scan QR values before Submitting");
                    //builder1.setMessage("Connect tO N");
                    builder2.setCancelable(false);
                    builder2.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // startActivity(new Intent(view.getContext(),BlockTabbedteacher.class));
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder2.create();
                    alert11.show();
                }
                  //  Toast.makeText(getContext(),"Please Scan answers" , Toast.LENGTH_SHORT).show();
            }
        });

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            callToRun();
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    100);
        } else
            callToRun();

        return view;
    }
    @TargetApi(Build.VERSION_CODES.M)

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.e("value", "Permission Granted,Now you can use local Drive");
                else
                    Log.e("value", "Permissions Denied,You cannot use local Drive");
                break;

        }
    }

    private void callToRun() {
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String position = String.valueOf(i);
                Intent intent = new Intent(getContext(), ScannerActivity.class);
                intent.putExtra("qid", position);
                startActivity(intent);
            }
        });
    }

}
