package com.poli.posfly.evento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditEvent extends Fragment{

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener hour;
    private ProgressDialog progressDialog;

    private ArrayList<String> data = new ArrayList<>();
    private String key;
    private String info = "";
    private String sName;
    private String sDate;
    private String sHour;
    private String sPlace;
    private String sDescription;
    private String sPrice;

    private EditText txtName;
    private EditText txtDate;
    private EditText txtHour;
    private EditText txtPlace;
    private EditText txtDescription;
    private EditText txtPrice;


    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public EditEvent () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_edit_event, container, false);

        data = getArguments().getStringArrayList("data");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Event");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditName);
        txtDate = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditDate);
        txtHour = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditHour);
        txtPlace = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditPlace);
        txtDescription = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditDescription);
        txtPrice = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditPrice);
        Button btnEdit = (Button) view.findViewById(com.poli.posfly.R.id.btnEditEvent);

        txtName.setText(data.get(1));
        txtDate.setText(data.get(2));
        txtHour.setText(data.get(3));
        txtPlace.setText(data.get(4));
        txtDescription.setText(data.get(5));
        txtPrice.setText(data.get(6));

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTxtDate();
            }
        };
        hour = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minutes) {
                myCalendar.set(Calendar.HOUR, hour);
                myCalendar.set(Calendar.MINUTE, minutes);
                updateTxtHour();
            }
        };

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), hour, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        //Se oprime el botón de crear evento
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = txtName.getText().toString().trim();
                sDate = txtDate.getText().toString().trim();
                sHour = txtHour.getText().toString().toLowerCase().trim();
                sPlace = txtPlace.getText().toString().trim();
                sDescription = txtDescription.getText().toString().trim();
                sPrice = txtPrice.getText().toString().trim();

                progressDialog.setMessage("Editando evento, por favor espera...");
                progressDialog.show();
                if (checkFields(sName, sDate, sHour, sPlace, sDescription, sPrice)) {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Evento editado con éxito", Toast.LENGTH_SHORT).show();
                    mDatabase.child(data.get(0)).setValue(new Event (data.get(0), sName, sDate, sHour, sPlace, sDescription, sPrice, 0, null, mAuth.getCurrentUser().getEmail()));
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                }
                else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        return view;
    }

    //revisa que los campos estén llenos
    public boolean checkFields (String name, String date, String hour, String place, String description, String price) {
        if (name.isEmpty() || date.isEmpty() || hour.isEmpty() || place.isEmpty() || description.isEmpty() || price.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        else {
            info = "Error en la conexión";
            return true;
        }
    }

    //Actualizan el texto en los campos de fecha y hora
    private void updateTxtDate () {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dat = new SimpleDateFormat(myFormat, Locale.US);
        txtDate.setText(dat.format(myCalendar.getTime()));
    }
    private void updateTxtHour () {
        String myFormat = "HH:mm";
        SimpleDateFormat hou = new SimpleDateFormat(myFormat, Locale.US);
        txtHour.setText(hou.format(myCalendar.getTime()));
    }
}