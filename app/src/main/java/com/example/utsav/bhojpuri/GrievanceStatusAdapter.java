package com.example.utsav.bhojpuri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GrievanceStatusAdapter extends RecyclerView.Adapter<GrievanceStatusAdapter.MyViewHolder> {
    Context context;
    List<Statuslist> statuslist=new ArrayList<>();
    public GrievanceStatusAdapter(Context context,List<Statuslist>statuslist){
        this.context=context;
        this.statuslist=statuslist;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String st="",st1="",st2="",st3="";
        if (statuslist.get(i).getStatus().equals("Pending")){
            st="<font color='#FFC107'>...</font>"+"<font color='white'>Pending</font>"+"<font color='#FFC107'>...</font>";
            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse2);

        }
        else  if (statuslist.get(i).getStatus().equals("Done")){
            st="<font color='#4CAF50'>...</font>"+"<font color='white'> Done </font>"+" <font color='#4CAF50'>...</font>";

            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse3);

            //   myViewHolder.status.setBackgroundColor(ContextCompat.getColor(context,R.color.green));

        }
        else if (statuslist.get(i).getStatus().equals("In Progress")){
            st="<font color='#F44336'>...</font>"+"<font color='white'> In Progress </font>"+"<font color='#F44336'>...</font>";

            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse1);

        }
        String s1="<b>"+"Grievance      :  "+"</b>"+statuslist.get(i).getGrievance();
        String s2="<b>"+"Grievance type :  "+"</b>"+statuslist.get(i).getGrievance_type();
        String s3="<b>"+"School         :  "+"</b>"+statuslist.get(i).getSchool();
        String s5="<b>"+"Grievance ID   :  "+"</b>"+statuslist.get(i).getGrievance_id();
        String s6="<b>"+"Created On     :  "+"</b>"+statuslist.get(i).getCreated_on();
        String s7="<b>"+"Block          :  "+"</b>"+statuslist.get(i).getBlock();
        String s8="<b>"+"Cluster        :  "+"</b>"+statuslist.get(i).getCluster();

        myViewHolder.grv.setText(Html.fromHtml(s1));
        myViewHolder.grv_type.setText(Html.fromHtml(s2));
        myViewHolder.school.setText(Html.fromHtml(s3));
        myViewHolder.status.setText(Html.fromHtml(st));
        myViewHolder.grv_id.setText(Html.fromHtml(s5));
        myViewHolder.created_on.setText(Html.fromHtml(s6));
        myViewHolder.block.setText(Html.fromHtml(s7));
        myViewHolder.cluster.setText(Html.fromHtml(s8));



    }

    @Override
    public int getItemCount() {
        return statuslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView grv,grv_type,school,grv_id,created_on,block,cluster,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            grv=itemView.findViewById(R.id.grievance);
            grv_type=itemView.findViewById(R.id.grievance_type);
            status=itemView.findViewById(R.id.status);
            school=itemView.findViewById(R.id.school);
            grv_id=itemView.findViewById(R.id.grievance_id);
            created_on=itemView.findViewById(R.id.created_on);
            block=itemView.findViewById(R.id.block);
            cluster=itemView.findViewById(R.id.cluster);




        }
    }
}
