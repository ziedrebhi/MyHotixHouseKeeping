package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.ArriveePrevuData;

import java.util.List;

public class ArriveesPrevuesAdapter extends BaseAdapter {
    List<ArriveePrevuData> listCP;
    LayoutInflater layoutInflater;
    Context ctx;

    public ArriveesPrevuesAdapter(Context context,
                                  List<ArriveePrevuData> listCP) {
        this.listCP = listCP;
        ctx = context;
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
            holder.commentView = (TextView) convertView
                    .findViewById(R.id.txt_comment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nomView.setText(listCP.get(position).getClient());
        holder.roomView.setText(ctx.getResources().getString(R.string.chambre) + " : " + listCP.get(position).getRoom());
        holder.commentView.setText(listCP.get(position).getComment());
        return convertView;
    }

    static class ViewHolder {
        TextView nomView;
        TextView roomView;
        TextView commentView;
    }
}
