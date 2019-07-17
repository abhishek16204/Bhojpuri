package com.example.utsav.bhojpuri;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SurveyRecordAdapter extends ArrayAdapter<Integer> {


    Button sync;

    Context context;
    List<Integer> listS;
    int isSync, cluster_id = 0;
    String timeStampStr = null, schoolNameStr = null, date = null, remark = null;
    String question, option, imageStr;
    int role = 0, marked_by_id = 0;
    HttpResponse response;
    String message = null, status = null;
    SyncWithServerAsync async;




    public SurveyRecordAdapter(Context context, List<Integer> listS) {
        super(context, R.layout.list_item_survey_record, listS);
        this.listS = listS;
        this.context = context;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_survey_record, parent, false);

        ClusterDB clusterDB = new ClusterDB(context);
        Cursor cursorCluster = clusterDB.getCluster();
        while (cursorCluster.moveToNext()) {
            cluster_id = cursorCluster.getInt(2);
        }

        Cursor cursor = clusterDB.getDataForSchool(listS.get(position), cluster_id);

        question = "";
        option = "";
        imageStr = "";
        while (cursor.moveToNext()) {
            question = question + cursor.getString(0) + ",";
            option = option + cursor.getString(1) + ",";
            if (cursor.getString(3) == null) {
                imageStr = imageStr + "" + ",";
            } else {
                imageStr = imageStr + cursor.getString(3) + ",";
            }
        }

        question = question.substring(0, question.length() - 1);
        option = option.substring(0, option.length() - 1);
        imageStr = imageStr.substring(0, imageStr.length() - 1);


        Cursor c = clusterDB.getDataForSchool(listS.get(position), cluster_id);
        if (c.moveToNext()) {
            timeStampStr = c.getString(4);
            date = c.getString(8);
            remark = c.getString(7);
            isSync = c.getInt(2);
        }


        Cursor c1 = clusterDB.getSchoolName(listS.get(position));
        if (c1.moveToFirst()) {
            schoolNameStr = c1.getString(4);
        }

        Cursor cursorLst = clusterDB.getLST();
        if (cursorLst.moveToNext()) {
            role = cursorLst.getInt(3);
            marked_by_id = cursorLst.getInt(1);
        }


        TextView schoolName = rowView.findViewById(R.id.school_name_survey_record);
        TextView timeStamp = rowView.findViewById(R.id.timestamp_survey_record);

        schoolName.setText(schoolNameStr);
        timeStamp.setText(timeStampStr);

        sync = rowView.findViewById(R.id.sync_survey_record);


        if (isSync == 1) {
            sync.setClickable(false);
            sync.setText("");
            sync.setLayoutParams(new LinearLayout.LayoutParams(40, 40));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sync.getLayoutParams();
            lp.setMargins(0, 13, 20, 0);
            sync.setLayoutParams(lp);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                sync.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.checked));
            } else {
                sync.setBackground(ContextCompat.getDrawable(context, R.drawable.checked));
            }
        }else {
            sync.setOnClickListener(new View.OnClickListener() {
                @Override
                public synchronized void onClick(View v) {


                    Boolean isconnected = ConnectivityReceiver.isConnected();
                    if (isconnected == false) {
                        Toast.makeText(context, "Chech your internet conntection and Retry..", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cursor s = clusterDB.getDataForSchool(listS.get(position), cluster_id);

                    int temp = 1;

                    if(s.moveToNext()){
                        temp=0;
                    }

                    if (temp == 0) {
                        status = null;
                        message = null;

                        async = new SyncWithServerAsync();
                        async.execute(String.valueOf(listS.get(position)), timeStampStr, date, remark, String.valueOf(role), String.valueOf(marked_by_id), question, option, imageStr);

                    } else {
                        Toast.makeText(context, "Already synced, Stop pressing that button.", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
        return rowView;
    }

    class SyncWithServerAsync extends AsyncTask<String, String, String> {
        ProgressDialog dialog = new ProgressDialog(context);
        ClusterDB clusterDB = new ClusterDB(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Sending data to server");
            dialog.show();
            dialog.setCancelable(false);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String sid = s.substring(0, s.lastIndexOf(","));



            Log.e("ddrrr", s + "        " + sid);
            Toast.makeText(context, "School-id" + sid, Toast.LENGTH_LONG).show();
            dialog.cancel();
            if (status != null) {
                if (!status.equals("Fail")) {
                    Toast.makeText(context, "Submitted to server", Toast.LENGTH_SHORT).show();



                    clusterDB.updateIsSync(Integer.parseInt(sid));


                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        sync.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.checked));
                    } else {
                        sync.setBackground(ContextCompat.getDrawable(context, R.drawable.checked));
                    }
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, " not Submitted to server", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
            }
            //async.cancel(true);
            //async = null;
        }

        @Override
        protected String doInBackground(String... strings) {

            String school_id = strings[0];
            String timeStampStr = strings[1];
            String date = strings[2];
            String remark = strings[3];
            String role = strings[4];
            String marked_by_id = strings[5];
            String question = strings[6];
            String option = strings[7];
            String imageStr = strings[8];

            Log.e("check", school_id + " " + timeStampStr + " " + date + " " + remark + " " + role + " " + marked_by_id + " " + question + " " + option);

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost("http://bhojpur.empoweru.in/baseline_response/");
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(10);
            nameValuePair.add(new BasicNameValuePair("school_id", school_id));
            nameValuePair.add(new BasicNameValuePair("date", date));
            nameValuePair.add(new BasicNameValuePair("role", role));
            nameValuePair.add(new BasicNameValuePair("markedtype", String.valueOf(4)));
            nameValuePair.add(new BasicNameValuePair("marked_by_id", marked_by_id));
            nameValuePair.add(new BasicNameValuePair("markedon", timeStampStr));
            nameValuePair.add(new BasicNameValuePair("remark", remark));
            nameValuePair.add(new BasicNameValuePair("ques_id", question));
            nameValuePair.add(new BasicNameValuePair("response", option));
            nameValuePair.add(new BasicNameValuePair("image", imageStr));

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
                if (message.equals("Failed") && status.equals("Fail")) {

                    Log.e("failed ", " failed to send data to server");

                    //Toast.makeText(context,"failed to upload data to server.",Toast.LENGTH_LONG ).show();
                } else {
                    Log.e("PAssed ", " sent data to server");

                    //Toast.makeText(context,"Successfully uploaded data to server.",Toast.LENGTH_LONG ).show();
                }
                Log.d("Http Response:", response.toString());
            } catch (ClientProtocolException e) {
                // writing exception to log
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(context, "IO", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (JSONException e) {
                Toast.makeText(context, "JSON", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            String re = school_id + "," + status;

            return re;
        }
    }

}
