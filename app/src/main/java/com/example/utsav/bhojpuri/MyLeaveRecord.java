package com.example.utsav.bhojpuri;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyLeaveRecord extends Fragment {

    RecyclerView recyclerView;
    AlertDialog.Builder builder1;
    MyLeaveRecordAdapter adapter;
    LinearLayout no_record;
    ProgressDialog dialog;
    List<Statuslistt> statuslistitem;
    private TeacherDB td;
    int userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_leave_record, container, false);

        recyclerView=view.findViewById(R.id.recycle);
        td=new TeacherDB(getContext());
        dialog=new ProgressDialog(getContext());
        dialog.setMessage("Loading Please wait....");
        dialog.show();
        dialog.setCancelable(true);
        no_record=view.findViewById(R.id.no_record);
        statuslistitem=  new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor res=td.getAllData3();
        res.moveToFirst();
        userid = res.getInt(3);

        String url="http://bhojpur.empoweru.in/My_leave/?userid="+userid;
        Boolean isconnected = ConnectivityReceiver.isConnected();
        if (isconnected) {
            new MyLeaveRecord.fetchData().execute(url);
        }
        else if (isconnected == false) {
            dialog.cancel();
            builder1 = new AlertDialog.Builder(getContext());
            builder1.setMessage("Check Your Internet Connection!!");
            //builder1.setMessage("Connect tO N");
            builder1.setCancelable(false);
            builder1.setPositiveButton(
                    "ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


        return view;
    }
    public class fetchData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String respUrl=strings[0];
            try {
                String st = "";
                URL url = new URL(respUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuffer buffer = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject parentObject = new JSONObject(s);
                JSONArray JA = parentObject.getJSONArray("myleave");
                for (int i = 0; i < JA.length(); i++) {

                    Statuslistt item=new Statuslistt();
                    JSONObject list_obj=JA.getJSONObject(i);
                    item.setLeaveRequest_id(list_obj.getInt("leave_request_id"));
                    item.setFromDate(list_obj.getString("from_date"));
                    item.setToDate(list_obj.getString("to_date"));
                    item.setDays(list_obj.getString("days"));
                    item.setReason(list_obj.getString("reason"));
                    item.setLeave_type(list_obj.getString("leave_type"));
                    item.setCreated_on(list_obj.getString("req_on"));
                    item.setStatus(list_obj.getString("status"));



                    statuslistitem.add(item);


                }
                adapter=new MyLeaveRecordAdapter(getContext(),statuslistitem);

                if(adapter.getItemCount()==0){
                    dialog.cancel();
                    no_record.setVisibility(View.VISIBLE);
                }
                else{
                    dialog.cancel();
                    no_record.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);}
            }  catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();

        }
    }
}
