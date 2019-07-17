package com.example.utsav.bhojpuri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyLeaveRecordAdapter extends RecyclerView.Adapter<MyLeaveRecordAdapter.MyViewHolder> {

    Context context;
    List<Statuslistt> statuslist=new ArrayList<>();
    public MyLeaveRecordAdapter(Context context,List<Statuslistt>statuslist){
        this.context=context;
        this.statuslist=statuslist;
    }
    @NonNull
    @Override
    public MyLeaveRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_leave_list,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLeaveRecordAdapter.MyViewHolder myViewHolder, int i) {
        String st="",st1="",st2="",st3="";
        if (statuslist.get(i).getStatus().equals("1")){
            st="<font color='#FFC107'>...</font>"+"<font color='white'>Pending</font>"+"<font color='#FFC107'>...</font>";
            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse2);

        }
        else  if (statuslist.get(i).getStatus().equals("2")){
            st="<font color='#4CAF50'>...</font>"+"<font color='white'> Approve </font>"+" <font color='#4CAF50'>...</font>";

            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse3);

            //   myViewHolder.status.setBackgroundColor(ContextCompat.getColor(context,R.color.green));

        }
        else if (statuslist.get(i).getStatus().equals("3")){
            st="<font color='#F44336'>...</font>"+"<font color='white'> Reject </font>"+"<font color='#F44336'>...</font>";

            myViewHolder.status.setBackgroundResource(R.drawable.ecclipse1);

        }
        String s1="<b>"+"Reason      :  "+"</b>"+statuslist.get(i).getReason();
        String s2="<b>"+"Leave type :  "+"</b>"+statuslist.get(i).getLeave_type();
        String s3="<b>"+"From Date         :  "+"</b>"+statuslist.get(i).getFromDate();
        String s5="<b>"+"Leave Request ID   :  "+"</b>"+statuslist.get(i).getLeaveRequest_id();
        String s6="<b>"+"Created On     :  "+"</b>"+statuslist.get(i).getCreated_on();
        String s7="<b>"+"To Date          :  "+"</b>"+statuslist.get(i).getToDate();
        String s8="<b>"+"Days        :  "+"</b>"+statuslist.get(i).getDays();

        myViewHolder.reason.setText(Html.fromHtml(s1));
        myViewHolder.leave_type.setText(Html.fromHtml(s2));
        myViewHolder.from_date.setText(Html.fromHtml(s3));
        myViewHolder.status.setText(Html.fromHtml(st));
        myViewHolder.leave_request_id.setText(Html.fromHtml(s5));
        myViewHolder.created_on.setText(Html.fromHtml(s6));
        myViewHolder.to_date.setText(Html.fromHtml(s7));
        myViewHolder.days.setText(Html.fromHtml(s8));



    }

    @Override
    public int getItemCount() {
        return statuslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView reason,leave_type,from_date,leave_request_id,created_on,to_date,days,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reason=itemView.findViewById(R.id.reason);
            leave_type=itemView.findViewById(R.id.leave_type);
            status=itemView.findViewById(R.id.status);
            from_date=itemView.findViewById(R.id.from_date);
            leave_request_id=itemView.findViewById(R.id.leave_request_id);
            created_on=itemView.findViewById(R.id.created_on);
            to_date=itemView.findViewById(R.id.to_date);
            days=itemView.findViewById(R.id.days);




        }
    }


}
