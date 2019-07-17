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

public class GrievanceRecordAdapter extends RecyclerView.Adapter<GrievanceRecordAdapter.SubmitViewHolder> {
    int checkPermissionk;
    List<String> dates,category,grievance,grievance_id,flag;
    AlertDialog.Builder builder1;
    String[] escrito;
    private GrievanceDB grv;
    ProgressDialog dialog,dialogphone;
    String status;
    private Context context;
    HttpEntity httpEntity;
    public GrievanceRecordAdapter(List<String> dates, List<String> category, List<String> grievance, List<String> grievance_id,List<String> flag) {
        this.category=category;
        this.grievance=grievance;
        this.dates=dates;
        this.grievance_id=grievance_id;
        this.flag = flag;
    }

    @Override
    public SubmitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievance_status_item_row,parent,false);
        grv=new GrievanceDB(v.getContext());
        context=v.getContext();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        return new SubmitViewHolder(v);

    }

    @Override
    public void onBindViewHolder(SubmitViewHolder holder, int position) {
        String date= dates.get(position);
        String categorys=category.get(position);
        String grievence=grievance.get(position);
        String grievance_ids=grievance_id.get(position);
        String flags = flag.get(position);


        holder.bindProducto(date,categorys,grievence,grievance_ids,flags);
    }
    private boolean checkPermission(String permission) {

        return checkPermissionk==PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public int getItemCount() {
        return category.size();
    }

    public String[] getEscrito() {
        return escrito;
    }
    public class SubmitViewHolder extends RecyclerView.ViewHolder {
        TextView dates, category, grievance;
        Button sync;
        HttpResponse response;
        String message, status;
        int school_id, grvcat_id, submitby;
        String image, submit_on, grv_name, grvremark;


        public SubmitViewHolder(View itemView) {
            super(itemView);
            dates = (TextView) itemView.findViewById(R.id.date);
            category = (TextView) itemView.findViewById(R.id.cat);
            grievance = (TextView) itemView.findViewById(R.id.grv);
            sync = itemView.findViewById(R.id.sync);
            dialog = new ProgressDialog(itemView.getContext());
            dialogphone = new ProgressDialog(itemView.getContext());

        }

        public void bindProducto(final String date, final String categorys, String grievence, final String grievance_ids, String flag) {
            if(Integer.parseInt(flag)==1)
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
            else if(Integer.parseInt(flag)==2){
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
                            Cursor res = grv.getAllData11(Integer.parseInt(grievance_ids));
                            while (res.moveToNext()) {
                                school_id = res.getInt(1);
                                grvcat_id = res.getInt(2);
                                grv_name = res.getString(3);
                                image = res.getString(4);
                                submitby = res.getInt(5);
                                submit_on = res.getString(6);
                                grvremark = res.getString(7);

                            }
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://bhojpur.empoweru.in/grievance/");

                            //  String CSRFTOKEN =  getCsrfFromUrl("http://jharkhand.empoweru.in/teacher_attendance/");
                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
                            nameValuePair.add(new BasicNameValuePair("userid", String.valueOf(submitby)));
                            nameValuePair.add(new BasicNameValuePair("image", image));
                            nameValuePair.add(new BasicNameValuePair("school", String.valueOf(school_id)));
                            nameValuePair.add(new BasicNameValuePair("category", String.valueOf(grvcat_id)));
                            nameValuePair.add(new BasicNameValuePair("grievance", grvremark));
                            nameValuePair.add(new BasicNameValuePair("submit_on", submit_on));
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
                                                grv.update(Integer.parseInt(grievance_ids));
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

            String sourceString1 = "<b>" + "Date: " + "</b> " + date;
            String sourceString2 = "<b>" + "Category: " + "</b> " + categorys;
            String sourceString3 = "<b>" + "Grievance: " + "</b> " + grievence;
            dates.setText(Html.fromHtml(sourceString1));
            category.setText(Html.fromHtml(sourceString2));
            grievance.setText(Html.fromHtml(sourceString3));

        }

    }

}