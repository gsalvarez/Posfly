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
import android.content.SharedPreferences;
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

public class CreateEvent extends Fragment{

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener hour;
    private ProgressDialog progressDialog;

    private String URL;
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

    public CreateEvent () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_create_event, container, false);

        URL = getArguments().getString("URL");
        progressDialog = new ProgressDialog(getActivity());

        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtNameE);
        txtDate = (EditText) view.findViewById(com.poli.posfly.R.id.txtDate);
        txtHour = (EditText) view.findViewById(com.poli.posfly.R.id.txtHour);
        txtPlace = (EditText) view.findViewById(com.poli.posfly.R.id.txtPlace);
        txtDescription = (EditText) view.findViewById(com.poli.posfly.R.id.txtDescription);
        txtPrice = (EditText) view.findViewById(com.poli.posfly.R.id.txtPrice);
        Button btnCreate = (Button) view.findViewById(com.poli.posfly.R.id.btnCreateE);

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
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Creando evento, por favor espera...");
                progressDialog.show();
                if(checkNetwork()){
                    sName = txtName.getText().toString().trim();
                    sDate = txtDate.getText().toString().trim();
                    sHour = txtHour.getText().toString().toLowerCase().trim();
                    sPlace = txtPlace.getText().toString().trim();
                    sDescription = txtDescription.getText().toString().trim();
                    sPrice = txtPrice.getText().toString().trim();


                    if (checkFields(sName, sDate, sHour, sPlace, sDescription, sPrice)) {
                        postNewEvent(sName, sPlace, sDate, sHour, sDescription, sPrice);
                    }
                    else {
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }

    public void postNewEvent (final String nombre, final String lugar, final String fecha, final String hora, final String descripcion, final String precio){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"new_event.php");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("nombre", nombre));
                    params.add(new BasicNameValuePair("lugar", lugar));
                    params.add(new BasicNameValuePair("fecha", fecha));
                    params.add(new BasicNameValuePair("hora", hora));
                    params.add(new BasicNameValuePair("descripcion", descripcion));
                    params.add(new BasicNameValuePair("precio", precio));
                    params.add(new BasicNameValuePair("idUsuario", getID()));
                    params.add(new BasicNameValuePair("calificacion", "0"));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("TAG", "entra al ultimo");
                            switch (text) {
                                case "Evento creado con éxito":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStackImmediate();
                                    break;
                                case "Este evento ya existe":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    break;
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

    public String getID(){
        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("id_usuario", null);
    }
}