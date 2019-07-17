package com.example.utsav.bhojpuri;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ScannerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> arr1;
    public ScannerAdapter(Context context, ArrayList<String> arr)
    {
        this.context=context;
        this.arr1=arr;
    }
    @Override
    public int getCount() {
        return arr1.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View customview;
        customview = LayoutInflater.from(context).inflate( R.layout.custom_item,viewGroup,false);
        TextView name1 = customview.findViewById( R.id.tv1);
        name1.setText(arr1.get(i));
        return customview;
    }
}
