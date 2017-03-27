package com.hotix.myhotixhousekeeping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.RackRoomData;

import java.util.List;

public class CustomGrid extends BaseAdapter {
    List<RackRoomData> listRoom;
    private Context mContext;

    public CustomGrid(Context c, List<RackRoomData> listRoom) {
        mContext = c;
        this.listRoom = listRoom;
    }

    @Override
    public int getCount() {
        return listRoom.size();
    }

    @Override
    public Object getItem(int position) {
        return listRoom.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
        } else {
            grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.textNumChb);
        textView.setText(String.valueOf(listRoom.get(position).getNumChb()));

        switch (listRoom.get(position).getStatutId()) {
            case 1:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.vac_clean);
                }
                break;
            case 2:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.vac_dirty);
                }
                break;
            case 3:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.occ_clean);
                }
                break;
            case 4:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.occ_dirty);
                }
                break;
            case 5:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.expect_dep);
                }
                break;
            case 6:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.day_use);
                }
                break;
            case 7:
                if (listRoom.get(position).isIsAttributed()) {
                    if (listRoom.get(position).getStatutId() == 1)
                        textView.setBackgroundResource(R.drawable.attributed);
                    else
                        textView.setBackgroundResource(R.drawable.attributed_sale);
                } else {
                    textView.setBackgroundResource(R.drawable.out_of_order);
                }
                break;
            case 8:
                if (listRoom.get(position).getStatutId() == 1)
                    textView.setBackgroundResource(R.drawable.attributed);
                else
                    textView.setBackgroundResource(R.drawable.attributed_sale);
                break;
        }

        return grid;
    }
}
