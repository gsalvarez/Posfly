package com.poli.posfly.usuario;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poli.posfly.Start;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Fragment {

    //Patrón de emails
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private EditText txtEmail;
    private EditText txtPass;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        TextView tvReg = (TextView) view.findViewById(com.poli.posfly.R.id.tvReg);
        TextView tvForg = (TextView) view.findViewById(com.poli.posfly.R.id.tvForgot);
        Button btnLogin = (Button) view.findViewById(com.poli.posfly.R.id.btnLogin);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmailL);
        txtPass = (EditText) view.findViewById(com.poli.posfly.R.id.txtPassL);

        //Click en el botón de registro lleva al fragmento de registro
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fi");
                transaction.replace(com.poli.posfly.R.id.fragment_container, new Register(), "fr");
                transaction.commit();
            }
        });
        //Click en el botón de olvidé contraseña lleva al fragmento de recuperar contraseña
        tvForg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fi");
                transaction.replace(com.poli.posfly.R.id.fragment_container, new Forgot(), "ff");
                transaction.commit();
            }
        });

        //Click en el botón de Login valida la información para iniciar sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin(txtEmail.getText().toString(), txtPass.getText().toString());
            }
        });

        return view;
    }

    //Método que verifica el correo y la contraseña ingresada (Campos vacios, correo valido, datos en Firebase)
    public void tryLogin(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Hay campos vacíos", Toast.LENGTH_SHORT).show();
        } else if (!validateEmail(email)) {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "La dirección de correo no es válida", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Iniciando sesión, por favor espera...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPass.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Logueado, se muestra el fragmento de inicio
                                progressDialog.dismiss();
                                FirebaseUser user = mAuth.getCurrentUser();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(com.poli.posfly.R.id.fragment_container, new Start(), "fs");
                                transaction.commit();
                            } else {
                                // Mensaje si no inicia sesión
                                Toast.makeText(getActivity().getApplication().getApplicationContext(), "Error al iniciar sesión, verifica los datos", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    //Método que verifica que el correo sea válido
    public boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}