package com.example.utsav.bhojpuri;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.utsav.bhojpuri.SendGrievance.encodeToBase64;

public class LeaveRequest extends Fragment {
    private TeacherDB td;
    private LeaveDB lev;
    List<String> leave_type=new ArrayList<>();
    List<Integer>leave_type_id=new ArrayList<>();
    ArrayAdapter<String> adapter;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    int l_id;
    String l_type;
    EditText et ,et1;
    Calendar nCuurentDate;
    int day,month,year;
    Spinner spinner;
    AlertDialog.Builder builder1,builder2;
    ImageButton imageButton;
    private static final int CAMERA_REQUEST = 1888;
    String myBase64Image="imbase64";
    Button submit,retake;
    EditText remark,from_date,to_date;
    LinearLayout retakelayout,imagelayout;
    int school_id=0,submit_by=0,role=0;
    String submit_on="";
    Cursor cursor;
    HttpResponse response;
    String message, status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave_request, container, false);
        td=new TeacherDB(getContext());
        lev = new LeaveDB(getContext());
        spinner=view.findViewById(R.id.spinner);
        submit=view.findViewById(R.id.submitresp);
        final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);

        imageButton=view.findViewById(R.id.imagebuttonx);
        retakelayout=view.findViewById(R.id.retakelayout);
        imagelayout=view.findViewById(R.id.imagelayout);
        retake=view.findViewById(R.id.retake);
        remark=view.findViewById(R.id.remark);
        from_date=view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);
        Cursor cr=td.getSchool_details();
        cr.moveToFirst();
        school_id=cr.getInt(7);
        Cursor cr1=td.getAllData3();
        cr1.moveToFirst();
        submit_by=cr1.getInt(3);
        role =cr1.getInt(0);
        final SimpleDateFormat datet = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        submit_on=datet.format(new Date());
        builder1 = new AlertDialog.Builder(getContext());
        builder2 = new AlertDialog.Builder(getContext());

        
        et  = (EditText)view.findViewById(R.id.from_date);
        et1 = (EditText)view.findViewById(R.id.to_date) ;
        nCuurentDate = Calendar.getInstance();
        day = nCuurentDate.get(Calendar.DAY_OF_MONTH);
        month = nCuurentDate.get(Calendar.MONTH);
        year = nCuurentDate.get(Calendar.YEAR);
        et.setFocusable(false);
        et1.setFocusable(false);
        et.setText("");
        et1.setText("");
       refresh();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                l_id = leave_type_id.get(position);
                l_type=leave_type.get(position);
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
        
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        monthOfYear = monthOfYear+1;
                        et.setText(year+"-"+monthOfYear+"-"+dayOfMonth);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear+1;
                        et1.setText(year+"-"+monthOfYear+"-"+dayOfMonth);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpClient httpClient = new DefaultHttpClient();
                Boolean isconnected = ConnectivityReceiver.isConnected();
                HttpPost httpPost = new HttpPost("http://bhojpur.empoweru.in/leave_request/");

                if (isconnected) {
                    if (et.getText().toString().length() != 0 && et1.getText().toString().length() != 0 && l_id != 0) {

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(9);
                    nameValuePair.add(new BasicNameValuePair("userid", String.valueOf(submit_by)));
                    nameValuePair.add(new BasicNameValuePair("roleid", String.valueOf(role)));
                    nameValuePair.add(new BasicNameValuePair("imagedd", myBase64Image));
                    nameValuePair.add(new BasicNameValuePair("school", String.valueOf(school_id)));
                    nameValuePair.add(new BasicNameValuePair("levid", String.valueOf(l_id)));
                    nameValuePair.add(new BasicNameValuePair("le_reason", remark.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("fdate", from_date.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("tdate", to_date.getText().toString()));
                    nameValuePair.add(new BasicNameValuePair("timestamp", submit_on));

                    try {
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    } catch (UnsupportedEncodingException e) {
                        // writing error to Log
                        e.printStackTrace();
                    }
                    try {
                        response = httpClient.execute(httpPost);
                        String responseBody = EntityUtils.toString(response.getEntity());
                        JSONObject obj = new JSONObject(responseBody);
                        message = obj.getString("message");
                        status = obj.getString("status");
                        // Toast.makeText(view.getContext(),"ok",Toast.LENGTH_LONG ).show();
                        Log.d("Http Response:", response.toString());
                    } catch (ClientProtocolException e) {
                        // writing exception to log
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(view.getContext(), "IO", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        Toast.makeText(view.getContext(), "JSON", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    builder1.setTitle(status);
                    builder1.setMessage(message);
                    builder1.setPositiveButton(
                            "ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // startActivity(new Intent(getContext(),BlockTabbedteacher.class));
                                    dialog.cancel();
                                    pager.setCurrentItem(1);
                                   /* adapter.setNotifyOnChange(true);
                                    adapter.notifyDataSetChanged();*/
                                    refresh();
                                    remark.setText("");
                                    et.setText("");
                                    et1.setText("");

                                }
                            });

                }
                    else if (et.getText().toString().length() == 0) {
                        et.setError("Enter start date first!");

                    } else if (et1.getText().toString().length() == 0) {
                        et1.setError("Enter end date first!");
                    } else if (l_id == 0) {
                        builder2.setMessage("Please select a Leave type");
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
                }else {
                    builder1.setTitle("Alert");
                    builder1.setMessage("No Internet Connection");
                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }

                            });


                    }
                AlertDialog alert11 = builder1.create();
                alert11.show();



                    /*if (l_id > 0) {
                        lev.insertdata(school_id, l_id, l_type,from_date.getText().toString(),to_date.getText().toString(),myBase64Image, submit_by, submit_on, remark.getText().toString(),1);
                        builder1.setTitle("Preliminary Leave Done");
                        builder1.setMessage("Please sync to complete the process");
                        builder1.setCancelable(false);

                        builder1.setPositiveButton(
                                "ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        remark.setText(null);

                                        pager.setCurrentItem(1);
                                        // startActivity(new Intent(getContext(),BlockTab1Fragment.class));
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    else {
                        builder2.setMessage("Please select a Leave type");
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
                    }*/


            }


        });


        return view;



    }
public void refresh(){
    Cursor cursor=td.leave_data();
    leave_type_id.clear();
    leave_type.clear();
    leave_type_id.add(0);
    leave_type.add("Select Leave Type");
    while (cursor.moveToNext()){
        leave_type.add(cursor.getString(1));
        leave_type_id.add(cursor.getInt(0));
    }
    adapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,leave_type.toArray() );
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

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
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
