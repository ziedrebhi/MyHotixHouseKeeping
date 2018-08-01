package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class UpdateObjetTrouve extends Activity {

    String lieu, Nom, Prenom, RNom, RPrenom, commentaire, description, login,
            dateExtra;
    int idOT;
    EditText lieuUpdate, descriptionUpdate, comment, nom, prenom, renduNom,
            renduPrenom;
    Button btn_update;
    CustomProgressDialog progressDialog;
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_objet_trouve);
        pd = new ProgressDialog(UpdateObjetTrouve.this);
        lieuUpdate = (EditText) findViewById(R.id.lieu);
        descriptionUpdate = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        renduNom = (EditText) findViewById(R.id.nomRendu);
        renduPrenom = (EditText) findViewById(R.id.prenomRendu);
        btn_update = (Button) findViewById(R.id.btn_update);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        lieuUpdate.setEnabled(false);
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
                        new HttpRequestTaskClotureOT().execute();
                    }
                }
            });

            super.onResume();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
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

    private void showToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }


    private class HttpRequestTaskClotureOT extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;

        String prodNum, objTrouveDesc, objTrouveNom, objTrouvePrenom,
                objTrouveLieu, objTrouveRenduNom, objTrouveRenduPrenom,
                commentaire, ImageByteArray;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
            login = UserInfoModel.getInstance().getLogin().toString();
            commentaire = comment.getText().toString();
            objTrouveDesc = descriptionUpdate.getText().toString();
            objTrouveNom = nom.getText().toString();
            objTrouvePrenom = prenom.getText().toString();
            objTrouveRenduNom = renduNom.getText().toString();
            objTrouveRenduPrenom = renduPrenom.getText().toString();
            objTrouveLieu = lieuUpdate.getText().toString();

        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "UpdateObjetTrouves?" +
                        "prodNum=" + "";
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                response = restTemplate.getForObject(url, SuccessModel.class);
                Log.i(TAG, response.toString());
                return response;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                showToast("Error");
            }

            return null;
        }

        @Override
        protected void onPostExecute(SuccessModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok

                    //showToast("Ok")finish();

                } else {
                    // Error
                    showToast("Error");

                }

            } else

            {
                showToast("Error");
            }


        }
    }

}