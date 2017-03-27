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
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerTech;
import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerTypePanne;
import com.hotix.myhotixhousekeeping.adapter.PanneAdapter;
import com.hotix.myhotixhousekeeping.entities.PanneData;
import com.hotix.myhotixhousekeeping.entities.PanneModel;
import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.entities.Technicien;
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

public class PannesListActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;

    private static final int ID_CLOTURE = 2;
    private static final int ID_VIEW = 3;

    static int y1, y2, m1, m2, d1, d2;

    TabHost view;
    EditText dateDebut, dateFin;
    ImageButton actualiser, create;
    String login;

    ListView lvOT;
    List<PanneData> listPanne;
    Spinner spinner;
    List<String> techs;
    List<Technicien> listTech;
    AlertDialog.Builder builderMenu;
    TextView header, room_number, emptyMsg;
    View v;
    int panneId, technicienId;
    String comment;
    Spinner spEtatPannes;
    List<com.hotix.myhotixhousekeeping.entities.TypesPanne> etats;
    String EtatId = "1";
    QuickAction mQuickActionEncours, mQuickActionClotured;
    CustomAdapterSpinnerTech adapterTech;
    int TechId;
    EditText commentEd;
    String CommentaireCloture = "";
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

        setContentView(R.layout.activity_pannes);

        pd = new ProgressDialog(PannesListActivity.this);

        listTech = new ArrayList<Technicien>();
        listTech = UserInfoModel.getInstance().getUser().getData().getTechniciens();

        lvOT = (ListView) findViewById(R.id.listOC);
        dateDebut = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        actualiser = (ImageButton) findViewById(R.id.refresh_liste);
        create = (ImageButton) findViewById(R.id.refresh_liste2);
        spEtatPannes = (Spinner) findViewById(R.id.spinnerEtat);

        emptyMsg.setVisibility(View.GONE);

        etats = new ArrayList<TypesPanne>();
        etats.add(new TypesPanne(1, "En cours"));
        etats.add(new TypesPanne(2, "Colturée"));

        adapterEtats = new CustomAdapterSpinnerTypePanne(PannesListActivity.this,
                android.R.layout.simple_dropdown_item_1line, etats);
        spEtatPannes.setAdapter(adapterEtats);
        spEtatPannes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                com.hotix.myhotixhousekeeping.entities.TypesPanne etage = adapterEtats.getItem(position);
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
                        DeclarationPanne.class);
                i.putExtra("prod_id", -1);
                i.putExtra("num_chb", -1);
                i.putExtra("login", login);
                i.putExtra("room", false);
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
        return false;
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
        ActionItem clotureItem = new ActionItem(ID_CLOTURE, getResources()
                .getString(R.string.cloture), getResources().getDrawable(R.drawable.cloture));
        ActionItem viewItem = new ActionItem(ID_VIEW, getResources()
                .getString(R.string.view), getResources().getDrawable(R.drawable.view));
        mQuickActionEncours = new QuickAction(this);

        mQuickActionEncours.addActionItem(clotureItem);
        mQuickActionEncours.addActionItem(viewItem);

        // setup the action item click listener
        mQuickActionEncours
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_VIEW) {

                            Intent i = new Intent(getApplicationContext(), ViewPanneActivity.class);

                            i.putExtra("lieu", listPanne.get(mSelectedRow).getLieu());
                            i.putExtra("description", listPanne.get(mSelectedRow).getDescription());
                            i.putExtra("duree", listPanne.get(mSelectedRow).getDuree());
                            i.putExtra("nom", listPanne.get(mSelectedRow).getNom());
                            i.putExtra("prenom", listPanne.get(mSelectedRow).getPrenom());
                            i.putExtra("type", listPanne.get(mSelectedRow).getType());
                            i.putExtra("urgent", listPanne.get(mSelectedRow).isUrgent());

                            if (listPanne.get(mSelectedRow).getImage() != "") {
                                i.putExtra("image", scaleDownBitmap(getImageFromString(listPanne.get(mSelectedRow).getImage()), 100, getApplicationContext()));

                            } else {
                                i.putExtra("image", (Parcelable[]) null);

                            }
                            Log.i("Panne to show", listPanne.get(mSelectedRow).toString());
                            startActivity(i);
                        } else {
                            panneId = listPanne.get(mSelectedRow).getId();
                            comment = listPanne.get(mSelectedRow).getDescription();

                            Log.i("Panne à cloturer", listPanne.get(mSelectedRow).getDescription());
                            Log.i("Panne à cloturer ", String.valueOf(panneId));

                            ShowCloture(listPanne.get(mSelectedRow).getLieu());
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
        mQuickActionClotured.addActionItem(viewItem);

        // setup the action item click listener
        mQuickActionClotured
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_VIEW) {

                            Intent i = new Intent(getApplicationContext(), ViewPanneActivity.class);

                            i.putExtra("lieu", listPanne.get(mSelectedRow).getLieu());
                            i.putExtra("description", listPanne.get(mSelectedRow).getDescription());
                            i.putExtra("duree", listPanne.get(mSelectedRow).getDuree());
                            i.putExtra("nom", listPanne.get(mSelectedRow).getNom());
                            i.putExtra("prenom", listPanne.get(mSelectedRow).getPrenom());
                            i.putExtra("type", listPanne.get(mSelectedRow).getType());
                            i.putExtra("urgent", listPanne.get(mSelectedRow).isUrgent());

                            if (listPanne.get(mSelectedRow).getImage() != "") {
                                i.putExtra("image", scaleDownBitmap(getImageFromString(listPanne.get(mSelectedRow).getImage()), 100, getApplicationContext()));

                            } else {
                                i.putExtra("image", (Parcelable[]) null);

                            }
                            Log.i("Panne to show", listPanne.get(mSelectedRow).toString());
                            startActivity(i);
                        } else {
                            panneId = listPanne.get(mSelectedRow).getId();
                            comment = listPanne.get(mSelectedRow).getDescription();

                            Log.i("Panne à cloturer", listPanne.get(mSelectedRow).getDescription());
                            Log.i("Panne à cloturer ", String.valueOf(panneId));

                            ShowCloture(listPanne.get(mSelectedRow).getLieu());
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
                if (EtatId.equals("1")) {
                    mQuickActionEncours.show(view);
                } else {
                    mQuickActionClotured.show(view);
                }
            }
        });
        super.onResume();
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
        i.putExtra("login", login);
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

    private void ShowCloture(String roomNb) {

        builderMenu = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.menu_cloture_panne, null);
        builderMenu.setView(v);
        commentEd = (EditText) v.findViewById(R.id.editText);
        header = (TextView) v.findViewById(R.id.textHeader);
        spinner = (Spinner) v.findViewById(R.id.spTech);
        techs = new ArrayList<String>();
        listTech = new ArrayList<Technicien>();
        listTech = UserInfoModel.getInstance().getUser().getData().getTechniciens();

        adapterTech = new CustomAdapterSpinnerTech(PannesListActivity.this,
                android.R.layout.simple_dropdown_item_1line, listTech);
        spinner.setAdapter(adapterTech);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                com.hotix.myhotixhousekeeping.entities.Technicien tech = adapterTech.getItem(position);
                TechId = tech.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number.setText("" + roomNb);
        builderMenu.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            com.hotix.myhotixhousekeeping.entities.Technicien tech = (com.hotix.myhotixhousekeeping.entities.Technicien) spinner.getSelectedItem();
                            technicienId = tech.getId();
                            Log.i("Panne à cloturer TechId", String.valueOf(technicienId));
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (commentEd.getText().toString().equals("")) {
                                            commentEd.setError(getResources().getString(R.string.comment_requis));
                                        } else {

                                            if (isConnected()) {
                                                CommentaireCloture = commentEd.getText().toString();
                                                new HttpRequestTaskCloturePanne().execute();
                                            } else
                                                ShowAlert(getResources().getString(R.string.acces_denied));
                                        }
                                    }
                                });

                            } catch (Exception ex) {
                                Log.i("Exception", ex.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builderMenu.show();
    }

    public String getURLAPI() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/HNGAPI/api/MyHotixHouseKeeping/";
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


    private class HttpRequestTaskListPanne extends AsyncTask<Void, Void, PanneModel> {
        PanneModel response = null;
        String DateDeb, DateFin;
        int imageOk = 0;

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
                imageOk = 1;
            }
        }

        @Override
        protected PanneModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetListPannesCloture?" +
                        "etat=" + EtatId +
                        "&image=" + imageOk +
                        "&datedeb=" + DateDeb +
                        "&datefin=" + DateFin;
                Log.i(TAG, url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, PanneModel.class);
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
        protected void onPostExecute(PanneModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    // showToast("Ok");
                    listPanne = new ArrayList<PanneData>();
                    listPanne = response.getData();

                    lvOT.setAdapter(new PanneAdapter(getApplicationContext(),
                            response.getData()));
                    if (response.getData().size() == 0) {
                        emptyMsg.setVisibility(View.VISIBLE);

                    } else {
                        emptyMsg.setVisibility(View.GONE);
                        Log.i("Zied", listPanne.get(0).getImage().toString());
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


    private class HttpRequestTaskCloturePanne extends AsyncTask<Void, Void, SuccessModel> {
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
                final String url = getURLAPI() + "CloturePanne?" +
                        "panneId=" + panneId +
                        "&user_login=" + login +
                        "&comment=" + CommentaireCloture +
                        "&technicienId=" + technicienId;
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
