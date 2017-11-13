package com.poli.posfly.evento;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<Event> items;

    public ItemAdapter(EventF context, List<Event> items) {
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

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(com.poli.posfly.R.layout.event_item, parent, false);
        }

        // Set data into the view
        TextView txtName = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtNameEv);
        TextView txtPlace = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtPlaceEv);
        TextView txtDate = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtDateEv);
        TextView txtHour = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtHourEv);
        TextView txtDesc = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtDescEv);

        Event item = this.items.get(position);
        txtName.setText(item.getNombre());
        txtPlace.setText(String.valueOf(item.getLugar()));
        txtDate.setText(String.valueOf(item.getFecha()));
        txtHour.setText(String.valueOf(item.getHora()));
        txtDesc.setText(String.valueOf(item.getDescripcion()));

        rowView.setTag(item.getNombre());
        return rowView;
    }
}