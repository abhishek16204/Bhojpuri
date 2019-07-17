package com.example.utsav.bhojpuri;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import static com.example.utsav.bhojpuri.ExternalStorageUtil.getPrivateExternalStorageBaseDir;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    public EditText e1,e2;
    public Button b1;
    ProgressDialog dialog;
    String usid, pawrd;
    AlertDialog.Builder builder1;
    private TeacherDB td=new TeacherDB(LoginActivity.this);
    private ClusterDB crp=new ClusterDB(LoginActivity.this);
   /* private HM_details hmd= new HM_details(LoginActivity.this);
    private CRP_details crp= new CRP_details(LoginActivity.this);
    private Block_details brp= new Block_details(LoginActivity.this);*/
    FileWriter fw;
    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[0-9]{0,10}"

    );
    String designation;
    String name;
    String level;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        b1=(Button)findViewById(R.id.login);
        e1=(EditText)findViewById(R.id.e1);
        e2=(EditText)findViewById(R.id.e2);
       /* LinearLayout linearLayout=findViewById(R.id.empoweruweb);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse("http://promorph.in/"));
                startActivity(in);
            }
        });*/
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Loading your profile data , please wait..");
                dialog.show();
                dialog.setCancelable(true);
                Log.d("Login Pressed", "In Login onCLick Listener");
                usid = e1.getText().toString().trim();
                pawrd = e2.getText().toString().trim();
                Boolean isconnected = ConnectivityReceiver.isConnected();
                if (usid.isEmpty() || pawrd.isEmpty()) {
                    dialog.cancel();
                    builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setTitle("Enter Login Credentials");
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
                } else if (checkEmail(usid)){

                    //checkConnection();
                    if (isconnected) {
                        new JSONTask().execute("http://bhojpur.empoweru.in/user_login/?username="+usid+"&password="+pawrd);

                    } else if (isconnected == false) {
                        dialog.cancel();
                        builder1 = new AlertDialog.Builder(v.getContext());
                        builder1.setTitle("Check Your Internet Connection!!");
                        //builder1.setMessage("Connect tO N");
                        builder1.setCancelable(false);
                        builder1.setPositiveButton(
                                "ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // startActivity(new Intent(view.getContext(),BlockTabbedteacher.class));
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }
                else {
                    dialog.cancel();
                    Toast.makeText(LoginActivity.this,"Invalid email,Enter Only 10 Digits!!!",Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }



    private class JSONTask extends AsyncTask<String, String, String> {
        @Override
        public String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();

                // connection.setDoOutput(true);
                connection.connect();
                int statu = connection.getResponseCode();
                if(statu < 400) {
                    InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                    reader = new BufferedReader(isr);
                } else{
                    InputStreamReader isr = new InputStreamReader(connection.getErrorStream());
                    reader = new BufferedReader(isr);
                }
                // InputStream stream = connection.getInputStream();
                // reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));

                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //return buffer.toString();
                String finalJson = buffer.toString();
                String loopeddata = " ";
                JSONObject parentObject = new JSONObject(finalJson);
                String message = parentObject.getString("message");
                String status = parentObject.getString("status");

                if (status.equals("Success")) {

                    JSONArray parentArray3 = parentObject.getJSONArray("lst");
                    JSONArray parentArray6 = parentObject.getJSONArray("user_details");
                    for (int i = 0; i < parentArray3.length(); i++) {
                        JSONObject finalobject = parentArray3.getJSONObject(i);
                        String role = finalobject.getString("role");
                        level = finalobject.getString("level");
                        name = finalobject.getString("name");
                        String user_id = finalobject.getString("user_id");

                    }
                    for (int i = 0; i < parentArray6.length(); i++) {
                        JSONObject finalobject = parentArray6.getJSONObject(i);
                        designation = finalobject.getString("designation");
                    }

                    loopeddata="s"+String.valueOf(level)+message;
                } else if(status.equals("Fail")){
                    loopeddata ="f"+message;
                }

                return loopeddata;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                /*  Toast.makeText(LoginActivity.this,"JSON" ,Toast.LENGTH_SHORT ).show();*/
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s!=null) {
                dialog.cancel();
                char first_char = s.charAt(0);
                char second_char = s.charAt(1);
                if (first_char == 's') {
                    if (second_char == '5') {
                        dialog.cancel();
                        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginData.edit();
                        editor.putString("level", designation);
                        editor.putString("name", name);
                        editor.putString("userid", usid);
                        editor.putString("password", pawrd);
                        editor.putString("levell", String.valueOf(second_char));
                        editor.apply();
                        // Toast.makeText(LoginActivity.this, "Data Stored to LocalPath Successfully ", Toast.LENGTH_LONG).show();
                        new JSONTaskHM().execute("http://bhojpur.empoweru.in/user_login/?username="+usid+"&password="+pawrd);

                        intent=new Intent(LoginActivity.this,TeacherDash.class);
                    }
                    else if (second_char == '4') {
                        dialog.cancel();
                        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginData.edit();
                        editor.putString("level", designation);
                        editor.putString("name", name);
                        editor.putString("userid", usid);
                        editor.putString("password", pawrd);
                        editor.putString("levell", String.valueOf(second_char));
                        editor.apply();
                        //   Toast.makeText(LoginActivity.this, "Data Stored to LocalPath Successfully ", Toast.LENGTH_LONG).show();
                        new JSONCRP().execute("http://bhojpur.empoweru.in/user_login/?username=" + usid + "&password=" + pawrd);

                        intent=new Intent(LoginActivity.this,CRPDash.class);
                    }
                    else if (second_char == '3') {
                        dialog.cancel();
                        SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginData.edit();
                        editor.putString("level", designation);
                        editor.putString("name", name);
                        editor.putString("userid", usid);
                        editor.putString("password", pawrd);
                        editor.putString("levell", String.valueOf(second_char));
                        editor.apply();
                       /* new JSONBlock().execute("http://jharkhand.empoweru.in/user_login/?username=" + usid + "&password=" + pawrd);
                        intent=new Intent(LoginActivity.this,BRPdash.class);*/
                    }

                } else  {
                    dialog.cancel();
                    builder1 = new AlertDialog.Builder(LoginActivity.this);
                    String msg = s.substring(1);

                    builder1.setTitle(msg);

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
            }
            else {
                dialog.cancel();
                Toast.makeText(LoginActivity.this, "Slow internet connection", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public class JSONTaskHM extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //return buffer.toString();
                String finalJson = buffer.toString();
                StringBuffer loopeddata = new StringBuffer();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray5 = parentObject.getJSONArray("school_detail");
                JSONArray parentArray1 = parentObject.getJSONArray("teacher_details");
                JSONArray parentArray4 = parentObject.getJSONArray("user_details");
                JSONArray parentArray6 = parentObject.getJSONArray("class_detail");
                JSONArray parentArray7 = parentObject.getJSONArray("student_details");
                JSONArray parentArray3 = parentObject.getJSONArray("lst");
                JSONArray parentArray2 = parentObject.getJSONArray("slot_lists");
                JSONArray parentArray8 = parentObject.getJSONArray("category");
                JSONArray parentArray9 = parentObject.getJSONArray("leave");
                JSONArray parentAr10 = parentObject.getJSONArray("ques");
                JSONArray parentAr11 = parentObject.getJSONArray("module");


                td.deleteData("class_detail");
                td.deleteData("student_details");
                td.deleteData("school_details");
                td.deleteData("teacher_details");
                td.deleteData("user_details");
                td.deleteData("lst");
                td.deleteData("slot_list");
                td.deleteData("question_details");
                td.deleteData("option_details");


                for (int i = 0; i < parentArray5.length(); i++) {
                    JSONObject finalobject = parentArray5.getJSONObject(i);
                    String block = finalobject.getString("block");
                    String dise_code = finalobject.getString("dise_code");
                    String school_category = finalobject.getString("school_category");
                    String cluster = finalobject.getString("cluster");
                    String school_name = finalobject.getString("school_name");
                    String is_coed = finalobject.getString("is_coed");
                    int control_department = finalobject.getInt("control_department");
                    int school_id = finalobject.getInt("school_id");
                    td.school_details(block, dise_code, school_category, cluster, school_name, is_coed,control_department,school_id);
                    loopeddata.append(block + dise_code + school_category + cluster + school_name + is_coed+control_department+school_id+"\n");

                }
                for (int i = 0; i < parentArray3.length(); i++) {
                    JSONObject finalobject = parentArray3.getJSONObject(i);
                    String role = finalobject.getString("role");
                    level = finalobject.getString("level");
                    String name = finalobject.getString("name");
                    String user_id = finalobject.getString("user_id");
                    td.insert(role, level, name, user_id);
                    loopeddata.append(role + level + name + user_id + "\n");
                }
               for (int i = 0; i < parentArray8.length(); i++) {
                    JSONObject finalobject = parentArray8.getJSONObject(i);
                    String qc_id = finalobject.getString("qc_id");
                    String category = finalobject.getString("category");
                    td.category_detail(qc_id, category);
                    loopeddata.append( qc_id + category + "\n");
                }

                for (int i = 0; i < parentArray9.length(); i++) {
                    JSONObject finalobject = parentArray9.getJSONObject(i);
                    String leave_type_id = finalobject.getString("leave_type_id");
                    String leave_type = finalobject.getString("leave_type");
                    td.leave(leave_type_id, leave_type);
                    loopeddata.append( leave_type_id + leave_type + "\n");
                }
               for (int i = 0; i < parentAr11.length(); i++) {
                    JSONObject finalobject = parentAr11.getJSONObject(i);
                    String order = finalobject.getString("order");
                    String icon = finalobject.getString("icon");
                     String name = finalobject.getString("name");
                    td.module(order, icon,name);
                    loopeddata.append( order + icon+name + "\n");
                }
                for (int i = 0; i < parentArray1.length(); i++) {
                    JSONObject finalobject = parentArray1.getJSONObject(i);
                    String designation = finalobject.getString("designation");
                    String teacher_id = finalobject.getString("teacher_id");
                    String contact = finalobject.getString("contact");
                    String role = finalobject.getString("role");
                    String gender = finalobject.getString("gender");
                    String teacher_name = finalobject.getString("teacher_name");
                    td.insert2(designation, teacher_id, contact, role, gender, teacher_name);
                    loopeddata.append(designation + teacher_id + contact + role + gender + teacher_name+"\n");

                }

                for (int i = 0; i < parentArray4.length(); i++) {
                    JSONObject finalobject = parentArray4.getJSONObject(i);
                    designation = finalobject.getString("designation");
                    String email = finalobject.getString("email");
                    name = finalobject.getString("name");
                    String block = finalobject.getString("block");
                    String degree = finalobject.getString("degree");
                    String cluster = finalobject.getString("cluster");
                    String contact = finalobject.getString("contact");
                    String school = finalobject.getString("school");
                    String doj = finalobject.getString("doj");
                    String gender = finalobject.getString("gender");
                    String dob = finalobject.getString("dob");
                    String noa = finalobject.getString("noa");
                    String district = finalobject.getString("district");
                    td.user_details(designation, email, name, block, degree, cluster, contact, school, doj, gender, dob, noa, district);
                    loopeddata.append(designation + email + name + block + degree + cluster + contact + school + doj + gender + dob + noa + district + "\n");
                }
                for (int i = 0; i < parentAr10.length(); i++) {
                    JSONObject finalobject = parentAr10.getJSONObject(i);
                    String question_id = finalobject.getString("question_id");
                    String question_order = finalobject.getString("question_order");
                    String question_name = finalobject.getString("question_name");
                    JSONArray optionArray = finalobject.getJSONArray("option");
                           for (int j=0;j<optionArray.length();j++)
                           {
                               JSONObject obj=optionArray.getJSONObject(j);
                               String option_id=obj.getString("option_id");
                               String option_name=obj.getString("option_name");
                              String option_order=obj.getString("option_order");

                               td.option_detail(question_id,option_id, option_name,option_order);
                               loopeddata.append(question_id + option_id + option_name + "\n");

                           }



                    td.question_detail(question_id, question_order, question_name);
                    loopeddata.append(question_id + question_order + question_name + "\n");

                }


                for (int i = 0; i < parentArray6.length(); i++) {
                    JSONObject finalobject = parentArray6.getJSONObject(i);
                    String class_id = finalobject.getString("class_id");
                    String class_name = finalobject.getString("class_name");
                    td.class_detail(class_id, class_name);
                    loopeddata.append(class_id + class_name + "\n");
                }
                for (int i = 0; i < parentArray7.length(); i++) {
                    JSONObject finalobject = parentArray7.getJSONObject(i);
                    String student_code = finalobject.getString("student_code");
                    String student_id = finalobject.getString("student_id");
                    String class_id = finalobject.getString("class_id");
                    String student_name = finalobject.getString("student_name");
                    td.student_details(student_code, student_id, class_id, student_name);
                    loopeddata.append(student_code + student_id + class_id + student_name + "\n");
                }

                String privateDirPath = getPrivateExternalStorageBaseDir(getApplicationContext(), TeacherDB.DATABASE);
                File newFile = new File(privateDirPath, TeacherDB.DATABASE);
                fw = new FileWriter(newFile);
                fw.write( "School_details:\n"+parentArray5+"\nTeacher_Details:\n"+parentArray1+"\n Slot_List_Details:\n"+parentArray2+"\n List_Details:\n"+parentArray3+
                        "\n User_Details:\n"+parentArray4+"\n Class_detail:\n"+parentArray6+"\n Student_details:\n"+parentArray7);
                fw.flush();
                fw.close();
                return loopeddata.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Splash_Screen.savePreferences("userlogin","5");

            startActivity(intent);
            dialog.cancel();
            e1.setText(null);
            e2.setText(null);
        }
    }
    private class JSONCRP extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //return buffer.toString();
                String finalJson = buffer.toString();
                StringBuffer loopeddata = new StringBuffer();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray1 = parentObject.getJSONArray("lst");
                JSONArray parentArray2 = parentObject.getJSONArray("class_detail");
                JSONArray parentArray3 = parentObject.getJSONArray("user_details");
                JSONArray parentArray4 = parentObject.getJSONArray("school_detail");
                JSONArray parentArray5 = parentObject.getJSONArray("student_details");
                JSONArray parentArray6 = parentObject.getJSONArray("slot_lists");
                JSONArray parentArray8 = parentObject.getJSONArray("cluster_detail");
                JSONArray parentArray7 = parentObject.getJSONArray("teacher_details");
                JSONArray parentArray9 = parentObject.getJSONArray("monitot_list");
                JSONArray parentArray10 = parentObject.getJSONArray("survey_option_list");
                JSONArray parentArray11 = parentObject.getJSONArray("survey_question_list");


                crp.deleteData("user_details");
                crp.deleteData("slot_lists");
                crp.deleteData("lst");
                crp.deleteData("school_details");
                crp.deleteData("cluster_details");
                crp.deleteData("student_details");
                crp.deleteData("class_detail");
                crp.deleteData("monitot_list");
                crp.deleteData("survey_option_list");
                crp.deleteData("survey_question_list");




                for (int i = 0; i < parentArray1.length(); i++) {
                    JSONObject finalobject = parentArray1.getJSONObject(i);
                    String role = finalobject.getString("role");
                    String  level = finalobject.getString("level");
                    String name = finalobject.getString("name");
                    String user_id = finalobject.getString("user_id");

                    crp.lst(role,level,name,user_id );
                    loopeddata.append(role+level+name+user_id);
                }
                for (int i = 0; i < parentArray2.length(); i++) {
                    JSONObject finalobject = parentArray2.getJSONObject(i);
                    String school_id = finalobject.getString("school_id");
                    String  class_name = finalobject.getString("class_name");
                    String class_id = finalobject.getString("class_id");
                    crp.class_detail(school_id,class_name ,class_id );
                    loopeddata.append(school_id+class_name+class_id);
                }
                for (int i = 0; i < parentArray5.length(); i++) {
                    JSONObject finalobject = parentArray5.getJSONObject(i);
                    String student_id = finalobject.getString("student_id");
                    String  student_name = finalobject.getString("student_name");
                    String class_id = finalobject.getString("class_id");
                    String student_code = finalobject.getString("student_code");
                    crp.student_detail(student_code,student_id ,class_id ,student_name );
                    loopeddata.append(student_code+student_id+class_id+student_name);
                }
                for (int i = 0; i < parentArray8.length(); i++) {
                    JSONObject finalobject = parentArray8.getJSONObject(i);
                    String cluster_name = finalobject.getString("cluster_name");
                    String  block_id = finalobject.getString("block_id");
                    String cluster_id = finalobject.getString("cluster_id");
                    crp.cluster_detail(cluster_name,block_id ,cluster_id );
                    loopeddata.append(cluster_name+block_id+cluster_id);
                }
                for (int i = 0; i < parentArray3.length(); i++) {
                    JSONObject finalobject = parentArray3.getJSONObject(i);
                    String designation = finalobject.getString("designation");
                    String email = finalobject.getString("email");
                    String name = finalobject.getString("name");
                    String block = finalobject.getString("block");
                    String cluster = finalobject.getString("cluster");
                    String contact = finalobject.getString("contact");
                    String gender = finalobject.getString("gender");
                    String dob = finalobject.getString("dob");
                    String district = finalobject.getString("district");
                    String blood_group = finalobject.getString("blood_group");
                    crp.user(email,cluster ,dob ,block ,name ,gender , contact,blood_group ,designation ,district );
                    loopeddata.append(email+cluster+dob+block+name+gender+contact+blood_group+designation+district);

                
                }
                for (int i = 0; i < parentArray4.length(); i++) {
                    JSONObject finalobject = parentArray4.getJSONObject(i);
                    String block = finalobject.getString("block");
                    String school_category = finalobject.getString("school_category");
                    String cluster_id = finalobject.getString("cluster_id");
                    String cluster = finalobject.getString("cluster");
                    String school_name = finalobject.getString("school_name");
                    String is_coed = finalobject.getString("is_coed");
                    String school_id = finalobject.getString("school_id");
                    String dise_code = finalobject.getString("dise_code");
                    crp.school_details(block,school_category ,cluster_id ,cluster ,school_name ,is_coed ,school_id , dise_code);
                    loopeddata.append(block+school_category+cluster_id+cluster+school_name+is_coed+school_id+dise_code);
                }
                for (int i = 0; i < parentArray6.length(); i++) {
                    JSONArray finalobject = parentArray6.getJSONArray(i);
                    String slot_id=finalobject.getString(0);
                    String from_time=finalobject.getString(1);
                    String to_time=finalobject.getString(2);
                    String bool=finalobject.getString(3);
                    crp.slot_lists(slot_id,from_time ,to_time ,bool );
                    loopeddata.append(slot_id+from_time+to_time+bool);

                }
                for (int i = 0; i < parentArray9.length(); i++) {
                    JSONObject finalobject = parentArray9.getJSONObject(i);
                    int input_type_id = finalobject.getInt("input_type_id");
                    int  q_no = finalobject.getInt("q_no");
                    String name_regional = finalobject.getString("name_regional");
                    int q_id = finalobject.getInt("q_id");
                    String q_name = finalobject.getString("q_name");
                    int is_image = finalobject.getInt("is_image");
                    int opg_id = finalobject.getInt("opg_id");

                    crp.monitot_list(input_type_id , q_no,is_image,q_name,q_id,name_regional,opg_id );
                    loopeddata.append(input_type_id+q_no+is_image+q_name+q_id+name_regional+opg_id);
                }
                for (int i = 0; i < parentArray10.length(); i++) {
                    JSONObject finalobject = parentArray10.getJSONObject(i);
                    String input_type_id = finalobject.getString("input_type_id");
                    String opc_id = finalobject.getString("opc_id");
                    String  opg_id = finalobject.getString("opg_id");
                    String option_choices = finalobject.getString("option_choices");
                    String ques_id = finalobject.getString("ques_id");
                    crp.survey_option_list(input_type_id,opc_id,opg_id,option_choices,ques_id );
                    loopeddata.append(input_type_id+opc_id+opg_id+option_choices+ques_id);
                }
                for(int i = 0;i<parentArray11.length();i++)
                {
                    JSONObject finalobject=parentArray11.getJSONObject(i);
                    String input_type_id=finalobject.getString("input_type_id");
                    String opg_id=finalobject.getString("opg_id");
                    String  is_image=finalobject.getString("is_image");
                    String  question=finalobject.getString("question");
                    String  ques_no=finalobject.getString("ques_no");
                    String  in_regional_language=finalobject.getString("in_regional_language");
                    String  ques_id=finalobject.getString("ques_id");
                    crp.survey_question_list(input_type_id,opg_id,is_image,question,ques_no,in_regional_language,ques_id);
                    loopeddata.append(input_type_id+opg_id+is_image+question+ques_no+in_regional_language+ques_id);
                }
                String privateDirPath = getPrivateExternalStorageBaseDir(getApplicationContext(), "CRP_DETAILS");
                File newFile = new File(privateDirPath, "CRP_DATA");
                fw = new FileWriter(newFile);
                fw.write( "List Details:\n"+parentArray1+"\nClass_Details:\n"+parentArray2+"\n User_Details:\n"+parentArray3+"\n School_Details:\n"+parentArray4+
                        "\n Student_Details:\n"+parentArray5+"\n Slot_list:\n"+parentArray6+"\n Teacher_details:\n"+parentArray7+"\n Cluster_details:\n"+parentArray8+
                        "\n Monitot_list:\n"+parentArray9+"\n Option_list:\n"+parentArray10+"\n Question_list:\n"+parentArray11);
                fw.flush();
                fw.close();
                return loopeddata.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                Splash_Screen.savePreferences("userlogin", "4");
                startActivity(intent);
                dialog.cancel();
                e1.setText(null);
                e2.setText(null);
            }
            else
                Toast.makeText(LoginActivity.this, "NO Data", Toast.LENGTH_SHORT).show();
        }
    }
   /* private class JSONBlock extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //return buffer.toString();
                String finalJson = buffer.toString();
                StringBuffer loopeddata = new StringBuffer();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray1 = parentObject.getJSONArray("lst");
                JSONArray parentArray2 = parentObject.getJSONArray("class_detail");
                JSONArray parentArray3 = parentObject.getJSONArray("user_details");
                JSONArray parentArray4 = parentObject.getJSONArray("school_detail");
                JSONArray parentArray5 = parentObject.getJSONArray("student_details");
                JSONArray parentArray6 = parentObject.getJSONArray("slot_lists");
                JSONArray parentArray7 = parentObject.getJSONArray("teacher_details");
                JSONArray parentArray8 = parentObject.getJSONArray("cluster_detail");
                JSONArray parentArray9 = parentObject.getJSONArray("monitot_list");
                JSONArray parentArray10 = parentObject.getJSONArray("option_list");


                brp.deleteData("user_details");
                brp.deleteData("slot_lists");
                brp.deleteData("lst");
                brp.deleteData("school_details");
                brp.deleteData("cluster_details");
                brp.deleteData("student_details");
                brp.deleteData("class_detail");
                brp.deleteData("monitot_list");
                brp.deleteData("option_list");

                for (int i = 0; i < parentArray1.length(); i++) {
                    JSONObject finalobject = parentArray1.getJSONObject(i);
                    String role = finalobject.getString("role");
                    String  level = finalobject.getString("level");
                    String name = finalobject.getString("name");
                    String user_id = finalobject.getString("user_id");
                    brp.lst(role,level ,name ,user_id );
                    loopeddata.append(role+level+name+user_id);
                }
                for (int i = 0; i < parentArray2.length(); i++) {
                    JSONObject finalobject = parentArray2.getJSONObject(i);
                    String school_id = finalobject.getString("school_id");
                    String  class_name = finalobject.getString("class_name");
                    String class_id = finalobject.getString("class_id");
                    brp.class_detail(school_id,class_name ,class_id );
                    loopeddata.append(school_id+class_name+class_id);
                }
                for (int i = 0; i < parentArray4.length(); i++) {
                    JSONObject finalobject = parentArray4.getJSONObject(i);
                    String block = finalobject.getString("block");
                    String school_category = finalobject.getString("school_category");
                    String cluster_id = finalobject.getString("cluster_id");
                    String cluster = finalobject.getString("cluster");
                    String school_name = finalobject.getString("school_name");
                    String is_coed = finalobject.getString("is_coed");
                    String school_id = finalobject.getString("school_id");
                    String dise_code = finalobject.getString("dise_code");
                    brp.school_details(block,school_category ,cluster_id ,cluster ,school_name ,is_coed ,school_id , dise_code);
                    loopeddata.append(block+school_category+cluster_id+cluster+school_name+is_coed+school_id+dise_code);
                }
                for (int i = 0; i < parentArray5.length(); i++) {
                    JSONObject finalobject = parentArray5.getJSONObject(i);
                    String student_id = finalobject.getString("student_id");
                    String  student_name = finalobject.getString("student_name");
                    String class_id = finalobject.getString("class_id");
                    String student_code = finalobject.getString("student_code");
                    brp.student_detail(student_id,student_name ,class_id ,student_code );
                    loopeddata.append(student_id+student_name+class_id+student_code);
                }

                for (int i = 0; i < parentArray3.length(); i++) {
                    JSONObject finalobject = parentArray3.getJSONObject(i);
                    String designation = finalobject.getString("designation");
                    String email = finalobject.getString("email");
                    String name = finalobject.getString("name");
                    String block = finalobject.getString("block");
                    String contact = finalobject.getString("contact");
                    String gender = finalobject.getString("gender");
                    String dob = finalobject.getString("dob");
                    String district = finalobject.getString("district");
                    String blood_group = finalobject.getString("blood_group");
                    brp.user(email,dob ,block ,name ,gender , contact,blood_group ,designation ,district );
                    loopeddata.append(email+dob+block+name+gender+contact+blood_group+designation+district);
                }
                for (int i = 0; i < parentArray6.length(); i++) {
                    JSONArray finalobject = parentArray6.getJSONArray(i);
                    String slot_id=finalobject.getString(0);
                    String from_time=finalobject.getString(1);
                    String to_time=finalobject.getString(2);
                    String bool=finalobject.getString(3);
                    brp.slot_lists(slot_id,from_time ,to_time ,bool );
                    loopeddata.append(slot_id+from_time+to_time+bool);

                }
                for (int i = 0; i < parentArray8.length(); i++) {
                    JSONObject finalobject = parentArray8.getJSONObject(i);
                    String cluster_name = finalobject.getString("cluster_name");
                    String  block_id = finalobject.getString("block_id");
                    String cluster_id = finalobject.getString("cluster_id");
                    brp.cluster_detail(cluster_name,block_id ,cluster_id  );
                    loopeddata.append(cluster_name+block_id+cluster_id);
                }
                for (int i = 0; i < parentArray9.length(); i++) {
                    JSONObject finalobject = parentArray9.getJSONObject(i);
                    int input_type_id = finalobject.getInt("input_type_id");
                    int  q_no = finalobject.getInt("q_no");
                    String name_regional = finalobject.getString("name_regional");
                    int q_id = finalobject.getInt("q_id");
                    String q_name = finalobject.getString("q_name");
                    int is_image = finalobject.getInt("is_image");
                    int opg_id = finalobject.getInt("opg_id");

                    brp.monitot_list(input_type_id , q_no,name_regional ,q_id ,q_name,is_image,opg_id );
                    loopeddata.append(input_type_id+q_no+name_regional+q_id+q_name+is_image);
                }
                for (int i = 0; i < parentArray10.length(); i++) {
                    JSONObject finalobject = parentArray10.getJSONObject(i);
                    String input_type_id = finalobject.getString("input_type_id");
                    String  opg_id = finalobject.getString("opg_id");
                    String q_id = finalobject.getString("q_id");
                    String opc_name_regional = finalobject.getString("opc_name_regional");
                    String opc_id = finalobject.getString("opc_id");
                    String option_choices = finalobject.getString("option_choices");
                    brp.option_list(input_type_id,opg_id ,q_id ,opc_name_regional,opc_id,option_choices );
                    loopeddata.append(input_type_id+opg_id+q_id+opc_name_regional+opc_id+option_choices);
                }
                return loopeddata.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {

                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Splash_Screen.savePreferences("userlogin","3");

            //  Toast.makeText(LoginActivity.this, "Data Stored to LocalPath Successfully ", Toast.LENGTH_LONG).show();

            startActivity(intent);
            dialog.cancel();
            e1.setText(null);
            e2.setText(null);
        }
    }*/
}
