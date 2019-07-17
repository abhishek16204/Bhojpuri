package com.example.utsav.bhojpuri;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BaselineSurvey extends Fragment implements AdapterCallback {
    TextView qtn, a, b, c, d;
    List<QuestionList> q_list;
    List<String> question_list = new ArrayList<>();
    List<Integer> question_id = new ArrayList<>();
    List<Integer> opt_id = new ArrayList<>();
    List<String> option_list = new ArrayList<>();
    List<Integer> option_id = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    List<Integer> input_type_id = new ArrayList<>();
    List<Integer> isImage = new ArrayList<>();
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    Spinner spinner, spinner2;
    TextView spinner1;
    private ClusterDB sp;
    String cluster_list;
    int cluster_id_list;

    List<String> school_list = new ArrayList<>();
    List<Integer> school_id_list = new ArrayList<>();

    ImageView imageView;
    AdapterCallback callback;
    Bitmap bitmap;
    //BaselineSurveyAdapter baselineSurveyAdapter;

    SurveyAdapterS surveyAdapterS;
    int selectedposition;
    String myBase64Image1 = "imbase64";
    int permission_req_code = 105;
    ClusterDB crp;

    int selectedCluster, selectedSchool;

    List<QuestionList> cluster_images;
    int k = 0;

    Button sub;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!defaultPermissionCheck()) {
            askForPermission();
        }
        View view = inflater.inflate(R.layout.fragment_baseline_survey, container, false);

        final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);
        qtn = view.findViewById(R.id.textView);

        callback = this;

        crp = new ClusterDB(view.getContext());
        sp = new ClusterDB(view.getContext());
        Cursor ques = crp.getQuestion();
        Cursor opt = crp.getOption();
        q_list = new ArrayList<>();
        cluster_images = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        imageView = view.findViewById(R.id.imageView);


        ques.moveToNext();
        for (int i = 0; i < ques.getCount(); i++) {
            QuestionList item = new QuestionList();
            item.setQuestion(ques.getString(4) + ". " + ques.getString(3));
            item.setQuestion_id(ques.getInt(6));
            q_list.add(item);
            question_id.add(ques.getInt(6));
            input_type_id.add(ques.getInt(0));
            isImage.add(ques.getInt(2));
            ques.moveToNext();
        }


        Cursor cursorCluster = sp.getCluster();

        cluster_id_list = 0;
        cluster_list =null;
        while (cursorCluster.moveToNext()) {
            cluster_list = cursorCluster.getString(0);
            cluster_id_list = Integer.parseInt(cursorCluster.getString(2));
        }


        spinner1 = view.findViewById(R.id.spinner1);
        spinner1.setText(cluster_list);
        spinner2 = view.findViewById(R.id.spinner2);

        selectedCluster = cluster_id_list;
        setSchoolNames(cluster_id_list);




        return view;

    }

    private void setSchoolNames(int selectedCluster) {
        school_list.clear();
        school_id_list.clear();
        Cursor cursorSchool = sp.getSchools(cluster_id_list);
        school_list.add("select school");
        school_id_list.add(0);
        while (cursorSchool.moveToNext()) {
            school_list.add(cursorSchool.getString(4));
            school_id_list.add(Integer.parseInt(cursorSchool.getString(7)));
        }

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedSchool = school_id_list.get(position);
                Toast.makeText(getContext(), "school selected is " + position, Toast.LENGTH_SHORT).show();

                if (position != 0) {
                    recyclerView.setVisibility(View.VISIBLE);

                    surveyAdapterS = new SurveyAdapterS(getContext(), q_list, input_type_id, isImage, cluster_images, callback, selectedSchool, selectedCluster);
                    recyclerView.setAdapter(surveyAdapterS);

                }

                if (position == 0) {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter2 = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, school_list.toArray());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

    }


    @Override
    public void onMethodCallback(int position) {
        selectedposition = position;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 105);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void askForPermission() {

        //asking  for storage permission from user at runtime

        requestPermissions(new String[]{
                Manifest.permission.CAMERA
        }, permission_req_code);

    /*requestPermissions(getActivity(),new String[] {
            Manifest.permission.CAMERA
    },permission_req_code);*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //checking if user granted the permissions or not
        Log.e("dfdad", "onRequestPermissionsResult: ");
        if (requestCode == permission_req_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("fdfas", "granted");
                Toast.makeText(getContext(), "Permission granted :)", Toast.LENGTH_LONG).show();
            } else {
                Log.e("fdfas", " not granted");
                Toast.makeText(getContext(), "App will not work without permissions, Grant these permissions from settings. :|", Toast.LENGTH_LONG).show();
                askForPermission();
            }
        }
    }

    private boolean defaultPermissionCheck() {
        //checking if permissions is already granted
        int external_storage_write = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        return external_storage_write == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//          Toast.makeText(getContext(), "inside onActivity", Toast.LENGTH_SHORT).show();

        if (requestCode == 105) {
            if (resultCode == Activity.RESULT_OK) {
                bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    Log.e("Null pointer", "aa gya");
                    myBase64Image1 = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogview = inflater.inflate(R.layout.dialogdisplay, null);
                    ImageView image = dialogview.findViewById(R.id.immmm);
                    image.setImageBitmap(bitmap);

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("Image is selected").setMessage("Are you sure you want to submit the image")
                            .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getContext(), "Image aa gyi", Toast.LENGTH_SHORT).show();
                                    View viewItem = recyclerView.getLayoutManager().findViewByPosition(selectedposition);
                                    ImageButton icon = viewItem.findViewById(R.id.camera_image_list_question);
                                    icon.setEnabled(false);
                                    new SurveyAdapterS(selectedposition + 1, myBase64Image1);
                                    viewItem.findViewById(R.id.camera_alert_list_question).setVisibility(View.GONE);
                                    Button retake = viewItem.findViewById(R.id.retake_image_list_question);
                                    retake.setVisibility(View.VISIBLE);
                                    retake.setText("RETAKE");
                                    retake.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), 105);
                                        }
                                    });
                                    icon.setImageBitmap(bitmap);


                                }
                            }).setNegativeButton("Retake", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), 105);
                                }
                            }).setView(dialogview).setCancelable(false).show();


                }
                //  Toast.makeText(getContext(), "HEllo inside", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
