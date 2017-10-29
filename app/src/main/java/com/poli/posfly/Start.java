package com.poli.posfly;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Start extends Fragment {

    private TabLayout tabs;

    public Start() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_start, container, false);

        tabs = (TabLayout) view.findViewById(com.poli.posfly.R.id.tabs);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(com.poli.posfly.R.id.frag_container, new EventF(), "fe");
        transaction.commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        //do what you want when tab 0 is selected
                        transaction.replace(com.poli.posfly.R.id.frag_container, new EventF(), "fe");
                        break;
                    case 1:
                        //do what you want when tab 1 is selected
                        transaction.replace(com.poli.posfly.R.id.frag_container, new MuseumF(), "fm");
                        break;
                    case 2:
                        //do what you want when tab 2 is selected
                        transaction.replace(com.poli.posfly.R.id.frag_container, new ProfileF(), "fp");
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}