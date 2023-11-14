package com.example.hope;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class UserSignupFragment extends Fragment {

    private TextInputEditText nameInput, emailInput, passwordInput,confirmPassword,phoneInput;
    private Button registerButton;
    private FirebaseAuth mAuth;
    Dialog dialog;
    private Boolean isVolApproved = false;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user_signup, container, false);

        nameInput = rootView.findViewById(R.id.name_input);
        emailInput = rootView.findViewById(R.id.email_input);
        passwordInput = rootView.findViewById(R.id.password_input);
        confirmPassword = rootView.findViewById(R.id.confirmpassword_input);
        registerButton = rootView.findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = nameInput.getText().toString().trim();
                String Email = emailInput.getText().toString();
                String Password = passwordInput.getText().toString();
                String ConfirmPassword = confirmPassword.getText().toString();




                if (Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {

                    if (Password.matches(ConfirmPassword) && Password.length() >= 8)
                    {

                        db.collection("volunteers").whereEqualTo("Email",Email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty())
                                {
                                    Toast.makeText(requireContext(),"Email Already signed",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Map<String, Object> volunteers = new HashMap<>();
                                    volunteers.put("Name",Name);
                                    volunteers.put("Email",Email);
                                    volunteers.put("Password",Password);
                                    volunteers.put("isApproved",isVolApproved);
                                    db.collection("volunteers").add(volunteers).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(requireContext(),"Successful",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(requireContext(),"Signup Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userEmail", Email);
                        editor.putBoolean("isLoggedIn",true);
                        editor.apply();

                        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(requireView().getWindowToken(),0);
                        dialog = new Dialog(requireContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_wait);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        mAuth.createUserWithEmailAndPassword(Email,Password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                        if (task.isSuccessful()){

                                            Toast.makeText(requireContext(),"Successfuly created",Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null)
                                            {

                                                dialog.dismiss();
                                                Intent i = new Intent(requireContext(), HomeActivity.class);
                                                startActivity(i);


                                            }

                                        }else {


                                            dialog.dismiss();
                                            Toast.makeText(requireContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });

                    }else if (Password.length() < 8){

                        passwordInput.setError("Password must be atleast 8 character long");
                        passwordInput.requestFocus();


                    }else {

                        confirmPassword.setError("Password doesn't match");
                        confirmPassword.requestFocus();
                    }


                }else {

                    emailInput.setError("Please enter a valid Email Address");
                    emailInput.requestFocus();
                }

            }
        });

        return rootView;
    }
}