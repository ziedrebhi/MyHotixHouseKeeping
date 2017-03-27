package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.entities.Etage;

import java.util.List;

/**
 * Created by ziedrebhi on 21/03/2017.
 */

public class CustomAdapterSpinnerEtage extends ArrayAdapter<Etage> {
    List<com.hotix.myhotixhousekeeping.entities.Etage> values;
    private Context context;


    public CustomAdapterSpinnerEtage(Context context, int textViewResourceId
            , List<com.hotix.myhotixhousekeeping.entities.Etage> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;

    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Etage getItem(int i) {
        return values.get(i);
    }

    @Override
    public long getItemId(int i) {
        return values.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(i).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = super.getDropDownView(position, convertView, parent);
        TextView tv = ((TextView) v);
        tv.setText(values.get(position).getName());
        tv.setTextColor(Color.BLACK);
        return v;
    }
}