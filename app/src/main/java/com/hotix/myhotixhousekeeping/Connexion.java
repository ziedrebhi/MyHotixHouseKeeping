package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.adapter.NavDrawerListAdapter;
import com.hotix.myhotixhousekeeping.entities.LoginModel;
import com.hotix.myhotixhousekeeping.model.NavDrawerItem;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;
import com.hotix.myhotixhousekeeping.utils.IPServeur;
import com.hotix.myhotixhousekeeping.utils.UpdateChecker;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Connexion extends Activity implements
        android.view.View.OnClickListener {

    int time = 20;
    Timer t;
    TimerTask task;
    CustomProgressDialog progressDialog;
    EditText login, password;
    Button connexion;
    View layout, v;
    TextView header;
    AlertDialog.Builder ad, adc, adf;
    boolean resultLogin;
    // Update
    UpdateChecker checker;
    SharedPreferences pref;
    /*
     * Login
     */
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // Initialisation des vues
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.motdp);
        connexion = (Button) findViewById(R.id.connect);

        connexion.setOnClickListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);


        mTitle = mDrawerTitle = getTitle();
        navDrawerItems = new ArrayList<NavDrawerItem>();

        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
                .getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
                .getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
                .getResourceId(2, -1)));
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        pd = new ProgressDialog(Connexion.this);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        checker = new UpdateChecker(this, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private void displayView(int position) {

        switch (position) {
            case 0:
                Intent i = new Intent(this, IPServeur.class);
                this.startActivity(i);
                break;
            case 2:
                CloseApp();
                break;
            case 1:
                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);

                PackageManager manager = this.getPackageManager();
                alertDialogBuilder2.setTitle(getResources().getText(
                        R.string.app_name));
                alertDialogBuilder2.setIcon(R.drawable.housekeeping_icon);
                PackageInfo info;
                try {
                    info = manager.getPackageInfo(this.getPackageName(), 0);
                    alertDialogBuilder2.setMessage(getResources().getText(
                            R.string.version) + " : " + info.versionName);

                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                alertDialogBuilder2.setCancelable(false);
                alertDialogBuilder2.setPositiveButton(getResources().getString(R.string.OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //ResetFields();

                            }
                        });


                alertDialogBuilder2.show();

                break;
        }

    }

    private void CloseApp() {

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

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {

        if (isConnected()) // S'il y a connexion
        {


            if (login.getText().toString().trim().equals("")) {
                login.setError(getResources().getString(R.string.connect_data));
            }

            // Appel web service d'autentification
            else {
                HttpRequestTaskLogin ws = new HttpRequestTaskLogin();
                ws.execute();
            }
        } else // S'il n'y a pas de connexion
        {
            ShowAlert(getResources().getString(
                    R.string.msg_acces_denied));
        }

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }

    public boolean CheckVersionApp() {
        try {
            boolean lastVersion = true;
            pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String serveur = (pref.getString("serveur", ""));
            if (!serveur.equals("")) {
                serveur = serveur.split("/")[0];
                Log.i("serveur", "http://" + serveur + "/Android/versionHouseKeeping.txt");
                checker.checkForUpdateByVersionCode("http://" + serveur + "/Android/versionHouseKeeping.txt");

                if (checker.isUpdateAvailable()) {
                    Log.i("CheckVersionApp Update", "True");
                    lastVersion = false;

                } else {
                    Log.i("CheckVersionApp Update", "False");
                }
            }
            return lastVersion;
        } catch (Exception e) {
            return false;
        }
    }

    public void DownloadAndInstallLastVersion() {
        try {
            String serveur = (pref.getString("serveur", ""));
            if (!serveur.equals("")) {
                serveur = serveur.split("/")[0];
                Log.i("serveur", "http://" + serveur + "/Android/appHouseKeeping.apk");
                checker.downloadAndInstall("http://" + serveur + "/Android/appHouseKeeping.apk");
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String serveur = (pref.getString("serveur", ""));
        if (serveur.equals("")) {

            AlertDialog.Builder adConnexion = new AlertDialog.Builder(this);

            adConnexion.setMessage(getResources().getString(R.string.msg_srv_configuration));

            adConnexion.setNeutralButton(getResources().getString(R.string.configurer),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getApplicationContext(), IPServeur.class);
                            startActivity(i);
                        }
                    });
            adConnexion.show();
        }
        Boolean autoupdate = true;
        if (isOnline()) {
            Log.i("AUTO UPDATE", "Online");
            if (autoupdate) {
                Log.i("AUTO UPDATE", "TRUE");

                if (!CheckVersionApp()) {
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this);
                    Log.i("AUTO UPDATE", "OUTDATE : GET LAST VERSION");
                    PackageManager manager = this.getPackageManager();
                    alertDialogBuilder2.setTitle(getResources().getText(
                            R.string.app_name));
                    alertDialogBuilder2.setMessage(getResources().getText(
                            R.string.last_version_no));
                    alertDialogBuilder2.setIcon(R.drawable.ic_launcher);

                    alertDialogBuilder2.setCancelable(false);
                    alertDialogBuilder2.setPositiveButton(getResources().getText(
                            R.string.update),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //ResetFields();
                                    DownloadAndInstallLastVersion();
                                }
                            });


                    alertDialogBuilder2.show();
                } else {
                    Log.i("AUTO UPDATE", "UPDATED : You have last version");

                }
            } else {
                Log.i("AUTO UPDATE", "FALSE");

            }
        } else {
            Log.i("AUTO UPDATE", "Not Online");
        }
    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/webservice/MYHOTIX.asmx";
        return URL;
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

    private void showProgressDialog() {
        pd.setMessage(getResources().getString(R.string.msg_connecting));
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

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    private class HttpRequestTaskLogin extends AsyncTask<Void, Void, LoginModel> {
        LoginModel response = null;
        String Login, Password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
            Log.i("Login", "Start");
            Login = login.getText().toString().trim();
            Password = password.getText().toString().trim();

        }

        @Override
        protected LoginModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "login?login=" + Login
                        + "&password=" + Password;
                RestTemplate restTemplate = new RestTemplate();

                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, LoginModel.class);
                    Log.i(TAG, response.toString());
                } catch (IOError error) {
                    Log.e(TAG + " IOError", error.getMessage(), error);
                } catch (Exception ex) {
                    Log.e(TAG + " Exception 1", ex.getMessage(), ex);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ShowAlert(getResources().getString(R.string.msg_connecting_error_srv));
                        }
                    });

                }
                UserInfoModel.getInstance().setUser(response);
                UserInfoModel.getInstance().setLogin(Login);

                return response;
            } catch (Exception e) {
                Log.e(TAG + " Exception 2", e.getMessage(), e);

            }

            return null;
        }

        @Override
        protected void onPostExecute(LoginModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    int error = response.getData().getError();


                    if (error != -1) {
                        // Connexion failed
                        switch (error) {
                            case 0: // not authorised
                                ShowAlert(getResources().getString(R.string.msg_connecting_not_authorised));
                                login.setError(getResources().getString(R.string.msg_connecting_not_authorised));

                                break;
                            case 1: // not actif
                                ShowAlert(getResources().getString(R.string.msg_connecting_not_active));
                                login.setError(getResources().getString(R.string.msg_connecting_not_active));

                                break;
                            case 2: // expired
                                ShowAlert(getResources().getString(R.string.msg_connecting_expired_pwd));
                                password.setError(getResources().getString(R.string.msg_connecting_expired_pwd));

                                break;
                            case 3: // error password
                                ShowAlert(getResources().getString(R.string.msg_connecting_wrong_pwd));
                                password.setError(getResources().getString(R.string.msg_connecting_wrong_pwd));

                                break;
                            case 4: // unknow user
                                ShowAlert(getResources().getString(R.string.msg_connecting_not_found));
                                login.setError(getResources().getString(R.string.msg_connecting_not_found));

                                break;
                            case 5: // unknown
                                ShowAlert(getResources().getString(R.string.msg_connecting_error));

                                break;
                        }
                    } else {
                        // connexion OK
                        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
                        i.putExtra("login", login.getText().toString());
                        startActivity(i);
                    }
                } else {
                    ShowAlert(getResources().getString(R.string.msg_connecting_error));
                }
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
