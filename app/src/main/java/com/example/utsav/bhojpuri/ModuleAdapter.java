package com.example.utsav.bhojpuri;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class ModuleAdapter extends BaseAdapter {
    public Context context;
    public LayoutInflater inflater;
    ArrayList<ModuleList> mlist;
    public Spinner mySpinner;


    public ModuleAdapter(Context context,  ArrayList<ModuleList> mlist){
        this.context=context;
        this.mlist=mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        if (convertView==null)
        {
            inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.module_list,null);
        }
        viewHolder.imageView=view.findViewById(R.id.imageView);
        viewHolder.module_name=view.findViewById(R.id.textView);
        viewHolder.cardView=view.findViewById(R.id.cv);
        ModuleList module = new ModuleList();
        module = mlist.get(position);
        viewHolder.module_name.setText(module.getModulename());
        viewHolder.imageView.setImageBitmap(module.getModuleImage());
viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Toast.makeText(context,"You clicked" ,Toast.LENGTH_SHORT ).show();
        ((GridView)parent).performItemClick(v,position,0);
    }
});
        return view;
    }

    private class ViewHolder {
        private CardView cardView;
        private TextView module_name;
        private ImageView imageView;
    }

}