package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerUser;
import com.hotix.myhotixhousekeeping.entities.AutorisationData;
import com.hotix.myhotixhousekeeping.entities.AutorisationModel;
import com.hotix.myhotixhousekeeping.entities.SuccessModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    String TAG = this.getClass().getSimpleName();
    Spinner spinnerUser;
    Switch hasconfig, hasfm, hasaddpanne, hasclosepanne, hasaddobjet, hascloseobjet, hasmouchard, hastatus, haslieu, hasguest;
    List<AutorisationData> listUser;
    CustomAdapterSpinnerUser adapter;
    Button btn;
    int IdUser = -1;
    AutorisationData CurrentUser;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        spinnerUser = (Spinner) findViewById(R.id.spinnerUser);
        hasaddobjet = (Switch) findViewById(R.id.hasaddobjet);
        hascloseobjet = (Switch) findViewById(R.id.hascloseobjet);
        hasaddpanne = (Switch) findViewById(R.id.hasaddpanne);
        hasclosepanne = (Switch) findViewById(R.id.hasclosepanne);
        hasfm = (Switch) findViewById(R.id.hasfm);
        hasconfig = (Switch) findViewById(R.id.hasconfig);
        hasmouchard = (Switch) findViewById(R.id.has_mouchard_room);
        hastatus = (Switch) findViewById(R.id.has_change_statut);
        haslieu = (Switch) findViewById(R.id.has_etat_lieu);
        hasguest = (Switch) findViewById(R.id.has_view_client);
        btn = (Button) findViewById(R.id.btn_reclamer);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CurrentUser.setHasAddObjet(hasaddobjet.isChecked());
                CurrentUser.setHasAddPanne(hasaddpanne.isChecked());
                CurrentUser.setHasChangeStatut(hastatus.isChecked());
                CurrentUser.setHasCloseObjet(hascloseobjet.isChecked());
                CurrentUser.setHasClosePanne(hasclosepanne.isChecked());
                CurrentUser.setHasFM(hasfm.isChecked());
                CurrentUser.setHasConfig(hasconfig.isChecked());
                CurrentUser.setHasMouchard(hasmouchard.isChecked());
                CurrentUser.setHasViewClient(hasguest.isChecked());
                CurrentUser.setHasEtatLieu(haslieu.isChecked());
                if (isConnected())
                    new HttpRequestTaskUpdateSettings().execute();
                else ShowAlert(getResources().getString(R.string.acces_denied));
            }
        });
        pd = new ProgressDialog(this);
        listUser = new ArrayList<AutorisationData>();
        adapter = new CustomAdapterSpinnerUser(SettingsActivity.this,
                android.R.layout.simple_dropdown_item_1line, listUser);

        spinnerUser.setAdapter(adapter);
    }

    @Override
    protected void onResume() {


        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                com.hotix.myhotixhousekeeping.entities.AutorisationData user = adapter.getItem(position);
                CurrentUser = user;
                // IdUser = user.getId();
                if (user.isHasConfig()) {
                    hasconfig.setEnabled(false);
                } else {
                    hasconfig.setEnabled(true);
                }
                hasguest.setChecked(user.isHasViewClient());
                hastatus.setChecked(user.isHasChangeStatut());
                hasclosepanne.setChecked(user.isHasClosePanne());
                hascloseobjet.setChecked(user.isHasCloseObjet());
                hasconfig.setChecked(user.isHasConfig());
                hasaddpanne.setChecked(user.isHasAddPanne());
                hasaddobjet.setChecked(user.isHasAddObjet());
                hasmouchard.setChecked(user.isHasMouchard());
                haslieu.setChecked(user.isHasEtatLieu());
                hasfm.setChecked(user.isHasFM());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        if (isConnected())
            new HttpRequestTaskListUser().execute();
        else ShowAlert(getResources().getString(R.string.acces_denied));
        super.onResume();
    }

    public String getURLAPI() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/HNGAPI/api/MyHotixHouseKeeping/";
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

    private class HttpRequestTaskListUser extends AsyncTask<Void, Void, AutorisationModel> {
        AutorisationModel response = null;

        int id = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);

        }

        @Override
        protected AutorisationModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetAutorisation?" +
                        "id=" + IdUser;
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, AutorisationModel.class);
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
        protected void onPostExecute(AutorisationModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    listUser = new ArrayList<AutorisationData>();
                    listUser = response.getData();

                    adapter = new CustomAdapterSpinnerUser(SettingsActivity.this,
                            android.R.layout.simple_dropdown_item_1line, listUser);

                    spinnerUser.setAdapter(adapter);
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

    private class HttpRequestTaskUpdateSettings extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;
        String login;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);

        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "UpdateAutorisation?" +
                        "util_id=" + CurrentUser.getId() +
                        "&has_config=" + ((CurrentUser.isHasConfig()) ? 1 : 0) +
                        "&has_add_panne=" + ((CurrentUser.isHasAddPanne()) ? 1 : 0) +
                        "&has_add_objet=" + ((CurrentUser.isHasAddObjet()) ? 1 : 0) +
                        "&has_close_panne=" + ((CurrentUser.isHasClosePanne()) ? 1 : 0) +
                        "&has_close_objet=" + ((CurrentUser.isHasCloseObjet()) ? 1 : 0) +
                        "&has_mouchard_room=" + ((CurrentUser.isHasMouchard()) ? 1 : 0) +
                        "&has_change_statut=" + ((CurrentUser.isHasChangeStatut()) ? 1 : 0) +
                        "&has_etat_lieu=" + ((CurrentUser.isHasEtatLieu()) ? 1 : 0) +
                        "&has_view_client=" + ((CurrentUser.isHasViewClient()) ? 1 : 0) +
                        "&has_fm=" + ((CurrentUser.isHasFM()) ? 1 : 0);

                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, SuccessModel.class);
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
        protected void onPostExecute(SuccessModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    // showToast("Ok");

                    if (isConnected()) {
                        new HttpRequestTaskListUser().execute();
                    } else ShowAlert(getResources().getString(R.string.acces_denied));

                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_panne_cloture_error));

                }

            } else

            {
                ShowAlert(getResources().getString(R.string.msg_connecting_error));
            }


        }
    }


}
