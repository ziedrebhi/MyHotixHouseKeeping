package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DashGouvernante extends Activity implements OnClickListener {

    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/GetDateFrontHotel";
    public final String METHOD_NAME = "GetDateFrontHotel";

    Button room, team, complaint, clientAP, pannes;
    AlertDialog.Builder CloseApp;
    String login, dateFront;
    AlertDialog.Builder adc;
    View layout, v;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_gouvernante);
        room = (Button) findViewById(R.id.btn_rack);
        team = (Button) findViewById(R.id.team_manage);
        complaint = (Button) findViewById(R.id.objTrouves);
        clientAP = (Button) findViewById(R.id.clientAr);
        pannes = (Button) findViewById(R.id.maintenance);
        Bundle extras = getIntent().getExtras();
        login = extras.getString("login");

    }

    @Override
    protected void onResume() {
        room.setOnClickListener(this);
        team.setOnClickListener(this);
        complaint.setOnClickListener(this);
        clientAP.setOnClickListener(this);
        pannes.setOnClickListener(this);
        AsyncCallWS ws = new AsyncCallWS();
        ws.execute();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rack:
                Intent i = new Intent(this, RoomRack.class);
                i.putExtra("login", login);
                this.startActivity(i);
                finish();
                break;

            case R.id.team_manage:
                Intent i1 = new Intent(this, FemmeMenage.class);
                i1.putExtra("login", login);
                i1.putExtra("dateFront", dateFront);
                this.startActivity(i1);
                break;

            case R.id.objTrouves:
                Intent i2 = new Intent(this, ObjetsTrouves.class);
                i2.putExtra("login", login);
                i2.putExtra("dateFront", dateFront);
                this.startActivity(i2);
                finish();
                break;

            case R.id.clientAr:
                Intent i3 = new Intent(this, ListeClientsAP.class);
                i3.putExtra("login", login);
                i3.putExtra("dateFront", dateFront);
                this.startActivity(i3);
                finish();
                break;
            case R.id.maintenance:
                Intent i4 = new Intent(this, PannesList.class);
                i4.putExtra("login", login);
                i4.putExtra("dateFront", dateFront);
                this.startActivity(i4);
                finish();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:

                openDialogConnexion();
                //finish();
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void openDialogConnexion() {

        adc = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.deconnexion, null);
        adc.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        adc.setPositiveButton(getResources().getString(R.string.oui),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        adc.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        adc.show();
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed();
        openDialogConnexion();
    }

    public class AsyncCallWS extends AsyncTask<String, String, String> {
        String response;

        @Override
        protected void onPostExecute(String result) {
            dateFront = response;
            super.onPostExecute(result);
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(getURL());
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                response = envelope.getResponse().toString();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return response;
        }
    }
}
