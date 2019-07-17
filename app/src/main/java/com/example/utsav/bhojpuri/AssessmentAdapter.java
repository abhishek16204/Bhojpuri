package com.example.utsav.bhojpuri;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.SubmitViewHolder> {
    int checkPermissionk;
    List<String>submitted_by,submitted_on,flag,assessmentt;
    AlertDialog.Builder builder1;
    String[] escrito;
    private SubmitScanDB ssb;
    private ScannerDB sdb;
    ProgressDialog dialog,dialogphone;
    String status;
    private Context context;
    HttpEntity httpEntity;
    public AssessmentAdapter( List<String> submitted_by, List<String> submitted_on, List<String> flag ,List<String> assessmentt) {
        this.submitted_by=submitted_by;
        this.submitted_on=submitted_on;
        this.flag = flag;
     this.assessmentt = assessmentt;

    }

    @Override
    public AssessmentAdapter.SubmitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assesment_item,parent,false);
        ssb=new SubmitScanDB(v.getContext());
         sdb=new ScannerDB(v.getContext());
        context=v.getContext();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return new AssessmentAdapter.SubmitViewHolder(v);

    }

    @Override
    public void onBindViewHolder(AssessmentAdapter.SubmitViewHolder holder, int position) {
        String sub_by=submitted_by.get(position);
        String sub_on=submitted_on.get(position);
        String flags = flag.get(position);
         String assesment_id = assessmentt.get(position);


        holder.bindProducto(sub_by,sub_on,flags,assesment_id);
    }
    private boolean checkPermission(String permission) {

        return checkPermissionk==PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public int getItemCount() {
        return submitted_by.size();
    }

    public String[] getEscrito() {
        return escrito;
    }
    public class SubmitViewHolder extends RecyclerView.ViewHolder {
        TextView dates, assessment;
        Button sync;
        HttpResponse response;
        String message, status;
        int  submitted_by;
        String  submit_on,respon="",stu_id="",ques_id="";


        public SubmitViewHolder(View itemView) {
            super(itemView);
            dates = (TextView) itemView.findViewById(R.id.date);
            assessment = (TextView) itemView.findViewById(R.id.cat);
            sync = itemView.findViewById(R.id.sync);
            dialog = new ProgressDialog(itemView.getContext());
            dialogphone = new ProgressDialog(itemView.getContext());

        }

        public void bindProducto( final String sub_by, String sub_on, String flags,String assesment_id) {
            if(Integer.parseInt(flags)==1)
            {
                sync.setClickable(false);
                sync.setText("");
                sync.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sync.getLayoutParams();
                lp.setMargins(0,13,20,0);
                sync.setLayoutParams(lp);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    sync.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.checked) );
                } else {
                    sync.setBackground(ContextCompat.getDrawable(context, R.drawable.checked));
                }
            }
            else if(Integer.parseInt(flags)==2){
                sync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder1 = new AlertDialog.Builder(view.getContext());
                        builder1.setCancelable(false);
                        Boolean isconnected= ConnectivityReceiver.isConnected();
                        if (isconnected) {
                            dialog.setCancelable(false);
                            dialog.setMessage("Syncing Data");
                            dialog.show();
                            Cursor res = ssb.getAllData11(Integer.parseInt(assesment_id));
                             Cursor rest = ssb.getAllData2(Integer.parseInt(assesment_id));
                            while (res.moveToNext()) {
                               // ques_id = res.getInt(1);
                                submitted_by = res.getInt(1);
                                submit_on = res.getString(2);
                               // stu_id = res.getInt(4);
                              //  respon = res.getString(5);
                            }
                            while (rest.moveToNext()) {
                                ques_id = ques_id + rest.getInt(1) + ",";
                                 stu_id = stu_id + rest.getInt(2) + ",";
                                respon = respon + rest.getString(3) + ",";
                            }
                            ques_id=ques_id.substring(0,ques_id.length()-1);
                            stu_id = stu_id .substring(0, stu_id.length() - 1);
                            respon = respon.substring(0, respon.length() - 1);
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://bhojpur.empoweru.in/assessment/");

                            //  String CSRFTOKEN =  getCsrfFromUrl("http://jharkhand.empoweru.in/teacher_attendance/");
                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                            nameValuePair.add(new BasicNameValuePair("submit_by", String.valueOf(submitted_by)));
                            nameValuePair.add(new BasicNameValuePair("answer", respon));
                            nameValuePair.add(new BasicNameValuePair("student_id", stu_id));
                            nameValuePair.add(new BasicNameValuePair("question",ques_id));
                            nameValuePair.add(new BasicNameValuePair("send_on", submit_on));
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
                            if (dialog != null) {
                                dialog.dismiss();
                            }

                            builder1.setTitle(status);
                            builder1.setMessage(message);
                            builder1.setPositiveButton(
                                    "ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // startActivity(new Intent(getContext(),BlockTabbedteacher.class));
                                            dialog.cancel();
                                            if(status.equals("Success")) {
                                                ssb.update(Integer.parseInt(assesment_id));
                                                sdb.deleteData("mul_qrcodesplit");
                                                sync.setClickable(false);
                                                sync.setText("");
                                                sync.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
                                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sync.getLayoutParams();
                                                lp.setMargins(0,13,20,0);
                                                sync.setLayoutParams(lp);
                                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                    sync.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.checked) );
                                                } else {
                                                    sync.setBackground(ContextCompat.getDrawable(context, R.drawable.checked));
                                                }
                                                //   Toast.makeText(context,"Success" ,Toast.LENGTH_SHORT ).show();
                                            }
                                           /* else
                                                Toast.makeText(context,"Fail" ,Toast.LENGTH_SHORT ).show();
*/
                                        }
                                    });
                        }
                       /* else
                        {
                            builder1.setTitle("No Internet Connection");
                            builder1.setMessage("Do you want to update attendance via mobile network?");

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // startActivity(new Intent(getContext(),BlockTabbedteacher.class));
                                            dialog.cancel();
                                            hsts.update(Integer.parseInt(String.valueOf(stu_atten)));
                                            sync.setClickable(false);
                                            sync.setText("");
                                            sync.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
                                            final int sdk = android.os.Build.VERSION.SDK_INT;
                                            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                                sync.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.checked) );
                                            } else {
                                                sync.setBackground(ContextCompat.getDrawable(context, R.drawable.checked));
                                            }


                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                        }*/
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });
            }

            String sourceString1 = "<b>" + "Date: " + "</b> " + sub_on;
            String sourceString2 = "<b>" + "Assessment: " + "</b> "+assesment_id ;
            dates.setText(Html.fromHtml(sourceString1));
            assessment.setText(Html.fromHtml(sourceString2));

        }

    }

}