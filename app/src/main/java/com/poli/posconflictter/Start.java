package com.poli.posconflictter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.util.Log;
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
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        tabs = (TabLayout) view.findViewById(R.id.tabs);

        Log.d("TAG", String.valueOf(tabs.getChildCount()));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TAG", "onTabChanged: tab number=" + tabs.getSelectedTabPosition());
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (tabs.getSelectedTabPosition()) {
                    case 0:
                        //do what you want when tab 0 is selected
                        transaction.replace(R.id.frag_container, new EventF(), "fe");
                        transaction.commit();
                        break;
                    case 1:
                        //do what you want when tab 1 is selected
                        transaction.replace(R.id.frag_container, new Museum(), "fm");
                        transaction.commit();
                        break;
                    case 2:
                        //do what you want when tab 2 is selected
                        transaction.replace(R.id.frag_container, new Profile(), "fp");
                        transaction.commit();
                        break;

                    default:

                        break;
                }
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