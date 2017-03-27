package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.FemmesMenage;

import java.util.List;

public class FemmeMenageAdapter extends BaseAdapter {
    List<FemmesMenage> listFM;
    LayoutInflater layoutInflater;

    public FemmeMenageAdapter(Context context, List<FemmesMenage> listFM) {
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
        holder.nomView.setText(listFM.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView nomView;

    }

}
