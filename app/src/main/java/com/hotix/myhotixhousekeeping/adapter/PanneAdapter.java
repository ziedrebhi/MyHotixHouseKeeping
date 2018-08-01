package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.PanneData;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

public class PanneAdapter extends BaseAdapter {
    List<PanneData> listOC;
    LayoutInflater layoutInflater;
    Context ctx;

    public PanneAdapter(Context context, List<PanneData> listOC) {
        this.listOC = listOC;
        layoutInflater = LayoutInflater.from(context);
        this.ctx = context;
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
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView2);

            holder.descView = (TextView) convertView.findViewById(R.id.description_objet);
            holder.dateView = (TextView) convertView.findViewById(R.id.date_objet);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lieuView.setText(ctx.getResources().getString(R.string.place) + " : " + listOC.get(position).getLieu());
        holder.descView.setText("Description : " + listOC.get(position).getDescription());
        holder.dateView.setText("Date : " + listOC.get(position).getDate());
        if (listOC.get(position).isUrgent()) {
            BadgeView badge4 = new BadgeView(ctx, holder.imageView);
            badge4.setText("Urgent");
            badge4.setTextSize(12);
            badge4.setBadgePosition(BadgeView.POSITION_CENTER);
            badge4.setBadgeBackgroundColor(Color.RED);
            badge4.show();
        }
        return convertView;
    }

    static class ViewHolder {
        TextView lieuView;
        TextView descView;
        TextView dateView;
        ImageView imageView;
    }

}
