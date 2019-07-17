package com.example.utsav.bhojpuri;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrievanceRecord extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
private RecyclerView recyclerview;

        List<String> dates=new ArrayList<>(),category=new ArrayList<>(),grievance=new ArrayList<>(),
                grievance_id=new ArrayList<>(), flag=new ArrayList<>();
private GrievanceDB grv;
        GrievanceRecordAdapter adapter;
        Cursor submit;LinearLayout no_record;

@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.grievancerecord, container, false);
final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);

        // Toast.makeText(getContext(),"OnCreate" ,Toast.LENGTH_SHORT ).show();
        recyclerview = (android.support.v7.widget.RecyclerView) view.findViewById(R.id.rvPrueba);
        no_record=view.findViewById(R.id.no_record);
        android.support.v7.widget.LinearLayoutManager llm = new android.support.v7.widget.LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(llm);
        grv=new GrievanceDB(getContext());
        submit=grv.getAllData3();
        dates.clear();grievance_id.clear();category.clear();
        while (submit.moveToNext()) {

                grievance.add(submit.getString(7));
                category.add(submit.getString(3));
        dates.add(submit.getString(6));
                grievance_id.add(String.valueOf(submit.getInt(0)));
                flag.add(String.valueOf(submit.getInt(8)));
        }


        Collections.reverse(dates);
        Collections.reverse(category);
        Collections.reverse(grievance);
        Collections.reverse(grievance_id);
        Collections.reverse(flag);
        adapter = new GrievanceRecordAdapter(dates,category,grievance,grievance_id,flag);

        no_record=view.findViewById(R.id.no_record);
        if(adapter.getItemCount()==0){
        no_record.setVisibility(View.VISIBLE);
        }
        else{
        no_record.setVisibility(View.GONE);
        recyclerview.setAdapter(adapter);}

        return view;
        }



@Override
public void onNetworkConnectionChanged(boolean isConnected) {

        }
        }
