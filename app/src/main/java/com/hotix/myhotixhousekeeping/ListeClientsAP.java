package com.hotix.myhotixhousekeeping;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.fragments.flipviewer.AnimationFactory;
import com.fragments.flipviewer.AnimationFactory.FlipDirection;
import com.hotix.myhotixhousekeeping.adapter.ArriveesPrevuesAdapter;
import com.hotix.myhotixhousekeeping.entities.ArriveePrevuData;
import com.hotix.myhotixhousekeeping.entities.ArriveePrevuModel;
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

public class ListeClientsAP extends FragmentActivity {

    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;

    static int y1, y2, m1, m2, d1, d2;
    ViewAnimator viewAnimator;
    String login;
    TextView voirListe, valueCP, txtCP;
    EditText dateDebut, dateFin;
    ImageButton actualiser;
    ListView lvCP;
    List<ArriveePrevuData> listeCP;
    String TAG = "AP";
    ProgressDialog pd;
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
        setContentView(R.layout.activity_liste_clients_ap);

        viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipperCP);
        voirListe = (TextView) findViewById(R.id.voirListe);
        valueCP = (TextView) findViewById(R.id.txtValueCP);
        txtCP = (TextView) findViewById(R.id.txtCP);
        dateDebut = (EditText) findViewById(R.id.dateDebCP);
        dateFin = (EditText) findViewById(R.id.dateFinCP);
        actualiser = (ImageButton) findViewById(R.id.btnCP);
        lvCP = (ListView) findViewById(R.id.listArrPrev);
        pd = new ProgressDialog(ListeClientsAP.this);
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
                if (!ControleDate()) {

                    ShowAlert(getResources().getString(R.string.msg_error_date));

                } else {
                    voirListe.setVisibility(View.INVISIBLE);
                    if (isConnected())
                        new HttpRequestTaskArrivee().execute();
                    else ShowAlert(getResources().getString(R.string.acces_denied));
                }
            }
        });


        lvCP.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.LEFT_RIGHT);
            }
        });


    }

    @Override
    protected void onResume() {
        voirListe.setVisibility(View.INVISIBLE);
        valueCP.setVisibility(View.INVISIBLE);
        txtCP.setVisibility(View.INVISIBLE);

        listeCP = new ArrayList<ArriveePrevuData>();
        if (ControleDate()) {
            if (isConnected())
                new HttpRequestTaskArrivee().execute();
            else ShowAlert(getResources().getString(R.string.acces_denied));
        } else {
            ShowAlert(getResources().getString(R.string.msg_error_date));
        }
        SpannableString content = new SpannableString(getText(R.string.listeCP));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        voirListe.setText(content);
        voirListe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.RIGHT_LEFT);
            }
        });


        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("dateDeb", dateDebut.getText().toString());
        outState.putString("dateFin", dateFin.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        dateDebut.setText(savedInstanceState.getString("dateDeb"));
        dateFin.setText(savedInstanceState.getString("dateFin"));
        super.onRestoreInstanceState(savedInstanceState);
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

    private void showToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }

    private void showProgressDialog() {
        pd.setMessage("Connecting ...");
        pd.setCancelable(false);
        pd.show();
    }

    private class HttpRequestTaskArrivee extends AsyncTask<Void, Void, ArriveePrevuModel> {
        ArriveePrevuModel response = null;
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
            listeCP = new ArrayList<ArriveePrevuData>();
            Log.i(TAG, String.valueOf(DateDeb));
            Log.i(TAG, String.valueOf(DateFin));
        }

        @Override
        protected ArriveePrevuModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetArriveesPrevues?datedeb=" + DateDeb
                        + "&datefin=" + DateFin;
                Log.i(TAG, String.valueOf(url));
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, ArriveePrevuModel.class);
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
        protected void onPostExecute(ArriveePrevuModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    //  showToast("Ok");
                    listeCP = response.getData();
                    lvCP.setAdapter(new ArriveesPrevuesAdapter(getApplicationContext(),
                            listeCP));

                    valueCP.setVisibility(View.VISIBLE);
                    valueCP.setText(listeCP.size() + "");
                    txtCP.setVisibility(View.VISIBLE);
                    if (listeCP.size() > 0)
                        voirListe.setVisibility(View.VISIBLE);
                } else {
                    ShowAlert(getResources().getString(R.string.msg_loading_error));
                }
            } else {
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

