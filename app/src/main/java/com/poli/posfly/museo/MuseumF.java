package com.poli.posfly.museo;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MuseumF extends Fragment{

    private String URL;
    private String userActual;

    ListView lvMuseum;

    ArrayList<String> data = new ArrayList<>();

    ItemAdapterMuseum adapter;
    List<Museum> itemMuseum= new ArrayList<>();
    MuseumF museumf = this;

    public MuseumF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_museum, container, false);

        URL =getArguments().getString("URL");
        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        userActual =  pref.getString("id_usuario", null);

        lvMuseum = (ListView) view.findViewById(com.poli.posfly.R.id.lvMuseums);

        FloatingActionButton btnNewMuseum = (FloatingActionButton) view.findViewById(com.poli.posfly.R.id.btnNewMuseum);

        if(checkNetwork()) {
            getMuseos();
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Anécdotas actualizadas", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
        }

        btnNewMuseum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fs");
                Bundle args = new Bundle();
                args.putString("URL", URL);
                Fragment newMuseum = new CreateMuseum();
                newMuseum.setArguments(args);
                transaction.replace(com.poli.posfly.R.id.fragment_container, newMuseum, "fce");
                transaction.commit();
            }
        });

        adapter = new ItemAdapterMuseum(museumf, itemMuseum);
        lvMuseum.setAdapter(adapter);

        lvMuseum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Museum museum = (Museum) parent.getItemAtPosition(position);
                Toast.makeText(getActivity().getApplication().getApplicationContext(), museum.getIdMuseo(), Toast.LENGTH_SHORT).show();
            }
        });

        lvMuseum.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Museum museum = (Museum) parent.getItemAtPosition(position);
                data.add(museum.getIdMuseo());
                data.add(museum.getNombre());
                data.add(museum.getFecha());
                data.add(museum.getDescripcion());
                data.add(museum.getAnonimo());
                if(museum.getUsuario().equals(userActual)){
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Opciones de anécdota");
                    final View viewInflated = LayoutInflater.from(getActivity()).inflate(com.poli.posfly.R.layout.edit_dialog, (ViewGroup) getView(), false);
                    Button btnEditOption = (Button) viewInflated.findViewById(com.poli.posfly.R.id.editOption);
                    Button btnDeleteOption = (Button) viewInflated.findViewById(com.poli.posfly.R.id.deleteOption);

                    alertDialog.setView(viewInflated);
                    alertDialog.show();

                    btnEditOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            EditMuseum editMuseumF = new EditMuseum();
                            Bundle args = new Bundle();
                            args.putStringArrayList("data", data);
                            args.putString("URL", URL);
                            editMuseumF.setArguments(args);
                            transaction.addToBackStack("fe");
                            transaction.replace(com.poli.posfly.R.id.fragment_container, editMuseumF, "fee");
                            transaction.commit();
                        }
                    });
                    btnDeleteOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteMuseum(data.get(0));
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "No tienes permiso para editar o eliminar esta anécdota", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return view;
    }

    public void getMuseos() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"get_museums");
                    List<NameValuePair> params = new ArrayList<>();
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    try {
                        JSONArray jsonMuseos = new JSONArray(text);
                        for (int i = 0; i < jsonMuseos.length(); i++) {
                            String idMuseo = String.valueOf(jsonMuseos.getJSONObject(i).get("id_museo"));
                            String nombre = String.valueOf(jsonMuseos.getJSONObject(i).get("nombre"));
                            String fecha = String.valueOf(jsonMuseos.getJSONObject(i).get("fecha"));
                            String descripcion = String.valueOf(jsonMuseos.getJSONObject(i).get("descripcion"));
                            String id_usuario = String.valueOf(jsonMuseos.getJSONObject(i).get("id_usuario"));
                            String anonimo = String.valueOf(jsonMuseos.getJSONObject(i).get("anonimo"));

                            itemMuseum.add(new Museum(idMuseo, nombre, fecha, descripcion, id_usuario, anonimo));
                        }
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                setListViewHeight(lvMuseum);
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
    }

    public void deleteMuseum(final String idMuseo){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"delete_museum");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idMuseo", idMuseo));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            setListViewHeight(lvMuseum);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            MuseumF museumF = new MuseumF();
                            Bundle args = new Bundle();
                            args.putString("URL", URL);
                            museumF.setArguments(args);
                            transaction.replace(com.poli.posfly.R.id.frag_container, museumF, "fe");
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

    public boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}