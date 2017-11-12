package com.poli.posfly.evento;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class EditEvent extends Fragment{

    private String URL;

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

        URL = getArguments().getString("URL");
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

        txtName.setText(data.get(0));
        txtDate.setText(data.get(1));
        txtHour.setText(data.get(2));
        txtPlace.setText(data.get(3));
        txtDescription.setText(data.get(4));
        txtPrice.setText(data.get(5));

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
                    if(checkNetwork()) {
                        editEvent(sName, sDate, sHour, sPlace, sDescription, sPrice);
                    }
                    else{
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        return view;
    }

    public void editEvent(final String sName, final String sDate, final String sHour, final String sPlace, final String sDescription, final String sPrice) {
        Log.d("TAG", "Entra a editEvent");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TAG", "Entra a tryPost");
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"modify_event");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("oldNombre", data.get(0)));
                    params.add(new BasicNameValuePair("nombre", sName));
                    params.add(new BasicNameValuePair("lugar", sPlace));
                    params.add(new BasicNameValuePair("fecha", sDate));
                    params.add(new BasicNameValuePair("hora", sHour));
                    params.add(new BasicNameValuePair("descripcion", sDescription));
                    params.add(new BasicNameValuePair("precio", sPrice));
                    params.add(new BasicNameValuePair("calificacion", "0"));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);
                    Log.d("TAG", text);
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            switch(text){
                                case "Evento modificado con éxito":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Evento editado con éxito", Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStackImmediate();
                                    break;
                                default:
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Error editando el evento", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    //revisa que los campos estén llenos
    public boolean checkFields (String name, String date, String hour, String place, String description, String price) {
        if (name.isEmpty() || date.isEmpty() || hour.isEmpty() || place.isEmpty() || description.isEmpty() || price.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        return true;
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

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}