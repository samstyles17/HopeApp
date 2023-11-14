package com.example.hope;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminActivityApprovalFragment extends Fragment {

    private static final String CHANNEL_ID = "activity_approval_channel";
    private static final int NOTIFICATION_ID = 1;

    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private AdminActivityApprovalAdapter adminActivityApprovalAdapter;
    private ArrayList<AdminActivityApprovalClass> admactDataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_activity_approval, container, false);
        firestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.adminactivitylistrecyclerview);
        admactDataList = new ArrayList<AdminActivityApprovalClass>();
        adminActivityApprovalAdapter = new AdminActivityApprovalAdapter(admactDataList, new AdminActivityApprovalAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(int position) {
                AdminActivityApprovalClass adminActivityApprovalClass = admactDataList.get(position);
                updateApprovalStatus(adminActivityApprovalClass.getDocumentId(), true);
            }

            @Override
            public void onRejectClick(int position) {
                AdminActivityApprovalClass adminActivityApprovalClass = admactDataList.get(position);
                updateApprovalStatus(adminActivityApprovalClass.getDocumentId(), false);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adminActivityApprovalAdapter);

        fetchActivityData();
        return view;
    }

    private void fetchActivityData() {
        firestore.collection("volactivity")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        admactDataList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            AdminActivityApprovalClass activity = document.toObject(AdminActivityApprovalClass.class);
                            activity.setDocumentId(document.getId());
                            admactDataList.add(activity);
                        }
                        adminActivityApprovalAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateApprovalStatus(String documentId, boolean approved) {
        firestore.collection("volactivity")
                .document(documentId)
                .update("Approved", approved)
                .addOnSuccessListener(aVoid -> {
                    // Update successful, send notification and refresh the RecyclerView
                    sendNotification(approved ? "Activity Approved" : "Activity Not Approved");
                    fetchActivityData();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update approval status.", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendNotification(String message) {
        createNotificationChannel();

        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Activity Approval")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Activity Approval Channel";
            String description = "Channel for activity approval notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}