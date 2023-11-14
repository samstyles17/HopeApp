package com.example.hope;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ActivityRequestFragment extends Fragment {

    private String UsName, UsEmail;
    private TextView txtName,txtEmail;
    private Spinner actSpinner,locSpinner;
    private String[] activities = {"Adult Literacy","Health Awareness","SDG","Human Rights"};
    private String[] locations = {"New Delhi", "Bengaluru","Hyderabad","Chennai","Kolkata","Mumbai","Bhopal","Trivandrum","Pune","Ahmedabad","Jaipur","Bhubaneshwar","Raipur","Lucknow","Patna",};

    private Boolean isActivityApproved = false;
    FirebaseFirestore db;
    private Button reqBtn,dateBtn,timeBtn;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private TextView selectedDateTimeTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_activity_request, container, false);

        txtName = rootView.findViewById(R.id.volName);
        txtEmail = rootView.findViewById(R.id.volEmail);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actSpinner = rootView.findViewById(R.id.spinner);
        actSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner = rootView.findViewById(R.id.spinner2);
        locSpinner.setAdapter(adapter2);

        dateBtn = rootView.findViewById(R.id.pickDate);
        timeBtn = rootView.findViewById(R.id.pickTime);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("userEmail", "");

        db = FirebaseFirestore.getInstance();
        db.collection("volunteers").whereEqualTo("Email",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty())
                {
                    DocumentSnapshot volunteerSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    UsName = volunteerSnapshot.getString("Name");
                    UsEmail = volunteerSnapshot.getString("Email");

                    txtName.setText(UsName);
                    txtEmail.setText(UsEmail);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(),"Unable to detect Name and Email",Toast.LENGTH_SHORT).show();
            }
        });




        reqBtn = rootView.findViewById(R.id.volactSubmit);
        dateBtn = rootView.findViewById(R.id.pickDate);
        timeBtn = rootView.findViewById(R.id.pickTime);
        selectedDateTimeTextView = rootView.findViewById(R.id.selectedDateTimeTextView);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        updateSelectedDateTime();

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        txtName.setText(UsName);
        txtEmail.setText(UsEmail);



        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateStr = dateFormatter.format(calendar.getTime());
                String timeStr = timeFormatter.format(calendar.getTime());
                String VName = txtName.getText().toString().trim();
                Log.d("Username",VName);
                String VEmail = txtEmail.getText().toString().trim();
                Log.d("User Email",VEmail);
                String act = actSpinner.getSelectedItem().toString();


                String loc = locSpinner.getSelectedItem().toString();
                Map<String, Object> volActivity = new HashMap<>();
                volActivity.put("Name",UsName);
                volActivity.put("Email",UsEmail);
                volActivity.put("Date",dateStr);
                volActivity.put("Time",timeStr);
                volActivity.put("Activity",act);
                volActivity.put("Location",loc);
                volActivity.put("Approved",isActivityApproved);

                db.collection("volactivity").add(volActivity).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(requireContext(),"Activity Request Sent",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(),"Activity Request Sent",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return rootView;
    }
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        Calendar currentDate = Calendar.getInstance();

                        if (selectedDate.before(currentDate)) {
                            // The selected date is before the current date.
                            // Show an error message or reset the date picker to the current date.
                            // You can display a Toast message or a dialog to inform the user.
                            // For example:
                            Toast.makeText(requireContext(), "Please select a future date", Toast.LENGTH_SHORT).show();
                        } else {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateSelectedDateTime();
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        Calendar currentTime = Calendar.getInstance();

                        if (selectedTime.before(currentTime)) {
                            // The selected time is before the current time.
                            // Show an error message or reset the time picker to the current time.
                            // You can display a Toast message or a dialog to inform the user.
                            // For example:
                            Toast.makeText(requireContext(), "Please select a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            updateSelectedDateTime();
                        }
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }
    private void updateSelectedDateTime() {
        String dateStr = dateFormatter.format(calendar.getTime());
        String timeStr = timeFormatter.format(calendar.getTime());
        selectedDateTimeTextView.setText(dateStr + " " + timeStr);
    }
}