package com.example.hope;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


public class UserLoginFragment extends Fragment {

    private TextInputEditText email, password;
    private Button login;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        email = rootView.findViewById(R.id.email_input_login);
        password = rootView.findViewById(R.id.password_input_login);
        login = rootView.findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email= email.getText().toString();
                String Password = password.getText().toString();

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userEmail", Email);
                editor.putBoolean("isLoggedIn",true);
                editor.apply();

                if (Patterns.EMAIL_ADDRESS.matcher(Email).matches() && Password.length() >= 8){

                    mAuth.signInWithEmailAndPassword(Email,Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent i = new Intent(requireContext(), HomeActivity.class);
                                        startActivity(i);


                                    }else {

                                        Toast.makeText(requireContext(),"Login Failed",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                }else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {

                    email.setError("Enter a valid Email address");
                    email.requestFocus();

                }else
                {

                    password.setError("Password must be atleast 8 character long");
                    password.requestFocus();

                }



            }
        });

        return rootView;
    }
}