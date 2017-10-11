package com.poli.posconflictter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sosa on 11/10/2017.
 */

public class ItemAdapterMuseum extends BaseAdapter {
    private Context context;
    private List<Museum> items;

    public ItemAdapterMuseum(MuseumF context, List<Museum> items) {
        this.context = context.getActivity().getApplication().getApplicationContext();
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    //@Override
    //public View getView(int position, View convertView, ViewGroup parent) {

      //  View rowView = convertView;

    //    if (convertView == null) {
      //      // Create a new view into the list.
        //    LayoutInflater inflater = (LayoutInflater) context
        //            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         //   rowView = inflater.inflate(R.layout.event_item, parent, false);
       // }

        // Set data into the view.
       // TextView txtName = (TextView) rowView.findViewById(R.id.txtNameEv);
       // TextView txtPlace = (TextView) rowView.findViewById(R.id.txtPlaceEv);
       // TextView txtDate = (TextView) rowView.findViewById(R.id.txtDateEv);
       // TextView txtHour = (TextView) rowView.findViewById(R.id.txtHourEv);
       // TextView txtDesc = (TextView) rowView.findViewById(R.id.txtDescEv);

        //Event item = this.items.get(position);
        //txtName.setText(item.getNombre());
        //txtPlace.setText(String.valueOf(item.getLugar()));
      //  txtDate.setText(String.valueOf(item.getFecha()));
    //    txtHour.setText(String.valueOf(item.getHora()));
  //      txtDesc.setText(String.valueOf(item.getDescripcion()));
//
        //rowView.setTag(item.getNombre());
      //  return rowView;
    //}
}
