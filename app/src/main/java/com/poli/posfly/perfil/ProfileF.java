package com.poli.posfly.perfil;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.poli.posfly.R;
import com.poli.posfly.usuario.Login;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProfileF extends Fragment {
    private String URL;
    private String user;
    private String name = "";
    private String lastname = "";
    private String correo = "";

    private TextView tvName;
    private TextView tvCorreo;

    public ProfileF() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_profile, container, false);

        URL = getArguments().getString("URL");

        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        user = pref.getString("id_usuario", null);

        tvName = (TextView) view.findViewById(R.id.name);
        tvCorreo = (TextView) view.findViewById(R.id.mail);

        if (checkNetwork()){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(URL+"get_profile");
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("idUsuario", user));
                        httppost.setEntity(new UrlEncodedFormEntity(params));
                        HttpResponse resp = httpclient.execute(httppost);
                        HttpEntity ent = resp.getEntity();
                        final String text = EntityUtils.toString(ent);

                        try {
                            JSONObject json = new JSONObject(text);
                            name = String.valueOf(json.get("nombre"));
                            lastname = String.valueOf(json.get("apellido"));
                            correo = String.valueOf(json.get("correo"));
                            Handler h = new Handler(Looper.getMainLooper());
                            h.post(new Runnable() {
                                @Override
                                public void run() {
                                    tvName.setText(String.format("%s %s", name, lastname));
                                    tvCorreo.setText(correo);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        } else {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
        }

        tvName.setText(name);
        tvCorreo.setText(correo);

        Button btnLogout = (Button) view.findViewById(R.id.btnLogout);
        Button btnModifyPass = (Button) view.findViewById(R.id.btnPassword);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment login = new Login();
                Bundle args = new Bundle();
                args.putString("URL", URL);
                login.setArguments(args);
                transaction.replace(com.poli.posfly.R.id.fragment_container, login, "fi");
                transaction.commit();
            }
        });

        btnModifyPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fp");
                Fragment modPass = new ModPass();
                Bundle args = new Bundle();
                args.putString("URL", URL);
                modPass.setArguments(args);
                transaction.replace(com.poli.posfly.R.id.fragment_container, modPass, "fmp");
                transaction.commit();
            }
        });

        return view;
    }

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}