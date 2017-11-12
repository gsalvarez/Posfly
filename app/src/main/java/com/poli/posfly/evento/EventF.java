package com.poli.posfly.evento;

import android.app.AlertDialog;
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
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class EventF extends Fragment {

    private String URL;
    private String userActual;

    ListView lvEvents;

    ItemAdapter adapter;
    List<Event> itemEvents = new ArrayList<>();
    EventF eventf = this;

    ArrayList<String> data = new ArrayList<>();

    public EventF() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(com.poli.posfly.R.layout.fragment_event, container, false);

        URL = getArguments().getString("URL");
        SharedPreferences pref = getActivity().getApplication().getApplicationContext().getSharedPreferences("MyPref", 0);
        userActual =  pref.getString("id_usuario", null);

        lvEvents = (ListView) view.findViewById(com.poli.posfly.R.id.lvEvents);
        FloatingActionButton btnNewEvent = (FloatingActionButton) view.findViewById(com.poli.posfly.R.id.btnNewEvent);

        if(checkNetwork()) {
            getEventos();
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Eventos actualizados", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show();
        }

        btnNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack("fs");
                Bundle args = new Bundle();
                args.putString("URL", URL);
                Fragment newEvent = new CreateEvent();
                newEvent.setArguments(args);
                transaction.replace(com.poli.posfly.R.id.fragment_container, newEvent, "fce");
                transaction.commit();
            }
        });

        adapter = new ItemAdapter(eventf, itemEvents);
        lvEvents.setAdapter(adapter);

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", String.valueOf(position));
            }
        });

        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                data.add(event.getNombre());
                data.add(event.getFecha());
                data.add(event.getHora());
                data.add(event.getLugar());
                data.add(event.getDescripcion());
                data.add(event.getPrecio());
                if(event.getId_usuario().equals(userActual)){
                    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Opciones de evento");
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
                            EditEvent editEventF = new EditEvent();
                            Bundle args = new Bundle();
                            args.putStringArrayList("data", data);
                            args.putString("URL", URL);
                            editEventF.setArguments(args);
                            transaction.addToBackStack("fe");
                            transaction.replace(com.poli.posfly.R.id.fragment_container, editEventF, "fee");
                            transaction.commit();
                        }
                    });
                    btnDeleteOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteEvent(data.get(0));
                            adapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "No tienes permiso para editar o eliminar este evento", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return view;
    }

    public void getEventos() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"get_events");
                    List<NameValuePair> params = new ArrayList<>();
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    try {
                        JSONArray jsonEventos = new JSONArray(text);
                        for (int i = 0; i < jsonEventos.length(); i++) {
                            String nombre = String.valueOf(jsonEventos.getJSONObject(i).get("nombre"));
                            String fecha = String.valueOf(jsonEventos.getJSONObject(i).get("fecha"));
                            String hora = String.valueOf(jsonEventos.getJSONObject(i).get("hora"));
                            String lugar = String.valueOf(jsonEventos.getJSONObject(i).get("lugar"));
                            String descripcion = String.valueOf(jsonEventos.getJSONObject(i).get("descripcion"));
                            String precio = String.valueOf(jsonEventos.getJSONObject(i).get("precio"));
                            String calificacion = String.valueOf(jsonEventos.getJSONObject(i).get("calificacion"));
                            String id_usuario = String.valueOf(jsonEventos.getJSONObject(i).get("id_usuario"));

                            itemEvents.add(new Event (nombre, fecha, hora, lugar, descripcion, precio, calificacion, id_usuario));
                        }
                        Handler h = new Handler(Looper.getMainLooper());
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                setListViewHeight(lvEvents);
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

    public void deleteEvent(final String nombreEvento){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(URL+"delete_event");
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("nombre", nombreEvento));
                    httppost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse resp = httpclient.execute(httppost);
                    HttpEntity ent = resp.getEntity();
                    final String text = EntityUtils.toString(ent);

                    Handler h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            setListViewHeight(lvEvents);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            EventF eventF = new EventF();
                            Bundle args = new Bundle();
                            args.putString("URL", URL);
                            eventF.setArguments(args);
                            transaction.replace(com.poli.posfly.R.id.frag_container, eventF, "fe");
                            transaction.commit();
                            Toast.makeText(getActivity().getApplication().getApplicationContext(), "Evento eliminado", Toast.LENGTH_SHORT).show();
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