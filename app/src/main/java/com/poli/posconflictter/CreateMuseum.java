package com.poli.posconflictter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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

/**
 * Created by sosa on 10/10/2017.
 */

public class CreateMuseum extends Fragment {
    Calendar myCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private ProgressDialog progressDialog;

    private String info="";
    private String happy="La anécdota fue creada con éxito";
    private String nameM;
    private String dateM;
    private String descriptionM;
    private String authorM;

    private EditText txtNameMuseum;
    private EditText txtDateMuseum;
    private EditText txtDescriptionMuseum;
    private EditText txtAuthor;
    private Button buttonMuseum;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public CreateMuseum(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_create_museum, container, false);

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        mDatabase=database.getReference("Museum");
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getActivity());

        txtNameMuseum=(EditText)view.findViewById(R.id.txtNameMuseum);
        txtDateMuseum=(EditText)view.findViewById(R.id.txtDateMuseum);
        txtDescriptionMuseum=(EditText)view.findViewById(R.id.txtDescriptionM);
        txtAuthor=(EditText)view.findViewById(R.id.txtAuthorMuseum);
        buttonMuseum=(Button)view.findViewById(R.id.buttonCreateMuseum);

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTxtDateMuseum();
            }
        };


        txtDateMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Se oprime el botón de crear Anecdota
        buttonMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameM = txtNameMuseum.getText().toString().trim();
                dateM = txtDateMuseum.getText().toString().trim();
                descriptionM = txtDescriptionMuseum.getText().toString().trim();
                authorM=txtAuthor.getText().toString().trim();

                progressDialog.setMessage("Creando anécdota, por favor espera...");
                progressDialog.show();
                if (checkFieldsWithAuthor(nameM, dateM, descriptionM,authorM)) {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), happy , Toast.LENGTH_SHORT).show();
                    mDatabase.child(mDatabase.push().getKey()).setValue(new Museum (nameM, dateM, descriptionM, authorM));
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                }else if(checkFields(nameM,dateM,descriptionM)) {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(),happy, Toast.LENGTH_SHORT).show();
                    mDatabase.child(mDatabase.push().getKey()).setValue(new Museum(nameM, dateM, descriptionM,null));
                    FragmentManager fragmentManager=getFragmentManager();
                    fragmentManager.popBackStackImmediate();
                } else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(),info, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        return view;
    }

    //revisa que los campos estén llenos cuando el autor no es anónimo
    public boolean checkFieldsWithAuthor (String name, String date,  String description, String author) {
        if (name.isEmpty() || date.isEmpty() || description.isEmpty() || author.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        else {
            info = "Error en la conexión, vuelva a intentarlo";
            return true;
        }
    }

    public boolean checkFields(String name, String date, String description){
        if(name.isEmpty()||date.isEmpty()||description.isEmpty()){
            info="Todos los campos deben estar llenos";
            return false;
        }else{
            info="Error en la conexión. Vuelva a intentarlo";
            return true;
        }
    }

    //Actualizan el texto en los campos de fecha
    private void updateTxtDateMuseum () {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dat = new SimpleDateFormat(myFormat, Locale.US);
        txtDateMuseum.setText(dat.format(myCalendar.getTime()));
    }


}
