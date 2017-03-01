package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.model.ObjetCloture;

import java.util.ArrayList;

public class ObjetClotureAdapter extends BaseAdapter {
    ArrayList<ObjetCloture> listOC;
    LayoutInflater layoutInflater;

    public ObjetClotureAdapter(Context context, ArrayList<ObjetCloture> listOC) {
        this.listOC = listOC;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listOC.size();
    }

    @Override
    public Object getItem(int position) {
        return listOC.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_objet_trouve, null);
            holder = new ViewHolder();
            holder.lieuView = (TextView) convertView.findViewById(R.id.lieu_objet);
            holder.descView = (TextView) convertView.findViewById(R.id.description_objet);
            holder.dateView = (TextView) convertView.findViewById(R.id.date_objet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lieuView.setText("Lieu : " + listOC.get(position).getLieu());
        holder.descView.setText("Description : " + listOC.get(position).getDescription());
        holder.dateView.setText("Date : " + listOC.get(position).getDateOT());
        return convertView;
    }

    static class ViewHolder {
        TextView lieuView;
        TextView descView;
        TextView dateView;
    }

}
