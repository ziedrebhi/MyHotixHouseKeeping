package com.hotix.myhotixhousekeeping;

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
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ViewAnimator;

import com.fragments.flipviewer.AnimationFactory;
import com.fragments.flipviewer.AnimationFactory.FlipDirection;
import com.hotix.myhotixhousekeeping.adapter.CustomAffect;
import com.hotix.myhotixhousekeeping.adapter.FemmeMenageAdapter;
import com.hotix.myhotixhousekeeping.entities.AffectationFMData;
import com.hotix.myhotixhousekeeping.entities.AffectationFMModel;
import com.hotix.myhotixhousekeeping.entities.FemmesMenage;
import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FemmeMenage extends FragmentActivity {

    static int fmId;

    CustomProgressDialog progressDialog;
    List<FemmesMenage> listFM;
    List<AffectationFMData> listRoom, listRoomNA, listRoomA;
    ListView lvFM;
    ViewAnimator viewAnimator;
    GridView gridAff, gridNonAff;
    Button btnFM, btn_up, btn_down;
    MenuItem item;
    /*
 * Get RackRoom
 */
    String TAG = this.getClass().getSimpleName();
    String EtageId = "-1", BlocId = "-1";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_femme_menage);
        pd = new ProgressDialog(getApplicationContext());
        lvFM = (ListView) findViewById(R.id.listFM);
        viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipperFM);
        gridAff = (GridView) findViewById(R.id.gridAffect);
        gridNonAff = (GridView) findViewById(R.id.gridNonAffect);
        btnFM = (Button) findViewById(R.id.btn_affecterFM);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);
        float scalefactor = getResources().getDisplayMetrics().density * 100;
        int number = getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / (float) scalefactor);
        gridNonAff.setNumColumns(columns);
        gridAff.setNumColumns(columns);

        listFM = UserInfoModel.getInstance().getUser().getData().getFemmesMenage();
        lvFM.setAdapter(new FemmeMenageAdapter(getApplicationContext(),
                listFM));
    }

    @Override
    protected void onResume() {
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);

        listRoomA = new ArrayList<AffectationFMData>();
        listRoomNA = new ArrayList<AffectationFMData>();
        listRoom = new ArrayList<AffectationFMData>();
        displayRoomGrid();

        lvFM.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                fmId = listFM.get(position).getId();
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.RIGHT_LEFT);
                item.setVisible(true);
                if (isConnected()) {
                    new HttpRequestTaskRackRoom().execute();

                } else {
                    ShowAlert(getResources().getString(R.string.acces_denied));
                }
              /*  ListeAffWS ws = new ListeAffWS();
                ws.execute();*/
            }
        });

        gridNonAff.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AffectationFMData rfm = new AffectationFMData();
                rfm = listRoomNA.get(position);
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                listRoomNA.remove(rfm);
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                listRoomA.add(rfm);
                CNA.notifyDataSetChanged();
                gridNonAff.setAdapter(CNA);

            }
        });

        gridAff.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                AffectationFMData rfm = new AffectationFMData();
                rfm = listRoomA.get(position);
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                listRoomA.remove(rfm);
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                listRoomNA.add(rfm);
                CNA.notifyDataSetChanged();
                gridNonAff.setAdapter(CNA);

            }
        });

        btnFM.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listRoomA.size() > 0) {
                    if (isConnected()) {
                        new HttpRequestTaskAffect().execute();
                    } else {
                        ShowAlert(getResources().getString(R.string.acces_denied));
                    }
                } else {
                    ShowAlert(getResources().getString(R.string.msg_empty_affect));
                }

            }
        });

        btn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AffectationFMData rfm = new AffectationFMData();
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                for (int i = 0; i < listRoomA.size(); i++) {
                    rfm = listRoomA.get(i);
                    listRoomNA.add(rfm);
                }
                CNA.notifyDataSetChanged();
                listRoomA.clear();
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                gridNonAff.setAdapter(CNA);
            }
        });

        btn_down.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AffectationFMData rfm = new AffectationFMData();
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                for (int i = 0; i < listRoomNA.size(); i++) {
                    rfm = listRoomNA.get(i);
                    listRoomA.add(rfm);
                }
                CA.notifyDataSetChanged();
                listRoomNA.clear();
                CNA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                gridNonAff.setAdapter(CNA);
            }
        });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fm, menu);
        item = menu.findItem(R.id.retourtop);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retourtop:
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.LEFT_RIGHT);
                item.setVisible(false);
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    // Afficher le Gridview
    private void displayRoomGrid() {
        listRoomNA = new ArrayList<AffectationFMData>();
        listRoomA = new ArrayList<AffectationFMData>();

        for (int i = 0; i < listRoom.size(); i++) {

            if (listRoom.get(i).getId() == fmId) {
                listRoomA.add(listRoom.get(i));
            } else {
                listRoomNA.add(listRoom.get(i));
            }
        }

        gridAff.setAdapter(new CustomAffect(FemmeMenage.this, listRoomA));
        gridNonAff.setAdapter(new CustomAffect(FemmeMenage.this, listRoomNA));
    }

    private void openProg() {
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private String GetChaineAffection() {
        String chaine = "";
        for (int i = 0; i < listRoomA.size(); i++) {
            chaine = chaine + String.valueOf(listRoomA.get(i).getTypeHebId())
                    + "," + String.valueOf(listRoomA.get(i).getTypeProd())
                    + "," + String.valueOf(listRoomA.get(i).getProdId()) + ";";
        }
        return chaine.substring(0, chaine.length() - 1);
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
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
        pd = new ProgressDialog(this);
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

    private class HttpRequestTaskRackRoom extends AsyncTask<Void, Void, AffectationFMModel> {
        AffectationFMModel response = null;
        String etageId, blocId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);

            etageId = EtageId;


        }

        @Override
        protected AffectationFMModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetListAffectionFM?EmployeId=" + 1;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, AffectationFMModel.class);
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
        protected void onPostExecute(AffectationFMModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    listRoom = response.getData();
                    displayRoomGrid();

                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_loading_error));
                }

            } else

            {
                ShowAlert(getResources().getString(R.string.msg_loading_error));
            }


        }
    }


    private class HttpRequestTaskAffect extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;

        String chaine_affectation = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);
            chaine_affectation = GetChaineAffection();

        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "AffectationFemmeMenage?EmployeId=" + fmId +
                        "&chaine_affectation=" + chaine_affectation;
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
                    ShowAlert(getResources().getString(R.string.msg_affect_ok));
                    //finish();
                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_affect_ko));
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
