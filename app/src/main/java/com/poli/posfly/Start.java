package com.poli.posfly;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poli.posfly.evento.EventF;
import com.poli.posfly.museo.MuseumF;
import com.poli.posfly.perfil.ProfileF;

public class Start extends Fragment {

    Bundle args = new Bundle();
    private TabLayout tabs;

    public Start() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_start, container, false);

        String URL = getArguments().getString("URL");
        tabs = (TabLayout) view.findViewById(com.poli.posfly.R.id.tabs);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        args.putString("URL", URL);
        Fragment eventF = new EventF();
        eventF.setArguments(args);
        transaction.add(com.poli.posfly.R.id.frag_container, eventF, "fe");
        transaction.commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        Fragment eventFTab = new EventF();
                        eventFTab.setArguments(args);
                        transaction.replace(com.poli.posfly.R.id.frag_container, eventFTab, "fe");
                        break;
                    case 1:
                        Fragment museumFTab = new MuseumF();
                        museumFTab.setArguments(args);
                        transaction.replace(com.poli.posfly.R.id.frag_container, museumFTab, "fm");
                        break;
                    case 2:
                        Fragment profileFTab = new ProfileF();
                        profileFTab.setArguments(args);
                        transaction.replace(com.poli.posfly.R.id.frag_container, profileFTab, "fp");
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