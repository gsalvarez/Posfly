package com.poli.posconflictter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MuseumF extends Fragment{

    ListView lvMuseum;

    ItemAdapterMuseum adapter;
    List<Museum> itemMuseum= new ArrayList<>();
    MuseumF museumf = this;

    private DatabaseReference mDatabase;

    public MuseumF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_museum, container, false);

        lvMuseum = (ListView) view.findViewById(R.id.lvMuseum);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Museum");

        FloatingActionButton btnNewMuseum = (FloatingActionButton) view.findViewById(R.id.btnNewMuseum);

        btnNewMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fs");
                transaction.replace(R.id.fragment_container, new CreateMuseum(), "fce");
                transaction.commit();
            }
        });

        adapter = new ItemAdapterMuseum(museumf, itemMuseum);
        lvMuseum.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                /* FALTA LAYOUT DE ITEM MUSEUM
                itemMuseum.add(new Museum(dataSnapshot.child("nombre").getValue().toString(), dataSnapshot.child("fecha").getValue().toString(),
                                dataSnapshot.child("descripcion").getValue().toString(), dataSnapshot.child("autor").getValue().toString()));
                */

                adapter.notifyDataSetChanged();
                setListViewHeight(lvMuseum);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}