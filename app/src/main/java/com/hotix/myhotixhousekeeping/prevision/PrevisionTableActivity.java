package com.hotix.myhotixhousekeeping.prevision;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.PrevisionData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrevisionTableActivity extends Activity {
    ProgressDialog pd;
    List<PrevisionData> listDataPrevisions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_prevision_table);
        pd = new ProgressDialog(PrevisionTableActivity.this);
        listDataPrevisions = new ArrayList<PrevisionData>();

    }

    @Override
    protected void onResume() {
        showProgressDialog(2);
        Intent intent = getIntent();
        if (null != intent) {
            Bundle extras = intent.getExtras();
            listDataPrevisions = (List<PrevisionData>) extras.get("data");
        }
        Log.i("Data", String.valueOf(listDataPrevisions.size()));
        init();
        super.onResume();
    }

    private void showProgressDialog(int i) {
        if (i == 1)
            pd.setMessage(getResources().getString(R.string.msg_connecting));
        else
            pd.setMessage(getResources().getString(R.string.msg_loading));
        pd.setCancelable(false);
        pd.show();
    }

    public void init() {

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);

 /*
        TableRow tbrow0 = new TableRow(this);
        tbrow0.setBackgroundResource(R.drawable.cell_shape);

        // Date
        TextView tv0 = new TextView(this);
        tv0.setText(getResources().getString(R.string.date));
        tv0.setPadding(5, 5, 5, 5);
        tv0.setGravity(Gravity.CENTER);
        tbrow0.addView(tv0);

        // Capacite
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;
        TextView tv10 = new TextView(this);
        tv10.setText(getResources().getString(R.string.capactite));
        tv10.setPadding(5, 5, 5, 5);
        tv10.setGravity(Gravity.CENTER);
        tbrow0.addView(tv10,params);

        // Recouche
        TextView tv1 = new TextView(this);
        tv1.setText(getResources().getString(R.string.prev_recouche));
        tv1.setPadding(5, 5, 5, 5);
        tv1.setGravity(Gravity.CENTER);
        tbrow0.addView(tv1,params);

        // Arrivées
        TextView tv2 = new TextView(this);
        tv2.setText(getResources().getString(R.string.prev_arrivee));
        tv2.setPadding(5, 5, 5, 5);
        tv2.setGravity(Gravity.CENTER);
        tbrow0.addView(tv2,params);

        // Départs
        TextView tv3 = new TextView(this);
        tv3.setText(getResources().getString(R.string.prev_depart));
        tv3.setPadding(5, 5, 5, 5);
        tv3.setGravity(Gravity.CENTER);
        tbrow0.addView(tv3,params);

        // Total
        TextView tv40 = new TextView(this);
        tv40.setText(getResources().getString(R.string.total));
        tv40.setPadding(5, 5, 5, 5);
        tv40.setGravity(Gravity.CENTER);
        tbrow0.addView(tv40,params);

        // Taux Occ
        TextView tv4 = new TextView(this);
        tv4.setText(getResources().getString(R.string.prev_taux));
        tv4.setPadding(5, 5, 5, 5);
        tv4.setGravity(Gravity.CENTER);
        tbrow0.addView(tv4,params);

        stk.addView(tbrow0);

        // 2é  row
        TableRow tbrow1 = new TableRow(this);
        tbrow1.setBackgroundResource(R.drawable.cell_shape);

        TextView chb1 = new TextView(this);
        chb1.setText(getResources().getString(R.string.chb));
        chb1.setPadding(5, 5, 5, 5);
        chb1.setGravity(Gravity.CENTER);
        tbrow1.addView(chb1,params);

        TextView pax1 = new TextView(this);
        pax1.setText(getResources().getString(R.string.pax));
        pax1.setPadding(5, 5, 5, 5);
        pax1.setGravity(Gravity.CENTER);
        tbrow1.addView(pax1,params);


        TextView chb2 = new TextView(this);
        chb2.setText(getResources().getString(R.string.chb));
        chb2.setPadding(5, 5, 5, 5);
        chb2.setGravity(Gravity.CENTER);
        tbrow1.addView(chb2,params);

        TextView pax2 = new TextView(this);
        pax2.setText(getResources().getString(R.string.pax));
        pax2.setPadding(5, 5, 5, 5);
        pax2.setGravity(Gravity.CENTER);
        tbrow1.addView(pax2,params);

        TextView chb3 = new TextView(this);
        chb3.setText(getResources().getString(R.string.chb));
        chb3.setPadding(5, 5, 5, 5);
        chb3.setGravity(Gravity.CENTER);
        tbrow1.addView(chb3,params);

        TextView pax3 = new TextView(this);
        pax3.setText(getResources().getString(R.string.pax));
        pax3.setPadding(5, 5, 5, 5);
        pax3.setGravity(Gravity.CENTER);
        tbrow1.addView(pax3,params);

        TextView chb4 = new TextView(this);
        chb4.setText(getResources().getString(R.string.chb));
        chb4.setPadding(5, 5, 5, 5);
        chb4.setGravity(Gravity.CENTER);
        tbrow1.addView(chb4,params);

        TextView pax4 = new TextView(this);
        pax4.setText(getResources().getString(R.string.pax));
        pax4.setPadding(5, 5, 5, 5);
        pax4.setGravity(Gravity.CENTER);
        tbrow1.addView(pax4,params);

        TextView chb5 = new TextView(this);
        chb5.setText(getResources().getString(R.string.chb));
        chb5.setPadding(5, 5, 5, 5);
        chb5.setGravity(Gravity.CENTER);
        tbrow1.addView(chb5,params);

        TextView pax5 = new TextView(this);
        pax5.setText(getResources().getString(R.string.pax));
        pax5.setPadding(5, 5, 5, 5);
        pax5.setGravity(Gravity.CENTER);
        tbrow1.addView(pax5,params);

        TextView chb6 = new TextView(this);
        chb6.setText(getResources().getString(R.string.chb));
        chb6.setPadding(5, 5, 5, 5);
        chb6.setGravity(Gravity.CENTER);
        tbrow1.addView(chb6,params);

        TextView pax6 = new TextView(this);
        pax6.setText(getResources().getString(R.string.pax));
        pax6.setPadding(5, 5, 5, 5);
        pax6.setGravity(Gravity.CENTER);
        tbrow1.addView(pax6,params);


        stk.addView(tbrow1);
*/


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();


        for (int i = 0; i < listDataPrevisions.size(); i++) {
            TableRow tbrow = new TableRow(this);

            TextView t1v = new TextView(this);
            try {
                t1v.setText(format.parse(listDataPrevisions.get(i).getDate()).toLocaleString().replace("00:00:00", ""));
            } catch (ParseException e) {
                t1v.setText(listDataPrevisions.get(i).getDate());
            }
            t1v.setBackgroundResource(R.drawable.fact_shape);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v0 = new TextView(this);
            t2v0.setText(listDataPrevisions.get(i).getCapaCHB());
            t2v0.setGravity(Gravity.CENTER);
            t2v0.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t2v0);

            TextView t2v01 = new TextView(this);
            t2v01.setText(listDataPrevisions.get(i).getCapaPax());
            t2v01.setBackgroundResource(R.drawable.fact_shape);
            t2v01.setGravity(Gravity.CENTER);
            tbrow.addView(t2v01);

            TextView t2v = new TextView(this);
            t2v.setText(listDataPrevisions.get(i).getResCHB());
            t2v.setGravity(Gravity.CENTER);
            t2v.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t2v);

            TextView t2v2 = new TextView(this);
            t2v2.setText(listDataPrevisions.get(i).getResPax());
            t2v2.setGravity(Gravity.CENTER);
            t2v2.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t2v2);


            TextView t3v = new TextView(this);
            t3v.setText(listDataPrevisions.get(i).getArrCHB());
            t3v.setGravity(Gravity.CENTER);
            t3v.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t3v);

            TextView t3v2 = new TextView(this);
            t3v2.setText(listDataPrevisions.get(i).getArrPax());
            t3v2.setGravity(Gravity.CENTER);
            t3v2.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t3v2);

            TextView t4v = new TextView(this);
            t4v.setText(listDataPrevisions.get(i).getDepCHB());
            t4v.setGravity(Gravity.CENTER);
            t4v.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t4v);

            TextView t4v2 = new TextView(this);
            t4v2.setText(listDataPrevisions.get(i).getDepPax());
            t4v2.setBackgroundResource(R.drawable.fact_shape);
            t4v2.setGravity(Gravity.CENTER);
            tbrow.addView(t4v2);

            TextView t5v0 = new TextView(this);
            t5v0.setText(listDataPrevisions.get(i).getTotCHB());
            t5v0.setGravity(Gravity.CENTER);
            t5v0.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t5v0);

            TextView t5v02 = new TextView(this);
            t5v02.setText(listDataPrevisions.get(i).getTotPax());
            t5v02.setGravity(Gravity.CENTER);
            t5v02.setBackgroundResource(R.drawable.fact_shape);
            t5v02.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t5v02);

            TextView t5v = new TextView(this);
            t5v.setText(listDataPrevisions.get(i).getOccCHB() + "%");
            t5v.setGravity(Gravity.CENTER);
            t5v.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t5v);

            TextView t5v0d = new TextView(this);
            t5v0d.setText(listDataPrevisions.get(i).getOccPax() + "%");
            t5v0d.setGravity(Gravity.CENTER);
            t5v0d.setBackgroundResource(R.drawable.fact_shape);
            tbrow.addView(t5v0d);

            TextView tv58 = new TextView(this);
            tv58.setText(listDataPrevisions.get(i).getReste());
            tv58.setBackgroundResource(R.drawable.fact_shape);
            tv58.setGravity(Gravity.CENTER);
            tbrow.addView(tv58);

            stk.addView(tbrow);
        }
        pd.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mouchard_table, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.graph:
                finish();
                break;

        }
        return super.onMenuItemSelected(featureId, item);
    }

}
