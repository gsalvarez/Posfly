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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ProfileF extends Fragment {
    private String URL;
    private String info = "";
    private String userActual;
    private String name = "";
    private String lastname = "";
    private String correo = "";

    private EditText tvName;
    private EditText tvLast;
    private EditText tvCorreo;
    private Button btnAccept;
    private Button btnEdit;
    private Button btnCancel;

    public ProfileF() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_profile, container, false);

        URL = getArguments().getString("URL");

        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        userActual = pref.getString("id_usuario", null);

        tvName = (EditText) view.findViewById(R.id.name);
        tvLast = (EditText) view.findViewById(R.id.last);
        tvCorreo = (EditText) view.findViewById(R.id.mail);

        if (checkNetwork()){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(URL+"get_profile");
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("idUsuario", userActual));
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
                                    tvName.setText(name);
                                    tvLast.setText(lastname);
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
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnAccept = (Button) view.findViewById(R.id.btnAcceptEdit);
        btnCancel = (Button) view.findViewById(R.id.btnCancelEdit);

        btnAccept.setAlpha(0);
        btnAccept.setClickable(false);
        btnCancel.setAlpha(0);
        btnCancel.setClickable(false);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAccept.setAlpha(1);
                btnCancel.setAlpha(1);
                btnEdit.setAlpha(0);
                btnAccept.setClickable(true);
                btnCancel.setClickable(true);
                btnEdit.setClickable(false);

                tvName.setEnabled(true);
                tvLast.setEnabled(true);
                tvCorreo.setEnabled(true);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAccept.setAlpha(0);
                btnCancel.setAlpha(0);
                btnEdit.setAlpha(1);
                btnAccept.setClickable(false);
                btnCancel.setClickable(false);
                btnEdit.setClickable(true);

                tvName.setEnabled(false);
                tvLast.setEnabled(false);
                tvCorreo.setEnabled(false);
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = tvName.getText().toString().trim();
                lastname = tvLast.getText().toString().trim();
                correo = tvCorreo.getText().toString().trim();

                if(checkFields(name, lastname, correo)){
                    if(checkNetwork()){
                        modifyProfile(name, lastname, correo);
                        btnAccept.setAlpha(0);
                        btnCancel.setAlpha(0);
                        btnEdit.setAlpha(1);
                        btnAccept.setClickable(false);
                        btnCancel.setClickable(false);
                        btnEdit.setClickable(true);

                        tvName.setEnabled(false);
                        tvLast.setEnabled(false);
                        tvCorreo.setEnabled(false);
                    }
                    else{
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                }


            }
        });

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

    private void modifyProfile (final String nombre, final String apellido, final String email) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"modify_profile");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idUsuario", userActual));
                    params.add(new BasicNameValuePair("nombre", nombre));
                    params.add(new BasicNameValuePair("apellido", apellido));
                    params.add(new BasicNameValuePair("correo", email));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            btnAccept.setAlpha(0);
                            btnCancel.setAlpha(0);
                            btnEdit.setAlpha(1);
                            btnAccept.setClickable(false);
                            btnCancel.setClickable(false);
                            btnEdit.setClickable(true);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            ProfileF profileF = new ProfileF();
                            Bundle args = new Bundle();
                            args.putString("URL", URL);
                            profileF.setArguments(args);
                            transaction.replace(com.poli.posfly.R.id.frag_container, profileF, "fe");
                            transaction.commit();
                            Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();

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

    private boolean checkFields(String name, String lastname, String correo) {
        if (name.isEmpty() || lastname.isEmpty() || correo.isEmpty()) {
            info = "Todos los campos deben estar llenos";
            return false;
        }
        return true;
    }

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}