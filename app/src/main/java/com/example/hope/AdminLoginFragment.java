package com.example.hope;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginFragment extends Fragment {

    private EditText username, password;
    private Button admLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_admin_login, container, false);
        username = rootView.findViewById(R.id.username_input_login);
        password = rootView.findViewById(R.id.password_input_login);
        admLogin = rootView.findViewById(R.id.btnLogin);

        admLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (user.equals("admin") && pass.equals("admin"))
                {
                    Intent i  = new Intent(requireContext(),AdminHome.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(requireContext(),"Invalid username or password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}