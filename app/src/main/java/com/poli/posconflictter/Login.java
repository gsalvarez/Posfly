package com.poli.posconflictter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Fragment {

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private EditText txtEmail;
    private EditText txtPass;

    private FirebaseAuth mAuth;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        Button btnReg = (Button) view.findViewById(R.id.btnReg);
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtEmail = (EditText) view.findViewById(R.id.txtEmailL);
        txtPass = (EditText) view.findViewById(R.id.txtPassL);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new Register());
                transaction.commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin(txtEmail.getText().toString(), txtPass.getText().toString());
            }
        });

        return view;
    }

    public void tryLogin(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Hay campos vacíos", Toast.LENGTH_SHORT).show();
        } else if (!validateEmail(email)) {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "La dirección de correo no es válida", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(txtEmail.getText().toString(), codePass(txtPass.getText().toString()))
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.fragment_container, new Start());
                                transaction.commit();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity().getApplication().getApplicationContext(), "Error al iniciar sesión, verifica los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public boolean validateEmail(final String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }



    public static String codePass(String pass) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(pass.getBytes(), 0, pass.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
}