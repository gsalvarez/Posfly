package com.poli.posfly.usuario;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.poli.posfly.R;
import com.poli.posfly.Start;

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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Login extends Fragment {

    private String URL;
    private String user;
    private String pass;

    private EditText txtUser;
    private EditText txtPass;

    private ProgressDialog progressDialog;

    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        URL = getArguments().getString("URL");
        progressDialog = new ProgressDialog(getActivity());

        TextView tvReg = (TextView) view.findViewById(R.id.tvReg);
        TextView tvForg = (TextView) view.findViewById(R.id.tvForgot);
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtUser = (EditText) view.findViewById(R.id.txtUserL);
        txtPass = (EditText) view.findViewById(R.id.txtPassL);

        //Click en el botón de registro lleva al fragmento de registro
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fi");
                Bundle args = new Bundle();
                args.putString("URL", URL);
                Fragment fg = new Register();
                fg.setArguments(args);
                transaction.replace(R.id.fragment_container, fg, "fr");
                transaction.commit();
            }
        });
        //Click en el botón de olvidé contraseña lleva al fragmento de recuperar contraseña
        tvForg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fi");
                transaction.replace(R.id.fragment_container, new Forgot(), "ff");
                transaction.commit();
            }
        });

        //Click en el botón de Login valida la información para iniciar sesión
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = txtUser.getText().toString();
                pass = txtPass.getText().toString();

                progressDialog.setMessage("Iniciando sesión, por favor espera...");
                progressDialog.show();
                if (checkNetwork()){
                    pass = encodePass(pass);
                    tryLogin();
                } else {
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }

    //Método que verifica el correo y la contraseña ingresada (Campos vacios, correo valido, datos en Firebase)
    public void tryLogin() {
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Hay campos vacíos", Toast.LENGTH_SHORT).show();
        } else {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(URL+"login.php");
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("idUsuario", user));
                        params.add(new BasicNameValuePair("pass", pass));
                        httppost.setEntity(new UrlEncodedFormEntity(params));
                        HttpResponse resp = httpclient.execute(httppost);
                        HttpEntity ent = resp.getEntity();
                        final String text = EntityUtils.toString(ent);

                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (text) {
                                    case "Bienvenido":
                                        Toast.makeText(getActivity().getApplication().getApplicationContext(), text+" "+user, Toast.LENGTH_SHORT).show();
                                        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("id_usuario", user);
                                        editor.commit();
                                        FragmentManager fragmentManager = getFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.fragment_container, new Start(), "fs");
                                        transaction.commit();
                                        break;
                                    case "Credenciales incorrectas":
                                        Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
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
            progressDialog.dismiss();
        }
        progressDialog.dismiss();
    }

    private String encodePass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(pass.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}