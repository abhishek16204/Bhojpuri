package com.example.utsav.bhojpuri;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SurveyRecord extends Fragment {

    ListView listView;
    TextView no_record;
    ClusterDB crp;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baseline_survey_record, container, false);
        no_record = view.findViewById(R.id.no_record_survey_record);
        crp = new ClusterDB(getContext());

        no_record.setText("No Records Found");

        crp = new ClusterDB(view.getContext());

        Cursor cursor = crp.getAllData();

        List<Integer> tempS = new ArrayList<>();

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            Log.e("testing ", cursor.getInt(5)+"    ");
            tempS.add(cursor.getInt(5));

        }

      /*  for (int i = 0; i < tempS.size(); i++) {
            for (int j = i + 1 ; j < tempS.size(); j++) {
                if (tempS.get(i).equals(tempS.get(j))) {
                    Log.e("re", "onCreateView: inside for ->for");
                    tempS.remove(j);
                }
            }
        }*/

        Set<Integer> set = new LinkedHashSet<>();
        List<Integer> toRemove = new ArrayList<>();
        for(int i: tempS){
            if(set.add(i)==true){
                toRemove.add(i);
            }
        }

        tempS.clear();
        tempS.addAll(toRemove);



        for (int i = 0; i < tempS.size(); i++) {
            Log.e("errr", tempS.size()+"   "+tempS.get(i)+"   "+i);
        }

        //Set<Integer> s = new LinkedHashSet<>(tempS);
        //tempS.clear();
        //tempS.addAll(s);
        SurveyRecordAdapter adapter = new SurveyRecordAdapter(view.getContext(), tempS);

        if (tempS.size() > 0) {
            no_record.setVisibility(View.GONE);
        }

        listView = view.findViewById(R.id.listView_surveyRecord);
        listView.setAdapter(adapter);
        return view;
    }


}
