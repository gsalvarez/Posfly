package com.poli.posfly.museo;

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
import android.app.Fragment;
import android.content.Context;
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

public class EditMuseum extends Fragment{

    private String URL;

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private ProgressDialog progressDialog;

    private ArrayList<String> data = new ArrayList<>();
    private String info = "";
    private String sName;
    private String sDate;
    private String sDescription;
    private String anonimoME;

    private EditText txtName;
    private EditText txtDate;
    private EditText txtDescription;
    private CheckBox cbAnonimo;

    public EditMuseum () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_edit_museum, container, false);

        URL = getArguments().getString("URL");
        data = getArguments().getStringArrayList("data");

        progressDialog = new ProgressDialog(getActivity());

        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditNameM);
        txtDate = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditDateM);
        txtDescription = (EditText) view.findViewById(com.poli.posfly.R.id.txtEditDescM);
        cbAnonimo = (CheckBox) view.findViewById(R.id.cbAnonimoE);
        Button btnEdit = (Button) view.findViewById(com.poli.posfly.R.id.btnEditMuseum);

        txtName.setText(data.get(1));
        txtDate.setText(data.get(2));
        txtDescription.setText(data.get(3));

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTxtDate();
            }
        };

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Se oprime el botón de crear evento
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sName = txtName.getText().toString().trim();
                sDate = txtDate.getText().toString().trim();
                sDescription = txtDescription.getText().toString().trim();
                anonimoME = String.valueOf(cbAnonimo.isChecked());

                progressDialog.setMessage("Editando anécdota, por favor espera...");
                progressDialog.show();
                if (checkFields(sName, sDate, sDescription)) {
                    if(checkNetwork()) {
                        editMuseum(sName, sDate,sDescription, anonimoME);
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

    public void editMuseum(final String sName, final String sDate, final String sDescription, final String sAnonimo) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"modify_museum");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idMuseo", data.get(0)));
                    params.add(new BasicNameValuePair("nombre", sName));
                    params.add(new BasicNameValuePair("fecha", sDate));
                    params.add(new BasicNameValuePair("descripcion", sDescription));
                    params.add(new BasicNameValuePair("anonimo", sAnonimo));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);
                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            switch(text){
                                case "Anécdota modificada con éxito":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStackImmediate();
                                    break;
                                default:
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Error editando la anécdota", Toast.LENGTH_SHORT).show();
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
    public boolean checkFields (String name, String date, String description) {
        if (name.isEmpty() || date.isEmpty() || description.isEmpty()){
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

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}