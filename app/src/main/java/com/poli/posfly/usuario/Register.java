package com.poli.posfly.usuario;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.poli.posfly.Start;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Fragment {

    //Patrón de emails
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String info = "";
    private String sName;
    private String sLastname;
    private String sUser;
    private String sEmail;
    private String sPass;
    private String sRepass;

    private EditText txtName;
    private EditText txtLastname;
    private EditText txtUser;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtRepass;
    private ProgressDialog progressDialog;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public Register () {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_register, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        //Listener que escucha el estado de sesión del usuario
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
            }
        };

        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtName);
        txtLastname = (EditText) view.findViewById(com.poli.posfly.R.id.txtLastname);
        txtUser = (EditText) view.findViewById(com.poli.posfly.R.id.txtUser);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmailR);
        txtPass = (EditText) view.findViewById(com.poli.posfly.R.id.txtPassR);
        txtRepass = (EditText) view.findViewById(com.poli.posfly.R.id.txtRepassR);
        Button btnCreate = (Button) view.findViewById(com.poli.posfly.R.id.btnCreate);

        //Al oprimir el botón crear, verifica todos los campos
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = txtName.getText().toString().trim();
                sLastname = txtLastname.getText().toString().trim();
                sUser = txtUser.getText().toString().toLowerCase().trim();
                sEmail = txtEmail.getText().toString().trim();
                sPass = txtPass.getText().toString().trim();
                sRepass = txtRepass.getText().toString().trim();

                progressDialog.setMessage("Registrando, por favor espera...");
                progressDialog.show();
                if (checkFieldsPasswordsAndEmail(sName, sLastname, sUser, sEmail, sPass, sRepass)) {
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.getKey().equals(sName)) {
                                    info = "El usuario o el correo ya está en uso";
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                if (info.equals("Error en la conexión")) {
                    mAuth.createUserWithEmailAndPassword(sEmail, sPass)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                                        mDatabase.child(sUser).setValue(new User (sUser, sName, sLastname, sEmail, "user"));
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(com.poli.posfly.R.id.fragment_container, new Start());
                                        transaction.commit();
                                    }else {
                                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }

    //revisa los campos, las contraseñas, el email
    public boolean checkFieldsPasswordsAndEmail (String name, String lastname, String user, String email, String pass, String rePass) {
        if (name.isEmpty() || lastname.isEmpty() || user.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        else if (pass.length() < 6) {
            info = "La contraseña debe tener mínimo 6 caracteres";
            return false;
        }
        else if (!pass.equals(rePass)) {
            info = "Las contraseñas no coinciden";
            return false;
        }
        else if (!validateEmail(email)) {
            info = "La dirección de correo no es válida";
            return false;
        }
        else {
            info = "Error en la conexión";
            return true;
        }
    }

    //Método que verifica el email
    public boolean validateEmail (final String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
