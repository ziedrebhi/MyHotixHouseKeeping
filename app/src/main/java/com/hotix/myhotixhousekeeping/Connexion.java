package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
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
import com.hotix.myhotixhousekeeping.model.NavDrawerItem;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;
import com.hotix.myhotixhousekeeping.utils.IPServeur;
import com.hotix.myhotixhousekeeping.utils.UpdateChecker;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Connexion extends Activity implements
        android.view.View.OnClickListener {

    private static int nbEssais = 1;
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/Authentifier";
    public final String METHOD_NAME = "Authentifier";
    public Handler mHandler;
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
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        connexion.setOnClickListener(this);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        startTimer();

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
                alertDialogBuilder2.setPositiveButton("OK",
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

    private void DialogCloseApp() {
        ad = new AlertDialog.Builder(this);
        ad.setIcon(R.drawable.alert);
        ad.setTitle("Attention !");
        ad.setMessage(getResources().getString(R.string.close_app));
        ad.setPositiveButton(getResources().getString(R.string.oui),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

        ad.setNegativeButton(getResources().getString(R.string.cancel),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        ad.show();
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
            // Test champs requis
        /*	if (login.getText().toString().trim().equals("")
                    && password.getText().toString().equals("")) {
				login.setError(getResources().getString(R.string.connect_data));
				password.setError(getResources().getString(
						R.string.connect_data));
			}

			else if (!login.getText().toString().trim().equals("")
					&& password.getText().toString().equals("")) {
				password.setError(getResources().getString(
						R.string.connect_data));
			}
*/

            if (login.getText().toString().trim().equals("")) {
                login.setError(getResources().getString(R.string.connect_data));
            }

            // Appel web service d'autentification
            else {
                AsyncCallWS ws = new AsyncCallWS();
                ws.execute();
            }
        } else // S'il n'y a pas de connexion
        {
            AlertConnexionInternet();
        }

    }

    private void AlertConnexionInternet() {
        AlertDialog.Builder adConnexion = new AlertDialog.Builder(this);
        adConnexion.setTitle(getResources().getString(R.string.acces_denied));
        adConnexion.setMessage(getResources().getString(
                R.string.msg_acces_denied));
        adConnexion.setIcon(R.drawable.offline);
        adConnexion.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        adConnexion.show();
    }

    public void ConnexionResa(Boolean b) {
        if (b) {
            progressDialog.dismiss();
            Intent i = new Intent(this, DashGouvernante.class);
            i.putExtra("login", login.getText().toString());
            login.setText("");
            password.setText("");
            this.startActivity(i);

        } else {
            AccessDeniedAlert();
        }
    }

    private void AccessDeniedAlert() {
        ad = new AlertDialog.Builder(this);
        progressDialog.dismiss();
        ad.setIcon(R.drawable.error);
        ad.setTitle(getResources().getString(R.string.acces_denied));
        ad.setMessage(getResources().getString(R.string.verify_data));
        ad.setNeutralButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                password.setText("");
            }
        });
        ad.show();
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void openProg() {
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private void openDialogConnexion() {
        progressDialog.dismiss();
        adc = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.new_connexion, null);
        adc.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        adc.setPositiveButton(getResources().getString(R.string.oui),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AsyncCallWS ws = new AsyncCallWS();
                        ws.execute();
                    }
                });

        adc.setNegativeButton(getResources().getString(R.string.cancel),
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nbEssais = 1;
                    }
                });

        adc.show();
    }

    private void openDialogFinale() {
        progressDialog.dismiss();
        adf = new AlertDialog.Builder(this);
        adf.setIcon(R.drawable.error);
        adf.setMessage("Connexion échouée.");
        adf.setNeutralButton("OK", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                nbEssais = 1;
            }
        });
        adf.show();
    }

    private void NewConnection() {
        if (nbEssais == 2) {
            openDialogConnexion();
        } else if (nbEssais == 3) {
            openDialogConnexion();
        } else {
            openDialogFinale();
            nbEssais = 1;
        }
    }

    public void aboutLooper() {
        Thread th = new Thread() {
            public void run() {
                Looper.prepare();
                MessageQueue queue = Looper.myQueue();
                queue.addIdleHandler(new IdleHandler() {
                    int mReqCount = 0;

                    @Override
                    public boolean queueIdle() {
                        if (++mReqCount == 2) {

                            return false;
                        } else
                            return true;
                    }
                });
                progressDialog.dismiss();
                NewConnection();
                Looper.loop();
            }
        };
        th.start();
    }

    public void startTimer() {
        t = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time > 0)
                            time -= 1;
                    }
                });
            }
        };
        t.scheduleAtFixedRate(task, 0, 1000);
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
                Log.i("serveur", "http://" + serveur + "/Android/appHouseKeeping.apk");
                checker.downloadAndInstall("http://" + serveur + "/Android/appHouseKeeping.apk");
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }

    public class AsyncCallWS extends AsyncTask<String, String, String> {
        String result;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            openProg();
            nbEssais++;
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            // Les paramètres
            PropertyInfo type = new PropertyInfo();
            type.setName("type");
            type.setValue("M");
            type.setType(String.class);
            request.addProperty(type);

            PropertyInfo logval = new PropertyInfo();
            logval.setName("login");
            logval.setValue(login.getText().toString().trim());
            logval.setType(String.class);
            request.addProperty(logval);

            PropertyInfo passval = new PropertyInfo();
            passval.setName("password");
            passval.setValue(password.getText().toString());
            passval.setType(String.class);
            request.addProperty(passval);

            // Paramètres d'appel du web service 'Authentitfier'
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
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
                            androidHttpTransport.reset();
                            NewConnection();
                        }
                    });

                }
                if (result != null) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ConnexionResa(Boolean.parseBoolean(result));
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            //androidHttpTransport.reset();
            super.onPostExecute(result);
        }

    }
}
