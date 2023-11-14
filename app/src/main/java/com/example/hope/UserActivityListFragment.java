package com.example.hope;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserActivityListFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<ActivityListModel> actDataList;
    private ActivityListAdapter activityListAdapter;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_user_activity_list, container, false);
        recyclerView = rootView.findViewById(R.id.activitylistrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        actDataList = new ArrayList<ActivityListModel>();
        activityListAdapter =new ActivityListAdapter(actDataList,requireContext());
        recyclerView.setAdapter(activityListAdapter);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        db = FirebaseFirestore.getInstance();
        db.collection("volactivity").whereEqualTo("Email",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        String actName = document.getString("Name");
                        String actLocation = document.getString("Location");
                        String actDate = document.getString("Date");
                        String actTime = document.getString("Time");
                        boolean actApproval = document.getBoolean("Approved");

                        ActivityListModel activityListModel = new ActivityListModel(actName, actLocation, actDate, actTime, actApproval);
                        actDataList.add(activityListModel);
                    }
                    activityListAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(requireContext(), "No matching documents found.", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), "Failed to retrieve data from Firestore.", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;

    }
}