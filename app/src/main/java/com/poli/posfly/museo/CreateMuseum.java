package com.poli.posfly.museo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.poli.posfly.R;

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

public class CreateMuseum extends Fragment {
    Calendar myCalendar=Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private ProgressDialog progressDialog;

    private String URL;

    private String info="";
    private String nameM;
    private String dateM;
    private String descriptionM;
    private String anonimoM;

    private EditText txtNameMuseum;
    private EditText txtDateMuseum;
    private EditText txtDescriptionMuseum;
    private CheckBox cbAnonimo;

    public CreateMuseum(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(com.poli.posfly.R.layout.fragment_create_museum, container, false);

        URL = getArguments().getString("URL");

        progressDialog=new ProgressDialog(getActivity());

        txtNameMuseum=(EditText)view.findViewById(com.poli.posfly.R.id.txtNameMuseum);
        txtDateMuseum=(EditText)view.findViewById(com.poli.posfly.R.id.txtDateMuseum);
        txtDescriptionMuseum=(EditText)view.findViewById(com.poli.posfly.R.id.txtDescriptionM);
        cbAnonimo = (CheckBox) view.findViewById(R.id.cbAnonimo);
        Button buttonMuseum=(Button)view.findViewById(com.poli.posfly.R.id.buttonCreateMuseum);

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
                anonimoM = String.valueOf(cbAnonimo.isChecked());

                progressDialog.setMessage("Creando anécdota, por favor espera...");
                progressDialog.show();
                if (checkFields(nameM, dateM, descriptionM)) {
                    if(checkNetwork()) {

                        postNewMuseum(nameM, dateM, descriptionM, anonimoM);
                    }
                    else{
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(),info, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

        return view;
    }

    public void postNewMuseum(final String nameM, final String dateM, final String descriptionM, final String anonimoM) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"new_museum");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("nombre", nameM));
                    params.add(new BasicNameValuePair("fecha", dateM));
                    params.add(new BasicNameValuePair("descripcion", descriptionM));
                    params.add(new BasicNameValuePair("anonimo", anonimoM));
                    params.add(new BasicNameValuePair("idUsuario", getID()));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            switch (text) {
                                case "Anécdota creada con éxito":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStackImmediate();
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

    //revisa que los campos estén llenos cuando el autor no es anónimo
    public boolean checkFields (String name, String date,  String description) {
        if (name.isEmpty() || date.isEmpty() || description.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        return true;
    }

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Actualizan el texto en los campos de fecha
    private void updateTxtDateMuseum () {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat dat = new SimpleDateFormat(myFormat, Locale.US);
        txtDateMuseum.setText(dat.format(myCalendar.getTime()));
    }

    public String getID(){
        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        return pref.getString("id_usuario", null);
    }
}
