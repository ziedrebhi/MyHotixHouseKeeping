package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.ObjetTrouveData;

import java.util.List;

public class ObjetTrouveAdapter extends BaseAdapter {
    List<ObjetTrouveData> listOC;
    LayoutInflater layoutInflater;
    Context ctx;

    public ObjetTrouveAdapter(Context context, List<ObjetTrouveData> listOC) {
        this.listOC = listOC;
        ctx = context;
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
            convertView = layoutInflater.inflate(R.layout.item_panne, null);
            holder = new ViewHolder();
            holder.lieuView = (TextView) convertView.findViewById(R.id.lieu_objet);
            holder.descView = (TextView) convertView.findViewById(R.id.description_objet);
            holder.dateView = (TextView) convertView.findViewById(R.id.date_objet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lieuView.setText(ctx.getResources().getString(R.string.place) + " : " + listOC.get(position).getLieu());
        holder.descView.setText("Description : " + listOC.get(position).getDescription());
        holder.dateView.setText("Date : " + listOC.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView lieuView;
        TextView descView;
        TextView dateView;
    }

}
