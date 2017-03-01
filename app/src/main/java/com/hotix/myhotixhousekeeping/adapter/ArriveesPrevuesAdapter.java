package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.model.ClientPrevue;

import java.util.ArrayList;

public class ArriveesPrevuesAdapter extends BaseAdapter {
    ArrayList<ClientPrevue> listCP;
    LayoutInflater layoutInflater;

    public ArriveesPrevuesAdapter(Context context,
                                  ArrayList<ClientPrevue> listCP) {
        this.listCP = listCP;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listCP.size();
    }

    @Override
    public Object getItem(int position) {
        return listCP.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_resident, null);
            holder = new ViewHolder();
            holder.nomView = (TextView) convertView
                    .findViewById(R.id.nom_resident);
            holder.roomView = (TextView) convertView
                    .findViewById(R.id.num_chb_resident);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nomView.setText(listCP.get(position).getPRENOM() + " "
                + listCP.get(position).getNOM());
        holder.roomView.setText("Chambre : " + listCP.get(position).getROOM());
        return convertView;
    }

    static class ViewHolder {
        TextView nomView;
        TextView roomView;
    }
}
