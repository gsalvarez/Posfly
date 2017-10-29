package com.poli.posfly;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfile extends Fragment {

    private EditText txtName;
    private EditText txtLastname;
    private EditText txtEmail;
    private EditText txtPassR ;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public EditProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        Button btnUpdate = (Button) view.findViewById(com.poli.posfly.R.id.btnUpdate);
        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtName);
        txtLastname = (EditText) view.findViewById(com.poli.posfly.R.id.txtLastname);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmail);
        txtPassR = (EditText) view.findViewById(com.poli.posfly.R.id.txtPassR);

        //Click en el botón de Login valida la información para ser actualizada
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
