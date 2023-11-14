package com.example.hope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.MyViewHolder>{

    private ArrayList<ActivityListModel> actDataList;
    private Context context;

    public ActivityListAdapter(ArrayList<ActivityListModel> actDataList, Context context) {
        this.actDataList = actDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activitylistitem,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.MyViewHolder holder, int position) {
        ActivityListModel actlist =actDataList.get(position);
        holder.aname.setText(actlist.getActivity());
        holder.aloc.setText(actlist.getLocation());
        holder.adate.setText(actlist.getDate());
        holder.atime.setText(actlist.getTime());

        if (actlist.isApproved()) {
            holder.appr.setImageResource(R.drawable.baseline_check_24); // Green checkmark
        } else {
            holder.appr.setImageResource(R.drawable.baseline_block_24); // Red cross
        }
    }

    @Override
    public int getItemCount() {
        return actDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView aname,aloc,adate,atime;
        ImageView appr;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            aname = itemView.findViewById(R.id.volActivityName);
            aloc = itemView.findViewById(R.id.volActivityLocation);
            adate = itemView.findViewById(R.id.volActivityDate);
            atime = itemView.findViewById(R.id.volActivityTime);
            appr = itemView.findViewById(R.id.volActivityApproval);
        }
    }
}
