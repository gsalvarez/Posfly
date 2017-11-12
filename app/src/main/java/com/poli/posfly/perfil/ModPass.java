package com.poli.posfly.perfil;

import android.app.FragmentManager;
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

public class ModPass extends Fragment {
    private String URL;
    private String info;
    private String user;

    private String oldPass;
    private String newPass;
    private String newRepass;

    private TextView tvOldPass;
    private TextView tvNewPass;
    private TextView tvNewRepass;

    public ModPass() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_modify_pass, container, false);

        URL = getArguments().getString("URL");

        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        user = pref.getString("id_usuario", null);

        tvOldPass = (TextView) view.findViewById(R.id.txtOldPass);
        tvNewPass = (TextView) view.findViewById(R.id.txtNewPass);
        tvNewRepass = (TextView) view.findViewById(R.id.txtNewRepass);

        Button btnUpPass = (Button) view.findViewById(R.id.btnUpPass);

        btnUpPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = tvOldPass.getText().toString().trim();
                newPass = tvNewPass.getText().toString().trim();
                newRepass = tvNewRepass.getText().toString().trim();

                if (checkFields(oldPass, newPass, newRepass)) {
                    oldPass = encodePass(oldPass);
                    newPass = encodePass(newPass);
                    if (checkNetwork()){
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    HttpClient httpclient = new DefaultHttpClient();
                                    HttpPost httppost = new HttpPost(URL+"modify_pass");
                                    List<NameValuePair> params = new ArrayList<>();
                                    params.add(new BasicNameValuePair("idUsuario", user));
                                    params.add(new BasicNameValuePair("pass", oldPass));
                                    params.add(new BasicNameValuePair("newPass", newPass));
                                    httppost.setEntity(new UrlEncodedFormEntity(params));
                                    HttpResponse resp = httpclient.execute(httppost);
                                    HttpEntity ent = resp.getEntity();
                                    final String text = EntityUtils.toString(ent);

                                    Handler h = new Handler(Looper.getMainLooper());
                                    h.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.popBackStackImmediate();
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
                    } else {
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean checkFields(String oldPass, String newPass, String newRepass) {
        if (oldPass.isEmpty() || newPass.isEmpty() || newRepass.isEmpty()) {
            info = "Todos los campos deben estar llenos";
            return false;
        }
        else if  (encodePass(oldPass).equals(encodePass(newPass))) {
            info = "Ya estás usando la contraseña";
            return false;
        }
        else if (!newPass.equals(newRepass)) {
            info = "Las nuevas contraseñas no coinciden";
            return false;
        }
        return true;
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

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}