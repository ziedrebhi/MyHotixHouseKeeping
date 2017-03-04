package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.model.Etage;

import java.util.ArrayList;

public class CustomFilter extends BaseAdapter {
    ArrayList<Etage> listEtage;
    private Context mContext;

    public CustomFilter(Context c, ArrayList<Etage> listEtage) {
        mContext = c;
        this.listEtage = listEtage;
    }

    @Override
    public int getCount() {
        return listEtage.size();
    }

    @Override
    public Object getItem(int position) {
        return listEtage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.filter_single, null);
        } else {
            grid = (View) convertView;
        }
        CheckBox cbxView = (CheckBox) grid.findViewById(R.id.cbxEtage);
        cbxView.setText(String.valueOf(listEtage.get(position).getLBLETAGE()));
        cbxView.setChecked(listEtage.get(position).getCHECKED());
        cbxView.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) listEtage.get(position).setCHECKED(true);
                else listEtage.get(position).setCHECKED(false);
            }
        });

        return grid;
    }
}
