
package com.hotix.myhotixhousekeeping.prevision;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hotix.myhotixhousekeeping.R;
import com.hotix.myhotixhousekeeping.entities.PrevisionData;
import com.hotix.myhotixhousekeeping.entities.PrevisionModel;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your listview-item
 *
 * @author Philipp Jahoda
 */
public class ListViewMultiChartActivity extends DemoBase {
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    static int y1, y2, m1, m2, d1, d2;
    ListView lv;
    ImageButton refreshBtn;
    EditText dateDeb, dateFin;
    List<PrevisionData> listDataPrevisions;
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date_selected = String.format("%02d", Integer.parseInt(String.valueOf(dayOfMonth))) + "/"
                    + String.format("%02d", Integer.parseInt(String.valueOf(monthOfYear + 1))) + "/"
                    + String.valueOf(year);
            Log.i("PannesListActivity", "here 1" + date_selected);
            dateDeb.setText(date_selected);
            y1 = year;
            m1 = monthOfYear + 1;
            d1 = dayOfMonth;
        }
    };
    private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date_selected = String.format("%02d", Integer.parseInt(String.valueOf(dayOfMonth))) + "/"
                    + String.format("%02d", Integer.parseInt(String.valueOf(monthOfYear + 1))) + "/"
                    + String.valueOf(year);

            dateFin.setText(date_selected);
            y2 = year;
            m2 = monthOfYear + 1;
            d2 = dayOfMonth;
        }
    };
    private Menu mOptionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview_chart);
        refreshBtn = (ImageButton) findViewById(R.id.refresh_liste);
        dateDeb = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        lv = (ListView) findViewById(R.id.listView1);
        listDataPrevisions = new ArrayList<PrevisionData>();
        pd = new ProgressDialog(this);


        String DateFront = UserInfoModel.getInstance().getUser().getData().getDateFront();
        Date date = new Date();
        //23/02/2017
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateAsString = DateFront;
        try {
            date = sourceFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.add(Calendar.DATE, 7);
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);


        dateDeb.setText(String.format("%02d", Integer.parseInt(String.valueOf(day))) + "/"
                + String.format("%02d", Integer.parseInt(String.valueOf(month + 1))) + "/"
                + String.valueOf(year));
        dateFin.setText(String.format("%02d", Integer.parseInt(String.valueOf(day2))) + "/"
                + String.format("%02d", Integer.parseInt(String.valueOf(month2 + 1))) + "/"
                + String.valueOf(year2));

        dateDeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateDeb)
                    showDialog(DATE_DIALOG_ID);

            }
        });

        dateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (v1 == dateFin)
                    showDialog(DATE_DIALOG_ID1);

            }
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ControleDate()) {
                    if (isConnected())
                        new HttpRequestTaskPrev().execute();
                    else ShowAlert(getResources().getString(R.string.acces_denied));

                } else {
                    ShowAlert(getResources().getString(R.string.msg_error_date));
                }

            }
        });

    }

    @Override
    protected void onResume() {
        if (ControleDate()) {
            if (isConnected())
                new HttpRequestTaskPrev().execute();
            else ShowAlert(getResources().getString(R.string.acces_denied));

        } else {
            ShowAlert(getResources().getString(R.string.msg_error_date));
        }
        super.onResume();
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar(PrevisionData prevData) {

        ArrayList<BarEntry> entriesArr = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesDep = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesRec = new ArrayList<BarEntry>();

        entriesArr.add(new BarEntry(0, Float.valueOf(prevData.getArrCHB().replace(',', '.'))));
        entriesRec.add(new BarEntry(1, Float.valueOf(prevData.getResCHB().replace(',', '.'))));
        entriesDep.add(new BarEntry(2, Float.valueOf(prevData.getDepCHB().replace(',', '.'))));

        BarDataSet d = new BarDataSet(entriesArr, getResources().getString(R.string.prev_arrivee));
        d.setColor(Color.rgb(104, 241, 175));
        d.setHighLightAlpha(255);
        BarDataSet d1 = new BarDataSet(entriesDep, getResources().getString(R.string.prev_depart));
        d1.setColor(Color.RED);
        d1.setHighLightAlpha(255);
        BarDataSet d2 = new BarDataSet(entriesRec, getResources().getString(R.string.prev_recouche));
        d2.setColor(Color.rgb(242, 247, 158));
        d2.setHighLightAlpha(255);

        BarData cd = new BarData(d, d1, d2);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie(PrevisionData prevData) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        /*for (int i = 0; i < 2; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i + 1)));
        }*/
        entries.add(new PieEntry((float) Float.valueOf(prevData.getOccCHB().replace(',', '.')), getResources().getString(R.string.prev_taux)));
        entries.add(new PieEntry((float) (100 - Float.valueOf(prevData.getOccCHB().replace(',', '.'))), getResources().getString(R.string.prev_reste)));
        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }

    public String getURLAPI() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        String urlStr = "HNGAPI";
        boolean exist = URL.toLowerCase().matches(urlStr.toLowerCase());
        if (!exist)
            URL = URL + "/HNGAPI";

        URL = "http://" + URL + "/api/MyHotixHouseKeeping/";
        return URL;
    }

    private void showProgressDialog(int i) {
        if (i == 1)
            pd.setMessage(getResources().getString(R.string.msg_connecting));
        else
            pd.setMessage(getResources().getString(R.string.msg_loading));
        pd.setCancelable(false);
        pd.show();
    }

    private void ShowAlert(String s) {
        AlertDialog.Builder adConnexion = new AlertDialog.Builder(this);
        // adConnexion.setTitle(getResources().getString(R.string.acces_denied));
        adConnexion.setMessage(s);
        //adConnexion.setIcon(R.drawable.offline);
        adConnexion.setNeutralButton(getResources().getString(R.string.close),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        adConnexion.show();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    protected Dialog onCreateDialog(int id) {


        String DateFront = UserInfoModel.getInstance().getUser().getData().getDateFront();
        Date date = new Date();
        //23/02/2017
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            date = sourceFormat.parse(DateFront);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int cyear = c.get(Calendar.YEAR);
        int cmonth = c.get(Calendar.MONTH);
        int cday = c.get(Calendar.DAY_OF_MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        cal2.add(Calendar.DATE, 7);
        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);
        int day2 = cal2.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dp = new DatePickerDialog(this, mDateSetListener, cyear, cmonth,
                        cday);
                //dp.getDatePicker().setMinDate(c.getTimeInMillis());
                dp.getDatePicker().setMinDate(c.getTimeInMillis());
                return dp;
            case DATE_DIALOG_ID1:
                DatePickerDialog dp2 = new DatePickerDialog(this, mDateSetListener1, year2, month2,
                        day2);
                //dp2.getDatePicker().setMinDate(c.getTimeInMillis());
                dp2.getDatePicker().setMinDate(c.getTimeInMillis());
                return dp2;
        }
        return null;
    }

    public Boolean ControleDate() {
        String dateString1 = dateDeb.getText().toString();
        String dateString2 = dateFin.getText().toString();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        try {
            date1 = format.parse(dateString1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date1.compareTo(date2) <= 0) {
            //  System.out.println("dateString1 is an earlier date than dateString2");
            return true;
        } else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mouchard, menu);
        mOptionsMenu = menu;
        MenuItem item = menu.findItem(R.id.table);
        item.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.table:
                Intent i = new Intent(this, PrevisionTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) listDataPrevisions);
                i.putExtras(bundle);
                this.startActivity(i);
                break;

        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * adapter that supports 3 different item types
     */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            return getItem(position).getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    private class HttpRequestTaskPrev extends AsyncTask<Void, Void, PrevisionModel> {
        PrevisionModel response = null;
        String DateDeb, DateFin;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);
            DateDeb = dateDeb.getText().toString();
            DateFin = dateFin.getText().toString();
            String[] d1 = DateDeb.split("/");
            DateDeb = d1[2] + d1[1] + d1[0];
            String[] d2 = DateFin.split("/");
            DateFin = d2[2] + d2[1] + d2[0];

        }

        @Override
        protected PrevisionModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetPrevision?" +
                        "datedeb=" + DateDeb +
                        "&datefin=" + DateFin;
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, PrevisionModel.class);
                    Log.i(TAG, response.toString());
                } catch (IOError error) {
                    Log.e(TAG + " IOError", error.getMessage(), error);
                } catch (Exception ex) {
                    Log.e(TAG + " Exception 1", ex.getMessage(), ex);

                }

                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(PrevisionModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    // showToast("Ok");
                    listDataPrevisions = new ArrayList<PrevisionData>();
                    listDataPrevisions = response.getData();
                    ArrayList<ChartItem> list = new ArrayList<ChartItem>();

                    // 30 items
                    for (int i = 0; i < listDataPrevisions.size() * 3; i++) {

                        if (i % 3 == 0) {
                            //  list.add(new LineChartItem(generateDataLine(i + 1), getApplicationContext()));
                        } else if (i % 3 == 1) {
                            list.add(new BarChartItem(generateDataBar(listDataPrevisions.get(i / 3)), getApplicationContext(),
                                    listDataPrevisions.get(i / 3).getDate()));
                        } else if (i % 3 == 2) {
                            list.add(new PieChartItem(generateDataPie(listDataPrevisions.get(i / 3)), getApplicationContext()));
                        }
                    }

                    ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
                    lv.setAdapter(cda);
                    MenuItem item = mOptionsMenu.findItem(R.id.table);
                    item.setVisible(true);

                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_loading_error));

                }

            } else

            {
                ShowAlert(getResources().getString(R.string.msg_connecting_error));
            }


        }
    }

    public class MyErrorHandler implements ResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // your error handling here
            final String code = String.valueOf(response.getRawStatusCode());
            Log.i("ResponseErrorHandler", "handleError: " + String.valueOf(response.getRawStatusCode()));
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    ShowAlert(getResources().getString(R.string.msg_connecting_error_srv) + "\n(" + code + ")");
                }
            });
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            boolean hasError = false;
            Log.i("ResponseErrorHandler", "hasError: " + String.valueOf(response.getRawStatusCode()));
            int rawStatusCode = response.getRawStatusCode();
            if (rawStatusCode != 200) {
                hasError = true;
            }
            return hasError;
        }
    }
}
