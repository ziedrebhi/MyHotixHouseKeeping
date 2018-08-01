package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerTypePanne;
import com.hotix.myhotixhousekeeping.adapter.ObjetTrouveAdapter;
import com.hotix.myhotixhousekeeping.entities.ObjetTrouveData;
import com.hotix.myhotixhousekeeping.entities.ObjetTrouveModel;
import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.entities.TypesPanne;
import com.hotix.myhotixhousekeeping.quickaction.ActionItem;
import com.hotix.myhotixhousekeeping.quickaction.QuickAction;
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

public class ObjetsTrouveListActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    private static final int ID_UPDATE = 1;
    private static final int ID_CLOTURE = 2;
    private static final int ID_VIEW = 3;
    static int y1, y2, m1, m2, d1, d2;
    static int idObjT;
    TabHost view;
    EditText dateDebut, dateFin;
    ImageButton actualiser, create;
    String login, comment, EtatId = "1";
    ListView lvOT;
    List<ObjetTrouveData> lisObjetTrouves;
    Spinner spinner;
    AlertDialog.Builder builderMenu;
    TextView header, room_number, emptyMsg;
    View v;
    List<TypesPanne> etats;
    Spinner spEtats;
    QuickAction mQuickActionEncours, mQuickActionClotured;
    /*
    Get List Pannes
  */
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;
    private int mSelectedRow = 0;
    private CustomAdapterSpinnerTypePanne adapterEtats;
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

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h = (int) (newHeight * densityMultiplier);
        int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_objets_trouve);

        pd = new ProgressDialog(ObjetsTrouveListActivity.this);

        lvOT = (ListView) findViewById(R.id.listOC);
        dateDebut = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        actualiser = (ImageButton) findViewById(R.id.refresh_liste);
        create = (ImageButton) findViewById(R.id.refresh_liste2);
        spEtats = (Spinner) findViewById(R.id.spinnerEtat);
        if (!UserInfoModel.getInstance().getUser().getData().isHasAddObjet())
            create.setVisibility(View.GONE);
        emptyMsg.setVisibility(View.GONE);

        etats = new ArrayList<TypesPanne>();
        etats.add(new TypesPanne(1, getResources().getString(R.string.etat_encours)));
        etats.add(new TypesPanne(2, getResources().getString(R.string.etat_ecloturer)));

        adapterEtats = new CustomAdapterSpinnerTypePanne(ObjetsTrouveListActivity.this,
                android.R.layout.simple_dropdown_item_1line, etats);
        spEtats.setAdapter(adapterEtats);
        spEtats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                TypesPanne etage = adapterEtats.getItem(position);
                EtatId = String.valueOf(etage.getId());

                if (ControleDate()) {
                    if (isConnected())
                        new HttpRequestTaskListPanne().execute();
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
                        new HttpRequestTaskListPanne().execute();
                    else ShowAlert(getResources().getString(R.string.acces_denied));
                } else {
                    ShowAlert(getResources().getString(R.string.msg_error_date));
                }
            }
        });
        create.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        DeclarationObjetTrouve.class);
                startActivity(i);
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
                new HttpRequestTaskListPanne().execute();
            else ShowAlert(getResources().getString(R.string.acces_denied));
        } else {
            ShowAlert(getResources().getString(R.string.msg_error_date));
        }

        ActionItem clotureItem = new ActionItem(ID_CLOTURE, getResources().getString(R.string.cloture), getResources().getDrawable(
                R.drawable.cloture));
        ActionItem viewItem = new ActionItem(ID_VIEW, getResources().getString(R.string.view), getResources().getDrawable(
                R.drawable.view));

        mQuickActionEncours = new QuickAction(this);
        if (UserInfoModel.getInstance().getUser().getData().isHasCloseObjet())
            mQuickActionEncours.addActionItem(clotureItem);
        mQuickActionEncours.addActionItem(viewItem);

        // setup the action item click listener
        mQuickActionEncours
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_UPDATE) {
                            try {
                                UpdateObjetTrouve(lisObjetTrouves.get(mSelectedRow)
                                        .getLieu(), lisObjetTrouves.get(mSelectedRow)
                                        .getId(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomRendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomrendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getCommentaire(), lisObjetTrouves
                                        .get(mSelectedRow).getDescription());
                            } catch (Exception ex) {
                                Log.e("Excetiion 1111", ex.toString());
                            }
                        } else if (actionId == ID_CLOTURE) {
                            idObjT = lisObjetTrouves.get(mSelectedRow).getId();
                            if (isConnected())
                                new HttpRequestTaskClotureOT().execute();
                            else ShowAlert(getResources().getString(R.string.acces_denied));
                        } else {
                            try {
                                ViewObjetTrouve(lisObjetTrouves.get(mSelectedRow)
                                        .getLieu(), lisObjetTrouves.get(mSelectedRow)
                                        .getId(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomRendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomrendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getCommentaire(), lisObjetTrouves
                                        .get(mSelectedRow).getDescription(), (getAuthoriseImageSend() ? lisObjetTrouves
                                        .get(mSelectedRow).getImage() : ""));
                            } catch (Exception ex) {
                                Log.e("Excetiion", ex.toString());
                            }
                        }
                    }
                });

        // setup on dismiss listener, set the icon back to normal
        mQuickActionEncours.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        mQuickActionClotured = new QuickAction(this);
        // mQuickAction.addActionItem(updateItem);
        mQuickActionClotured.addActionItem(viewItem);

        // setup the action item click listener
        mQuickActionClotured
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_UPDATE) {
                            try {
                                UpdateObjetTrouve(lisObjetTrouves.get(mSelectedRow)
                                        .getLieu(), lisObjetTrouves.get(mSelectedRow)
                                        .getId(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomRendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomrendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getCommentaire(), lisObjetTrouves
                                        .get(mSelectedRow).getDescription());
                            } catch (Exception ex) {
                                Log.e("Excetiion", ex.toString());
                            }
                        } else if (actionId == ID_CLOTURE) {
                            idObjT = lisObjetTrouves.get(mSelectedRow).getId();
                            if (isConnected())
                                new HttpRequestTaskClotureOT().execute();
                            else ShowAlert(getResources().getString(R.string.acces_denied));
                        } else {
                            try {
                                ViewObjetTrouve(lisObjetTrouves.get(mSelectedRow)
                                        .getLieu(), lisObjetTrouves.get(mSelectedRow)
                                        .getId(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomTrouve(), lisObjetTrouves.get(mSelectedRow)
                                        .getNomRendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getPrenomrendu(), lisObjetTrouves.get(mSelectedRow)
                                        .getCommentaire(), lisObjetTrouves
                                        .get(mSelectedRow).getDescription(), (getAuthoriseImageSend() ? lisObjetTrouves
                                        .get(mSelectedRow).getImage() : ""));
                            } catch (Exception ex) {
                                Log.e("Excetiion", ex.toString());
                            }
                        }
                    }
                });

        // setup on dismiss listener, set the icon back to normal
        mQuickActionClotured.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        lvOT.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedRow = position;
                Log.i("ETAT ", EtatId);
                if (EtatId.equals("1"))
                    mQuickActionEncours.show(view);
                else {
                    mQuickActionClotured.show(view);
                }
            }
        });
        super.onResume();
    }

    private void UpdateObjetTrouve(String lieu, int idOT, String Nom,
                                   String Prenom, String RNom, String RPrenom, String commentaire,
                                   String description) {
        try {
            Intent i = new Intent(this.getApplicationContext(), UpdateObjetTrouve.class);
            i.putExtra("lieu", lieu);
            Log.i("lieu", lieu);
            i.putExtra("idOT", idOT);
            Log.i("idOT", String.valueOf(idOT));
            i.putExtra("Nom", Nom);
            Log.i("Nom", Nom);
            i.putExtra("Prenom", Prenom);
            Log.i("Prenom", Prenom);
            i.putExtra("RNom", RNom);
            Log.i("RNom", RNom);
            i.putExtra("RPrenom", RPrenom);
            Log.i("RPrenom", RPrenom);
            i.putExtra("commentaire", commentaire);
            Log.i("commentaire", commentaire);
            i.putExtra("description", description);
            Log.i("description", description);
            i.putExtra("login", UserInfoModel.getInstance().getLogin());
            Log.i("login", UserInfoModel.getInstance().getLogin());
            i.putExtra("dateFront", UserInfoModel.getInstance().getUser().getData().getDateFront());
            //Log.i("dateFront", dateExtra);
            this.startActivity(i);


        } catch (Exception ex) {
            Log.e("Excetiion", ex.toString());
        }
    }

    private void ViewObjetTrouve(String lieu, int idOT, String Nom,
                                 String Prenom, String RNom, String RPrenom, String commentaire,
                                 String description, String image) {
        try {
            Intent i = new Intent(this.getApplicationContext(), ViewObjetTrouveActivity.class);
            i.putExtra("lieu", lieu);
            Log.i("lieu", lieu);
            i.putExtra("idOT", idOT);
            Log.i("idOT", String.valueOf(idOT));
            i.putExtra("Nom", Nom);
            Log.i("Nom", Nom);
            i.putExtra("Prenom", Prenom);
            Log.i("Prenom", Prenom);
            i.putExtra("RNom", RNom);
            Log.i("RNom", RNom);
            i.putExtra("RPrenom", RPrenom);
            Log.i("RPrenom", RPrenom);
            i.putExtra("commentaire", commentaire);
            Log.i("commentaire", commentaire);
            i.putExtra("description", description);
            Log.i("description", description);
            i.putExtra("login", UserInfoModel.getInstance().getLogin());
            Log.i("login2222222", UserInfoModel.getInstance().getLogin());
            i.putExtra("dateFront", UserInfoModel.getInstance().getUser().getData().getDateFront());
            Log.i("IMAGE ZIED", image + "fhfh");
            if (image != "") {
                i.putExtra("image", scaleDownBitmap(getImageFromString(image), 100, getApplicationContext()));

            } else {
                i.putExtra("image", (Parcelable[]) null);

            }
            this.startActivity(i);


        } catch (Exception ex) {
            Log.e("Excetiion", ex.toString());
        }
    }

    public Bitmap getImageFromString(String base64s) {
        try {

            byte[] decodedString = Base64.decode(base64s, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Log.i("getImageFromString", "here");
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
        i.putExtra("login", UserInfoModel.getInstance().getLogin());
        startActivity(i);
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

    private class HttpRequestTaskListPanne extends AsyncTask<Void, Void, ObjetTrouveModel> {
        ObjetTrouveModel response = null;
        String DateDeb, DateFin;
        int isImage = 0;

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
            if (getAuthoriseImageSend()) {
                isImage = 1;
            }
        }

        @Override
        protected ObjetTrouveModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetListObjetsTrouves?" +
                        "etat=" + EtatId +
                        "&image=" + isImage +
                        "&datedeb=" + DateDeb +
                        "&datefin=" + DateFin;
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, ObjetTrouveModel.class);
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
        protected void onPostExecute(ObjetTrouveModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    // showToast("Ok");
                    lisObjetTrouves = new ArrayList<ObjetTrouveData>();
                    lisObjetTrouves = response.getData();

                    lvOT.setAdapter(new ObjetTrouveAdapter(getApplicationContext(),
                            response.getData()));
                    if (response.getData().size() == 0) {
                        emptyMsg.setVisibility(View.VISIBLE);
                    } else {
                        emptyMsg.setVisibility(View.GONE);
                        Log.i("Zied", lisObjetTrouves.get(0).getImage().toString());
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

    private class HttpRequestTaskClotureOT extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;
        String login;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);
            login = UserInfoModel.getInstance().getLogin().toString();
        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "ClotureObjetTrouve?" +
                        "objTrouveId=" + idObjT;
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
                    //showToast("Ok");
                    if (ControleDate()) {
                        if (isConnected())
                            new HttpRequestTaskListPanne().execute();
                        else ShowAlert(getResources().getString(R.string.acces_denied));
                    } else {
                        ShowAlert(getResources().getString(R.string.msg_error_date));
                    }

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
