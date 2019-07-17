package com.example.utsav.bhojpuri;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendGrievance extends Fragment {
private TeacherDB td;
private GrievanceDB grv;
    List<String> category=new ArrayList<>();
    List<Integer>qc_id=new ArrayList<>();
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Spinner spinner;
    int c_id;
    String c_type;
    AlertDialog.Builder builder1,builder2;
    ImageButton imageButton;
    private static final int CAMERA_REQUEST = 1888;
    String myBase64Image="imbase64";
    Button submit,retake;
    LinearLayout retakelayout,imagelayout;
    EditText remark;
    int school_id=0,submit_by=0;
    String submit_on="";
    Cursor cursor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.sendgrievance, container, false);
        td=new TeacherDB(getContext());
        grv=new GrievanceDB(getContext());
          spinner=view.findViewById(R.id.spinner);
          submit=view.findViewById(R.id.submitresp);
        final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);
          imageButton=view.findViewById(R.id.imagebuttonx);
          retakelayout=view.findViewById(R.id.retakelayout);
        imagelayout=view.findViewById(R.id.imagelayout);
       retake=view.findViewById(R.id.retake);
       remark=view.findViewById(R.id.remark);
        Cursor cr=td.getSchool_details();
        cr.moveToFirst();
        school_id=cr.getInt(7);
        Cursor cr1=td.getAllData3();
        cr1.moveToFirst();
        submit_by=cr1.getInt(3);
        final SimpleDateFormat datet = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        submit_on=datet.format(new Date());
        builder1 = new AlertDialog.Builder(getContext());
        builder2 = new AlertDialog.Builder(getContext());
              refresh();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c_id = qc_id.get(position);
                c_type=category.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });
imageButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }
});
submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

            if (c_id > 0) {
                grv.insertdata(school_id, c_id, c_type, myBase64Image, submit_by, submit_on, remark.getText().toString(),2);
                builder1.setTitle("Preliminary Grievance Done");
                builder1.setMessage("Please sync to complete the process");
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                refresh();
                                pager.setCurrentItem(1);
                                // startActivity(new Intent(getContext(),BlockTab1Fragment.class));
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else {
                builder2.setMessage("Please select a Grievance type");
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


}


});
        return view;
    }
public void refresh(){
    remark.setText(null);
    Cursor cursor=td.category_data();
    qc_id.clear();
    category.clear();
    qc_id.add(0);
    category.add("Select Type");
    while (cursor.moveToNext()){
        category.add(cursor.getString(1));
        qc_id.add(cursor.getInt(0));
    }
    final ArrayAdapter<String> adapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,category.toArray() );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            /*case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;*/
            case CAMERA_REQUEST:
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Bitmap converetdImage = getResizedBitmap(photo, 500);
                    ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageview);
                    imageView.setImageBitmap(photo);
                    imagelayout.setVisibility(View.GONE);
                    retakelayout.setVisibility(View.VISIBLE);

                    myBase64Image = encodeToBase64(converetdImage, Bitmap.CompressFormat.JPEG, 100);
                }
                break;
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width+80+80, height+10, true);
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }
}
