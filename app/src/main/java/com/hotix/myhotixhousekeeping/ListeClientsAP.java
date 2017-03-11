package com.hotix.myhotixhousekeeping;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.fragments.flipviewer.AnimationFactory;
import com.fragments.flipviewer.AnimationFactory.FlipDirection;
import com.hotix.myhotixhousekeeping.adapter.ArriveesPrevuesAdapter;
import com.hotix.myhotixhousekeeping.model.ClientPrevue;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListeClientsAP extends FragmentActivity {

    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    static int y1, y2, m1, m2, d1, d2;
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/GetArriveesPrevues";
    public final String METHOD_NAME = "GetArriveesPrevues";
    CustomProgressDialog progressDialog;
    ViewAnimator viewAnimator;
    String login, dateExtra;
    TextView voirListe, valueCP, txtCP;
    EditText dateDebut, dateFin;
    Button actualiser;
    ListView lvCP;
    ArrayList<ClientPrevue> listeCP;
    int[] Tab;
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        // onDateSet method
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date_selected = String.valueOf(dayOfMonth) + "/"
                    + String.valueOf(monthOfYear + 1) + "/"
                    + String.valueOf(year);
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
            String date_selected = String.valueOf(dayOfMonth) + "/"
                    + String.valueOf(monthOfYear + 1) + "/"
                    + String.valueOf(year);
            dateFin.setText(date_selected);
            y2 = year;
            m2 = monthOfYear + 1;
            d2 = dayOfMonth;
        }
    };

    protected Dialog onCreateDialog(int id) {
        Calendar c = Calendar.getInstance();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_clients_ap);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipperCP);
        voirListe = (TextView) findViewById(R.id.voirListe);
        valueCP = (TextView) findViewById(R.id.txtValueCP);
        txtCP = (TextView) findViewById(R.id.txtCP);
        dateDebut = (EditText) findViewById(R.id.dateDebCP);
        dateFin = (EditText) findViewById(R.id.dateFinCP);
        actualiser = (Button) findViewById(R.id.btnCP);
        lvCP = (ListView) findViewById(R.id.listArrPrev);
    }

    @Override
    protected void onResume() {
        voirListe.setVisibility(View.INVISIBLE);
        valueCP.setVisibility(View.INVISIBLE);
        txtCP.setVisibility(View.INVISIBLE);

        Intent in = getIntent();
        dateExtra = in.getStringExtra("dateFront");
        Tab = new int[3];
        Tab = getDateFront(dateExtra);

        listeCP = new ArrayList<ClientPrevue>();

        Bundle extras = getIntent().getExtras();
        login = extras.getString("login");

        SpannableString content = new SpannableString(getText(R.string.listeCP));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        voirListe.setText(content);

        y1 = Tab[2];
        m1 = Tab[1];
        d1 = Tab[0];

        String date_selected, date_selected1;
        date_selected = String.valueOf(d1) + "/" + String.valueOf(m1) + "/"
                + String.valueOf(y1);
        y2 = y1;
        m2 = m1;
        d2 = d1;

        date_selected1 = String.valueOf(d2) + "/" + String.valueOf(m2) + "/"
                + String.valueOf(y2);

        dateDebut.setText(date_selected);
        dateFin.setText(date_selected1);

        voirListe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.RIGHT_LEFT);
            }
        });

        actualiser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!ControleDate()) {
                    ShowToast();
                } else {
                    voirListe.setVisibility(View.INVISIBLE);
                    AsyncClientsPrevuesWS ws = new AsyncClientsPrevuesWS();
                    ws.execute();
                }
            }
        });

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

        lvCP.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.LEFT_RIGHT);
            }
        });
        AsyncClientsPrevuesWS ws = new AsyncClientsPrevuesWS();
        ws.execute();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
        i.putExtra("login", login);
        startActivity(i);
        finish();
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

    private void MessageErreurServeur() {
        Toast t = Toast
                .makeText(
                        getApplicationContext(),
                        "Erreur de connexion au serveur ! \n Veuillez réessayer s'il vous plait.",
                        Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public void ShowToast() {
        Toast t = Toast.makeText(this,
                "Date erronée ! \n Veuillez entrer une date correcte.",
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public Boolean ControleDate() {
        Boolean res = true;
        if ((y1 > y2) || ((y1 == y2) && (m1 > m2))
                || ((y1 == y2) && (m1 == m2) && (d1 > d2))) {
            res = false;
        }

        return res;

    }

    public String getDate(String text) {

        String str1[] = text.replaceAll(" ", "").split("/");

        String day = "00" + str1[0].trim();
        day = day.substring(day.length() - 2);

        String month = "00" + str1[1].trim();
        month = (month).substring(month.length() - 2);

        return str1[2] + month + day;
    }

    public int[] getDateFront(String dt) {
        int[] Tab = new int[3];
        if (dt == null) {

            java.util.Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            dt = formatter.format(date);

        }
        String str[] = new String[3];
        str[0] = dt.substring(6, 8);
        str[1] = dt.substring(4, 6);
        str[2] = dt.substring(0, 4);
        if (str[0].charAt(0) == '0') {
            str[0].replace("0", "");
        }
        if (str[1].charAt(0) == '0') {
            str[1].replace("0", "");
        }
        Tab[0] = Integer.parseInt(str[0]);
        Tab[1] = Integer.parseInt(str[1]);
        Tab[2] = Integer.parseInt(str[2]);

        return Tab;
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    public class AsyncClientsPrevuesWS extends
            AsyncTask<String, String, ArrayList<ClientPrevue>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            listeCP = new ArrayList<ClientPrevue>();
            progressDialog.setCancelable(true);
            progressDialog.show();
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<ClientPrevue> listeCP) {

            lvCP.setAdapter(new ArriveesPrevuesAdapter(getApplicationContext(),
                    listeCP));

            valueCP.setVisibility(View.VISIBLE);
            valueCP.setText(listeCP.size() + "");
            txtCP.setVisibility(View.VISIBLE);
            if (listeCP.size() > 0)
                voirListe.setVisibility(View.VISIBLE);
            androidHttpTransport.reset();
            progressDialog.dismiss();
            super.onPostExecute(listeCP);
        }

        protected ArrayList<ClientPrevue> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            PropertyInfo pi_dateDeb = new PropertyInfo();
            pi_dateDeb.setName("dateDeb");
            pi_dateDeb.setValue(getDate(dateDebut.getText().toString()));
            pi_dateDeb.setType(String.class);
            request.addProperty(pi_dateDeb);

            PropertyInfo pi_dateFin = new PropertyInfo();
            pi_dateFin.setName("dateFin");
            pi_dateFin.setValue(getDate(dateFin.getText().toString()));
            pi_dateFin.setType(String.class);
            request.addProperty(pi_dateFin);

            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 30000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    response = (SoapObject) envelope.getResponse();
                } catch (SocketTimeoutException e) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            MessageErreurServeur();
                            androidHttpTransport.reset();
                        }
                    });

                }
                if (response != null) {
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ClientPrevue ClientPrev;
                            SoapObject ClientPrevues = new SoapObject();
                            SoapObject objCP = new SoapObject();

                            ClientPrevues = (SoapObject) response
                                    .getProperty(0);
                            for (int i = 0; i < ClientPrevues
                                    .getPropertyCount(); i++) {
                                objCP = (SoapObject) ClientPrevues
                                        .getProperty(i);

                                ClientPrev = new ClientPrevue();
                                ClientPrev.setResaId(objCP.getProperty("RESA")
                                        .toString());
                                if (objCP.getProperty("ROOM").toString().trim()
                                        .equals("anyType{}")) {
                                    ClientPrev.setROOM(0);
                                } else {
                                    ClientPrev.setROOM(Integer.parseInt(objCP
                                            .getProperty("ROOM").toString()));
                                }

                                ClientPrev.setNOM(objCP.getProperty("NOM")
                                        .toString());
                                ClientPrev.setPRENOM(objCP
                                        .getProperty("PRENOM").toString());
                                ClientPrev.setARRIVEE(objCP.getProperty(
                                        "ARRIVEE").toString());
                                ClientPrev.setDEPART(objCP
                                        .getProperty("DEPART").toString());
                                listeCP.add(ClientPrev);

                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listeCP;
        }

    }
}
