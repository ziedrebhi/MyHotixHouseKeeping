package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class UpdateObjetTrouve extends Activity {
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/UpdateObjetTrouves";
    public final String METHOD_NAME = "UpdateObjetTrouves";
    String lieu, Nom, Prenom, RNom, RPrenom, commentaire, description, login,
            dateExtra;
    int idOT;
    EditText lieuUpdate, descriptionUpdate, comment, nom, prenom, renduNom,
            renduPrenom;
    Button btn_update;
    CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_objet_trouve);

        lieuUpdate = (EditText) findViewById(R.id.lieu);
        descriptionUpdate = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        renduNom = (EditText) findViewById(R.id.nomRendu);
        renduPrenom = (EditText) findViewById(R.id.prenomRendu);
        btn_update = (Button) findViewById(R.id.btn_update);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
    }

    @Override
    protected void onResume() {
        try {
            Bundle extras = getIntent().getExtras();
            dateExtra = extras.getString("dateFront");
            login = extras.getString("login");
            lieu = extras.getString("lieu");
            idOT = extras.getInt("idOT");
            Nom = extras.getString("Nom");
            Prenom = extras.getString("Prenom");
            RNom = extras.getString("RNom");
            RPrenom = extras.getString("RPrenom");
            commentaire = extras.getString("commentaire");
            description = extras.getString("description");

            lieuUpdate.setText(lieu);
            descriptionUpdate.setText(description);
            if (commentaire == null) {
                comment.setText("");
            } else {
                comment.setText(commentaire);
            }

            nom.setText(Nom);
            prenom.setText(Prenom);
            renduNom.setText(RNom);
            renduPrenom.setText(RPrenom);

            btn_update.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (ChapmsRequis()) {
                        AsyncUpdateObjetWS ws = new AsyncUpdateObjetWS();
                        ws.execute();
                    }
                }
            });

            super.onResume();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
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

    public void ShowEtatMessage(Boolean b) {

        if (b) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_layout_id));

            TextView text = (TextView) layout.findViewById(R.id.textResult);
            text.setText(getResources().getString(R.string.update_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            Intent i = new Intent(getApplicationContext(), ObjetsTrouves.class);
            i.putExtra("login", login);
            i.putExtra("dateFront", dateExtra);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_error,
                    (ViewGroup) findViewById(R.id.custom_toast_echec));

            TextView text = (TextView) layout.findViewById(R.id.textError);
            text.setText(getResources().getString(R.string.update_not_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
    }

    private boolean ChapmsRequis() {
        if (lieuUpdate.getText().toString().length() == 0) {
            lieuUpdate.setError("Chapms  requis");
        }
        if (descriptionUpdate.getText().toString().length() == 0) {
            descriptionUpdate.setError("Chapms  requis");
        }
        if (nom.getText().toString().length() == 0) {
            nom.setError("Chapms  requis");
        }
        if (prenom.getText().toString().length() == 0) {
            prenom.setError("Chapms  requis");
        }

        if (lieuUpdate.getText().toString().length() == 0
                || descriptionUpdate.getText().toString().length() == 0
                || nom.getText().toString().length() == 0
                || prenom.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    public class AsyncUpdateObjetWS extends AsyncTask<String, String, String> {
        HttpTransportSE androidHttpTransport;
        String result = "False";

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(true);
            progressDialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo pi_prodNum = new PropertyInfo();
            pi_prodNum.setName("prodNum");
            pi_prodNum.setValue(lieuUpdate.getText().toString());
            pi_prodNum.setType(String.class);
            request.addProperty(pi_prodNum);

            PropertyInfo pi_objTrouveId = new PropertyInfo();
            pi_objTrouveId.setName("objTrouveId");
            pi_objTrouveId.setValue(idOT);
            pi_objTrouveId.setType(Integer.class);
            request.addProperty(pi_objTrouveId);

            PropertyInfo objTrouveDesc = new PropertyInfo();
            objTrouveDesc.setName("objTrouveDesc");
            objTrouveDesc.setValue(descriptionUpdate.getText().toString());
            objTrouveDesc.setType(String.class);
            request.addProperty(objTrouveDesc);

            PropertyInfo objTrouveNom = new PropertyInfo();
            objTrouveNom.setName("objTrouveNom");
            objTrouveNom.setValue(nom.getText().toString());
            objTrouveNom.setType(String.class);
            request.addProperty(objTrouveNom);

            PropertyInfo objTrouvePrenom = new PropertyInfo();
            objTrouvePrenom.setName("objTrouvePrenom");
            objTrouvePrenom.setValue(prenom.getText().toString());
            objTrouvePrenom.setType(String.class);
            request.addProperty(objTrouvePrenom);

            PropertyInfo objRenduTrouveNom = new PropertyInfo();
            objRenduTrouveNom.setName("objTrouveRenduNom");
            objRenduTrouveNom.setValue(renduNom.getText().toString());
            objRenduTrouveNom.setType(String.class);
            request.addProperty(objRenduTrouveNom);

            PropertyInfo objRenduTrouvePrenom = new PropertyInfo();
            objRenduTrouvePrenom.setName("objTrouveRenduPrenom");
            objRenduTrouvePrenom.setValue(renduPrenom.getText().toString());
            objRenduTrouvePrenom.setType(String.class);
            request.addProperty(objRenduTrouvePrenom);

            PropertyInfo objTrouveLieu = new PropertyInfo();
            objTrouveLieu.setName("objTrouveLieu");
            objTrouveLieu.setValue(lieuUpdate.getText().toString());
            objTrouveLieu.setType(String.class);
            request.addProperty(objTrouveLieu);

            PropertyInfo pi_login = new PropertyInfo();
            pi_login.setName("login");
            pi_login.setValue(login);
            pi_login.setType(String.class);
            request.addProperty(pi_login);

            PropertyInfo pi_comment = new PropertyInfo();
            pi_comment.setName("comment");
            pi_comment.setValue(comment.getText().toString());
            pi_comment.setType(String.class);
            request.addProperty(pi_comment);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 30000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    result = envelope.getResponse().toString();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowEtatMessage(Boolean.parseBoolean(result));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
        }
    }
}
