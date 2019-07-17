package com.example.utsav.bhojpuri;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    TextView designation,email,name,block,degree,cluster,contact,school,doj,gender,dob,noa,district;
    private ImageButton back;
    private TeacherDB td=new TeacherDB(UserProfile.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        school=findViewById(R.id.schoolname);
        noa=findViewById(R.id.noa);
        doj=findViewById(R.id.doj);
        degree=findViewById(R.id.degree);
        district=findViewById(R.id.district);
        cluster=findViewById(R.id.cluster);
        dob=(TextView)findViewById(R.id.dob1);
        designation=(TextView)findViewById(R.id.designation);
        gender=(TextView)findViewById(R.id.gender);
        name=(TextView)findViewById(R.id.name);

        contact=(TextView)findViewById(R.id.contact);

        email=(TextView)findViewById(R.id.email);

        block=(TextView)findViewById(R.id.block);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>My Profile</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00b7ff")));
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Cursor res = td.getUserData();
        res.moveToFirst();
        //Toast.makeText(getApplicationContext(),res.getCount() +"\n"+res.getColumnCount(),Toast.LENGTH_SHORT ).show();
        //  res.moveToPosition(0);

        name.setText( res.getString(2));
        dob.setText("Date of Birth :"+ res.getString(10));

        if(res.getInt(9)==1){
            gender.setText("Gender :"+"Male");
        }
        else {
            gender.setText("Gender :" + "Female");
        }
        school.setText("School Name:"+res.getString(7));
        noa.setText("NOA:"+res.getString(11));
        doj.setText("Date of Joining:"+res.getString(8));
        degree.setText("Degree:"+res.getString(4));
        district.setText("District:"+res.getString(12));
        email.setText("Email:"+ res.getString(1));
        designation.setText("Designation :"+ res.getString(0));
        contact.setText("Contact :"+ res.getString(6));
        block.setText("Block :"+ res.getString(3));
        cluster.setText("Cluster:"+res.getString(5));
    }
}
