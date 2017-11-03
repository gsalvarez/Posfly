package com.poli.posfly.usuario;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Fragment {

    //Patrón de emails
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String URL;
    private String info = "";
    private String sName;
    private String sLastname;
    private String sUser;
    private String sEmail;
    private String sPass;
    private String sRepass;

    private EditText txtName;
    private EditText txtLastname;
    private EditText txtUser;
    private EditText txtEmail;
    private EditText txtPass;
    private EditText txtRepass;
    private ProgressDialog progressDialog;

    public Register () {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_register, container, false);

        URL = getArguments().getString("URL");
        progressDialog = new ProgressDialog(getActivity());

        txtName = (EditText) view.findViewById(com.poli.posfly.R.id.txtName);
        txtLastname = (EditText) view.findViewById(com.poli.posfly.R.id.txtLastname);
        txtUser = (EditText) view.findViewById(com.poli.posfly.R.id.txtUser);
        txtEmail = (EditText) view.findViewById(com.poli.posfly.R.id.txtEmailR);
        txtPass = (EditText) view.findViewById(com.poli.posfly.R.id.txtPassR);
        txtRepass = (EditText) view.findViewById(com.poli.posfly.R.id.txtRepassR);
        Button btnCreate = (Button) view.findViewById(com.poli.posfly.R.id.btnCreate);

        //Al oprimir el botón crear, verifica todos los campos
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Registrando, por favor espera...");
                progressDialog.show();
                if (checkNetwork()){
                    sName = txtName.getText().toString().trim();
                    sLastname = txtLastname.getText().toString().trim();
                    sUser = txtUser.getText().toString().toLowerCase().trim();
                    sEmail = txtEmail.getText().toString().trim();
                    sPass = txtPass.getText().toString().trim();
                    sRepass = txtRepass.getText().toString().trim();

                    if (checkFieldsPasswordsAndEmail(sName, sLastname, sUser, sEmail, sPass, sRepass)) {
                        sPass = encodePass(sPass);
                        postNewUser(sUser, sName, sLastname, sEmail, sPass);
                        progressDialog.dismiss();
                    }
                    else{
                        Toast.makeText(getActivity().getApplication().getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        return view;
    }

    //revisa los campos, las contraseñas, el email
    public boolean checkFieldsPasswordsAndEmail (String name, String lastname, String user, String email, String pass, String rePass) {
        if (name.isEmpty() || lastname.isEmpty() || user.isEmpty() || email.isEmpty() || pass.isEmpty() || rePass.isEmpty()){
            info = "Todos los campos deben estar llenos";
            return false;
        }
        else if (pass.length() < 6) {
            info = "La contraseña debe tener mínimo 6 caracteres";
            return false;
        }
        else if (!pass.equals(rePass)) {
            info = "Las contraseñas no coinciden";
            return false;
        }
        else if (!validateEmail(email)) {
            info = "La dirección de correo no es válida";
            return false;
        }
        else {
            info = "ok";
            return true;
        }
    }

    //Método que verifica el email
    public boolean validateEmail (final String email) {
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Método que ejecuta el método POST para crear usuario en MySQL
    public void postNewUser (final String id_usuario, final String nombre, final String apellido, final String correo, final String pass) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"new_user.php");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idUsuario", id_usuario));
                    params.add(new BasicNameValuePair("nombre", nombre));
                    params.add(new BasicNameValuePair("apellido", apellido));
                    params.add(new BasicNameValuePair("correo", correo));
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
                                case "Cuenta creada con éxito":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.popBackStackImmediate();
                                    break;
                                case "Este usuario ya está en uso":
                                    Toast.makeText(getActivity().getApplication().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    break;
                                case "Este correo ya está en uso":
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