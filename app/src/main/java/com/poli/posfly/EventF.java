package com.poli.posfly;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class EventF extends Fragment {

    ListView lvEvents;

    ItemAdapter adapter;
    List<Event> itemEvents = new ArrayList<>();
    EventF eventf = this;

    ArrayList<String> data = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public EventF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_event, container, false);

        lvEvents = (ListView) view.findViewById(com.poli.posfly.R.id.lvEvents);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Event");
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton btnNewEvent = (FloatingActionButton) view.findViewById(com.poli.posfly.R.id.btnNewEvent);

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fs");
                transaction.replace(com.poli.posfly.R.id.fragment_container, new CreateEvent(), "fce");
                transaction.commit();
            }
        });

        adapter = new ItemAdapter(eventf, itemEvents);
        lvEvents.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                itemEvents.add(new Event(dataSnapshot.child("key").getValue().toString(), dataSnapshot.child("nombre").getValue().toString(), dataSnapshot.child("fecha").getValue().toString(), dataSnapshot.child("hora").getValue().toString(),
                            dataSnapshot.child("lugar").getValue().toString(), dataSnapshot.child("descripcion").getValue().toString(), dataSnapshot.child("precio").getValue().toString(),
                            Double.parseDouble(dataSnapshot.child("calificacion").getValue().toString()), null, dataSnapshot.child("creador").getValue().toString()));
                //Collections.reverse(itemEvents);
                adapter.notifyDataSetChanged();
                setListViewHeight(lvEvents);

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

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", String.valueOf(position));
            }
        });

        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                data.add(event.getKey());
                data.add(event.getNombre());
                data.add(event.getFecha());
                data.add(event.getHora());
                data.add(event.getLugar());
                data.add(event.getDescripcion());
                data.add(event.getPrecio());
                if(event.getCreador().equals(mAuth.getCurrentUser().getEmail())){
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Opciones de evento");
                    final View viewInflated = LayoutInflater.from(getActivity()).inflate(com.poli.posfly.R.layout.edit_dialog, (ViewGroup) getView(), false);
                    Button btnEditOption = (Button) viewInflated.findViewById(com.poli.posfly.R.id.editOption);
                    Button btnDeleteOption = (Button) viewInflated.findViewById(com.poli.posfly.R.id.deleteOption);

                    alertDialog.setView(viewInflated);
                    alertDialog.show();

                    btnEditOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            EditEvent editEventF = new EditEvent();
                            Bundle args = new Bundle();
                            args.putStringArrayList("data", data);
                            editEventF.setArguments(args);
                            transaction.addToBackStack("fe");
                            transaction.replace(com.poli.posfly.R.id.fragment_container, editEventF, "fee");
                            transaction.commit();
                        }
                    });
                    btnDeleteOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child(data.get(0)).setValue(null);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Evento eliminado", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "No tienes permiso para editar o eliminar este evento", Toast.LENGTH_SHORT).show();
                }
                return true;
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