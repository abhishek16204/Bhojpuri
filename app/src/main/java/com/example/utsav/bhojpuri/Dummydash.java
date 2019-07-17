package com.example.utsav.bhojpuri;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Dummydash extends AppCompatActivity {
    private GridView gridView;
    ModuleAdapter mda;
    ArrayList<ModuleList>mlist;
    TeacherDB td;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        gridView = findViewById( R.id.grid );
        td=new TeacherDB(Dummydash.this);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
          mlist=new ArrayList<>();
         mlist=td.getModuleData();
mda=new ModuleAdapter(Dummydash.this,mlist );
gridView.setAdapter(mda);
gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //  Toast.makeText(Dummydash.this, mlist.get(position).getModulename(), Toast.LENGTH_SHORT).show();
        if (mlist.get(position).getModulename().equals("Grievance")){
            startActivity(new Intent(Dummydash.this,Grievance.class));
        }
        else if (mlist.get(position).getModulename().equals("Leave")){
            startActivity(new Intent(Dummydash.this,Leave.class));
        }
           else if (mlist.get(position).getModulename().equals("Assessment")){
            startActivity(new Intent(Dummydash.this,Q_Activity.class));
        }

    }
});

    }

}
