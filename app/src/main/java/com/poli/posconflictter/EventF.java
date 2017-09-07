package com.poli.posconflictter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventF extends Fragment {

    ListView lvEvents;

    ItemAdapter adapter;
    List<Event> itemEvents = new ArrayList<>();
    EventF eventf = this;

    private DatabaseReference mDatabase;

    public EventF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        lvEvents = (ListView) view.findViewById(R.id.lvEvents);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Event");

        FloatingActionButton btnNewEvent = (FloatingActionButton) view.findViewById(R.id.btnNewEvent);

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fs");
                transaction.replace(R.id.fragment_container, new CreateEvent(), "fce");
                transaction.commit();
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    itemEvents.add(new Event(data.child("nombre").getValue().toString(), data.child("fecha").getValue().toString(), data.child("hora").getValue().toString(),
                            data.child("lugar").getValue().toString(), data.child("descripcion").getValue().toString(), data.child("precio").getValue().toString(),
                            Double.parseDouble(data.child("calificacion").getValue().toString()), null));
                }
                updateListView();
                Log.d("TAG", itemEvents.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        updateListView();

        return view;
    }

    public void updateListView(){
        adapter = new ItemAdapter(eventf, itemEvents);
        lvEvents.setAdapter(adapter);
    }
}