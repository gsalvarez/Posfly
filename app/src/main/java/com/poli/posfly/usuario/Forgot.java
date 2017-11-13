package com.poli.posfly.usuario;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot extends Fragment {

    //Patrón de emails
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private String URL;
    private String sMail;
    private EditText txtEmail;
    private EditText txtCodigo;

    public Forgot() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_forgot, container, false);

        URL = getArguments().getString("URL");

        Button btnMail = (Button) view.findViewById(com.poli.posfly.R.id.btnMail);
        Button btnForgot = (Button) view.findViewById(com.poli.posfly.R.id.btnForgot);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmailF);
        txtCodigo = (EditText) view.findViewById(com.poli.posfly.R.id.txtCodigo);

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sMail = txtEmail.getText().toString().trim();
                Log.d("TAG", sMail);

                if (txtEmail.getText().toString().isEmpty()) {
                    Log.d("TAG", "Escribe el correo");
                }
                else if(!validateEmail(txtEmail.getText().toString())) {
                    Log.d("TAG", "Correo no válido");
                }
                else{
                    if(checkNetwork()) {
                        sendMail(sMail);
                    }
                    else{
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void sendMail(final String email) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"forgot");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("correo", email));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);
                    Log.d("TAG", text);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu correo", Toast.LENGTH_SHORT).show();
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

    //Método que verifica que el correo sea válido
    public boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}