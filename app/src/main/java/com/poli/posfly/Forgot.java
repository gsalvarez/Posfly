package com.poli.posfly;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot extends Fragment {

    //Patrón de emails
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private EditText txtEmail;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    public Forgot() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_forgot, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        Button btnForgot = (Button) view.findViewById(com.poli.posfly.R.id.btnForgot);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmailF);

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().isEmpty()) {
                    Log.d("TAG", "Escribe el correo");
                }
                else if (!validateEmail(txtEmail.getText().toString())) {
                    Log.d("TAG", "Correo no válido");
                }
                else {
                    mAuth.sendPasswordResetEmail(txtEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Correo enviado", Toast.LENGTH_SHORT).show();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.popBackStackImmediate();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    //Método que verifica que el correo sea válido
    public boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}