package com.example.utsav.bhojpuri;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScannerList extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList=new ArrayList<>();
    private ScannerDB db=new ScannerDB(ScannerList.this);
    String question_no,qno;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_list);
         getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Scanned List</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00b7ff")));
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView= (ListView) findViewById(R.id.listView);

        Intent intent=getIntent();
        question_no=intent.getStringExtra("qid");
        cursor=db.scanned_data();
        cursor.moveToFirst();
        qno = cursor.getString(0);
        arrayList=new ScannerDB(this).getData();
        final ScannerAdapter adapter=new ScannerAdapter(this,arrayList);
        listView.setAdapter( adapter);
       /* findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannerList.this,Q_Activity.class));
            }
        });*/

    }
}
