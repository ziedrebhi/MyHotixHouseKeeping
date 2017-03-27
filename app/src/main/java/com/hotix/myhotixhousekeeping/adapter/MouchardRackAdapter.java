package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.MouchardRackData;

import java.util.List;

public class MouchardRackAdapter extends BaseAdapter {
    List<MouchardRackData> listOC;
    LayoutInflater layoutInflater;

    public MouchardRackAdapter(Context context, List<MouchardRackData> listOC) {
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
            convertView = layoutInflater.inflate(R.layout.item_mouchard, null);
            holder = new ViewHolder();
            holder.lieuView = (TextView) convertView.findViewById(R.id.lieu_objet);
            holder.actionView = (TextView) convertView.findViewById(R.id.description_objet);
            holder.dateView = (TextView) convertView.findViewById(R.id.date);
            holder.userView = (TextView) convertView.findViewById(R.id.user);
            holder.imageView = (ImageView) convertView.findViewById(R.id.list_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lieuView.setText("Lieu : " + listOC.get(position).getRoom());
        holder.actionView.setText(listOC.get(position).getOperation());
        holder.dateView.setText("Date : " + listOC.get(position).getDate());
        holder.userView.setText("Login : " + listOC.get(position).getUser());
        if (listOC.get(position).getPoste().equals("HNG FRONT")) {
            holder.imageView.setImageResource(R.drawable.mouchard_front);
        } else {
            holder.imageView.setImageResource(R.drawable.mouchard_myhotix);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView lieuView;
        TextView actionView;
        TextView dateView;
        TextView userView;
        ImageView imageView;
    }

}
