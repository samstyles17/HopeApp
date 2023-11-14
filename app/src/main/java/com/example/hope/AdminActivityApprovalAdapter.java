package com.example.hope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminActivityApprovalAdapter extends RecyclerView.Adapter<AdminActivityApprovalAdapter.MyViewHolder>
{
    private ArrayList<AdminActivityApprovalClass> admactDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public AdminActivityApprovalAdapter(ArrayList<AdminActivityApprovalClass> actDataList, OnItemClickListener listener) {
        this.admactDataList = actDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminActivityApprovalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adminrequestlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminActivityApprovalAdapter.MyViewHolder holder, int position) {

        AdminActivityApprovalClass adminactlist = admactDataList.get(position);
        holder.volName.setText(adminactlist.getName());
        holder.volEmail.setText(adminactlist.getEmail());
        holder.activityNameTextView.setText(adminactlist.getActivity());
        holder.activityLocationTextView.setText(adminactlist.getLocation());
        holder.activityDateTextView.setText(adminactlist.getDate());
        holder.activityTimeTextView.setText(adminactlist.getTime());
    }

    @Override
    public int getItemCount() {
        return admactDataList.size();
    }

   public  class MyViewHolder extends RecyclerView.ViewHolder{

       private TextView activityNameTextView;
       private TextView activityLocationTextView;
       private TextView activityDateTextView;
       private TextView activityTimeTextView;
       private TextView volName,volEmail;
       private FloatingActionButton acceptButton;
       private FloatingActionButton rejectButton;

       public MyViewHolder(@NonNull View itemView) {
           super(itemView);

           volName = itemView.findViewById(R.id.admVolunteerName);
           volEmail = itemView.findViewById(R.id.admVolunteerEmail);
           activityNameTextView = itemView.findViewById(R.id.admActivityName);
           activityLocationTextView = itemView.findViewById(R.id.admActivityLocation);
           activityDateTextView = itemView.findViewById(R.id.admActivityDate);
           activityTimeTextView = itemView.findViewById(R.id.admActivityTime);
           acceptButton = itemView.findViewById(R.id.acceptButton);
           rejectButton = itemView.findViewById(R.id.rejectButton);

           acceptButton.setOnClickListener(v -> {
               int position = getAdapterPosition();
               if (position != RecyclerView.NO_POSITION) {
                   listener.onAcceptClick(position);
               }
           });

           rejectButton.setOnClickListener(v -> {
               int position = getAdapterPosition();
               if (position != RecyclerView.NO_POSITION) {
                   listener.onRejectClick(position);
               }
           });
       }
   }


}
