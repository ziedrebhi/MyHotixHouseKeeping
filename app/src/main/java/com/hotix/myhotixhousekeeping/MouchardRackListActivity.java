package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerEtage;
import com.hotix.myhotixhousekeeping.adapter.MouchardRackAdapter;
import com.hotix.myhotixhousekeeping.entities.Etage;
import com.hotix.myhotixhousekeeping.entities.MouchardRackData;
import com.hotix.myhotixhousekeeping.entities.MouchardRackModel;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MouchardRackListActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;

    static int y1, y2, m1, m2, d1, d2;
    TabHost view;
    EditText dateDebut, dateFin;
    ImageButton actualiser;
    String login, comment, EtatId = "-1", BlocId = "-1";
    ListView lvOT;
    List<MouchardRackData> lisObjetTrouves;
    TextView emptyMsg;
    View v;
    List<Etage> etats;
    Spinner spEtats;
    /*
    Get List Pannes
  */
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;
    private CustomAdapterSpinnerEtage adapterEtats;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date_selected = String.format("%02d", Integer.parseInt(String.valueOf(dayOfMonth))) + "/"
                    + String.format("%02d", Integer.parseInt(String.valueOf(monthOfYear + 1))) + "/"
                    + String.valueOf(year);
            Log.i("PannesListActivity", "here 1" + date_selected);
            dateDebut.setText(date_selected);
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
            Log.i("PannesListActivity", "here 2" + date_selected);
            dateFin.setText(date_selected);
            y2 = year;
            m2 = monthOfYear + 1;
            d2 = dayOfMonth;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mouchard_rack);

        pd = new ProgressDialog(MouchardRackListActivity.this);

        lvOT = (ListView) findViewById(R.id.listOC);
        dateDebut = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        actualiser = (ImageButton) findViewById(R.id.refresh_liste);

        spEtats = (Spinner) findViewById(R.id.spinnerEtat);

        emptyMsg.setVisibility(View.GONE);

        etats = new ArrayList<Etage>();
        etats.add(new Etage(-1, -1, getResources().getString(R.string.all)));
        etats.addAll(UserInfoModel.getInstance().getUser().getData().getEtages());

        adapterEtats = new CustomAdapterSpinnerEtage(MouchardRackListActivity.this,
                android.R.layout.simple_dropdown_item_1line, etats);
        spEtats.setAdapter(adapterEtats);
        spEtats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                Etage etage = adapterEtats.getItem(position);
                EtatId = String.valueOf(etage.getId());
                BlocId = String.valueOf(etage.getBlocId());
                if (ControleDate()) {
                    if (isConnected())
                        new HttpRequestTaskMouchard().execute();
                    else ShowAlert(getResources().getString(R.string.acces_denied));
                } else {
                    ShowAlert(getResources().getString(R.string.msg_error_date));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });


        String DateFront = UserInfoModel.getInstance().getUser().getData().getDateFront();
        Date date = new Date();
        //23/02/2017
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            date = sourceFormat.parse(DateFront);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dateDebut.setText(String.format("%02d", Integer.parseInt(String.valueOf(day))) + "/"
                + String.format("%02d", Integer.parseInt(String.valueOf(month + 1))) + "/"
                + String.valueOf(year));
        dateFin.setText(String.format("%02d", Integer.parseInt(String.valueOf(day))) + "/"
                + String.format("%02d", Integer.parseInt(String.valueOf(month + 1))) + "/"
                + String.valueOf(year));

        dateDebut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dateDebut)
                    showDialog(DATE_DIALOG_ID);

            }
        });

        dateFin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (v1 == dateFin)
                    showDialog(DATE_DIALOG_ID1);

            }
        });

        actualiser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ControleDate()) {
                    if (isConnected())
                        new HttpRequestTaskMouchard().execute();
                    else ShowAlert(getResources().getString(R.string.acces_denied));
                } else {
                    ShowAlert(getResources().getString(R.string.msg_error_date));
                }
            }
        });


    }

    public boolean getAuthoriseImageSend() {
        boolean authoriseImage = false;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            authoriseImage = sp.getBoolean("image", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return authoriseImage;
    }

    @Override
    protected void onResume() {
        if (ControleDate()) {
            if (isConnected())
                new HttpRequestTaskMouchard().execute();
            else ShowAlert(getResources().getString(R.string.acces_denied));
        } else {
            ShowAlert(getResources().getString(R.string.msg_error_date));
        }


        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        super.onBackPressed();
    }

    public Boolean ControleDate() {
        String dateString1 = dateDebut.getText().toString();
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

        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, cyear, cmonth,
                        cday);
            case DATE_DIALOG_ID1:
                return new DatePickerDialog(this, mDateSetListener1, cyear, cmonth,
                        cday);
        }
        return null;
    }

    public String getURLAPI() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/HNGAPI/api/MyHotixHouseKeeping/";
        return URL;
    }

    private void showToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
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

    private class HttpRequestTaskMouchard extends AsyncTask<Void, Void, MouchardRackModel> {
        MouchardRackModel response = null;
        String DateDeb, DateFin;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);
            DateDeb = dateDebut.getText().toString();
            DateFin = dateFin.getText().toString();
            String[] d1 = DateDeb.split("/");
            DateDeb = d1[2] + d1[1] + d1[0];
            String[] d2 = DateFin.split("/");
            DateFin = d2[2] + d2[1] + d2[0];

        }

        @Override
        protected MouchardRackModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetMouchardRackRoom?" +
                        "etage=" + EtatId +
                        "&bloc=" + BlocId +
                        "&datedeb=" + DateDeb +
                        "&datefin=" + DateFin;
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, MouchardRackModel.class);
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
        protected void onPostExecute(MouchardRackModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    // showToast("Ok");
                    lisObjetTrouves = new ArrayList<MouchardRackData>();
                    lisObjetTrouves = response.getData();

                    lvOT.setAdapter(new MouchardRackAdapter(getApplicationContext(),
                            response.getData()));
                    if (response.getData().size() == 0) {
                        emptyMsg.setVisibility(View.VISIBLE);
                    } else {
                        emptyMsg.setVisibility(View.GONE);

                    }

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
