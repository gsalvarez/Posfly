package com.poli.posfly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(com.poli.posfly.R.layout.museum_item, parent, false);
        }

        //Set data into the view.
        TextView txtNameM = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtNameM);
        TextView txtDate = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtDateM);
        TextView txtDescripcionMuseum = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtDescripcionMostar);
        TextView txtAutor = (TextView) rowView.findViewById(com.poli.posfly.R.id.txtAutorMuseo);

        Museum item = this.items.get(position);
        txtNameM.setText(item.getNombre());
        txtDate.setText(String.valueOf(item.getFecha()));
        txtDescripcionMuseum.setText(item.getDescripcion());
        txtAutor.setText(item.getAutor());

        rowView.setTag(item.getNombre());
        return rowView;
    }
}
