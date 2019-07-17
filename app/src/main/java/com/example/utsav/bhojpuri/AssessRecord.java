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

public class AssessRecord extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{
    private RecyclerView recyclerview;

    List<String> submitted_by=new ArrayList<>(),submitted_on=new ArrayList<>(),
             flag=new ArrayList<>(),assessmentt=new ArrayList<>();
    private SubmitScanDB ssdb;
    AssessmentAdapter adapter;
    Cursor submit;LinearLayout no_record;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assesment_record, container, false);
        final ViewPager pager=   (ViewPager)getActivity().findViewById(R.id.viewpager);
        recyclerview = (android.support.v7.widget.RecyclerView) view.findViewById(R.id.rvPrueba);
        no_record=view.findViewById(R.id.no_record);
        android.support.v7.widget.LinearLayoutManager llm = new android.support.v7.widget.LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(llm);
        ssdb=new SubmitScanDB(getContext());
        submit=ssdb.getAllData3();
       submitted_by.clear();submitted_on.clear();
        assessmentt.clear();
        while (submit.moveToNext()) {

            assessmentt.add(submit.getString(0));
            submitted_by.add(String.valueOf(submit.getInt(1)));
            submitted_on.add(submit.getString(2));
            flag.add(String.valueOf(submit.getInt(3)));


        }

        Collections.reverse(submitted_by);
        Collections.reverse(submitted_on);
        Collections.reverse(flag);
        Collections.reverse(assessmentt);


        adapter = new AssessmentAdapter(submitted_by,submitted_on,flag,assessmentt);

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
