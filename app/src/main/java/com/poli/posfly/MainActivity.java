package com.poli.posfly;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.poli.posfly.usuario.Login;

public class MainActivity extends AppCompatActivity {

    public String URL = "http://192.168.0.7/posfly/"; // Gabriel
    //public String URL = "http://xxx.xxx.x.x/posfly/"; // Jeimy
    //public String URL = "http://xxx.xxx.x.x/posfly/"; // Camilo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.poli.posfly.R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            fragment = new Start();
        }
        else {
            fragment = new Login();
        }

        Bundle args = new Bundle();
        args.putString("URL", URL);
        fragment.setArguments(args);
        transaction.add(com.poli.posfly.R.id.fragment_container, fragment, "fi");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}