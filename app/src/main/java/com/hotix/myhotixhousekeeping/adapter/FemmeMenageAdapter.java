package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.model.FMenage;

import java.util.ArrayList;

public class FemmeMenageAdapter extends BaseAdapter {
    ArrayList<FMenage> listFM;
    LayoutInflater layoutInflater;

    public FemmeMenageAdapter(Context context, ArrayList<FMenage> listFM) {
        this.listFM = listFM;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listFM.size();
    }

    @Override
    public Object getItem(int position) {
        return listFM.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_fm, null);
            holder = new ViewHolder();
            holder.nomView = (TextView) convertView.findViewById(R.id.nomFM);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nomView.setText(listFM.get(position).getNom() + " "
                + listFM.get(position).getPrenom());
        return convertView;
    }

    static class ViewHolder {
        TextView nomView;

    }

}
