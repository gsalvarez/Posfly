package com.poli.posfly.perfil;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.poli.posfly.usuario.Login;

public class ProfileF extends Fragment {
    private String URL = "prueba";

    public ProfileF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_profile, container, false);

        //URL = getArguments().getString("URL");

        Button btnLogout = (Button) view.findViewById(com.poli.posfly.R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment login = new Login();
                Bundle args = new Bundle();
                args.putString("URL", URL);
                login.setArguments(args);
                transaction.replace(com.poli.posfly.R.id.fragment_container, login, "fi");
                transaction.commit();
            }
        });

        return view;
    }
}