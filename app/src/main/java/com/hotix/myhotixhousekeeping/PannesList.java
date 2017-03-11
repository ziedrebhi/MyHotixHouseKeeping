package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.adapter.PanneAdapter;
import com.hotix.myhotixhousekeeping.model.Panne;
import com.hotix.myhotixhousekeeping.model.TypePanne;
import com.hotix.myhotixhousekeeping.quickaction.ActionItem;
import com.hotix.myhotixhousekeeping.quickaction.QuickAction;
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
import java.util.List;

public class PannesList extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "MH_HOUSEKEEPING_OBJETS_TROUVES";
    private static final int ID_UPDATE = 1;
    private static final int ID_CLOTURE = 2;
    private static final int ID_VIEW = 3;
    //ViewAnimator viewAnimator1;
    static EditText lieu;
    static int idObjT;
    static int y1, y2, m1, m2, d1, d2;
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION2 = "http://tempuri.org/GetListePannesCloture";
    public final String METHOD_NAME2 = "GetListePannesCloture";
    public final String SOAP_ACTION3 = "http://tempuri.org/CloturePanne";
    public final String METHOD_NAME3 = "CloturePanne";
    public final String SOAP_ACTION1 = "http://tempuri.org/GetListeTechniciens";
    public final String METHOD_NAME1 = "GetListeTechniciens";
    CustomProgressDialog progressDialog;
    TabHost view;
    EditText dateDebut, dateFin;
    Button actualiser, create;
    String login, dateExtra;
    int[] Tab;
    ListView lvOT;
    ArrayList<Panne> listPanne;
    Spinner spinner;
    List<String> techs;
    ArrayList<TypePanne> listTech;
    AlertDialog.Builder builderMenu;
    TextView header, room_number, emptyMsg;
    View v;
    int panneId, technicienId;
    String comment;
    private Uri fileUri;
    private int mSelectedRow = 0;
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
        // viewAnimator1 = (ViewAnimator) this.findViewById(R.id.viewFlipperOT);
        Bundle extras = getIntent().getExtras();
        login = extras.getString("login");
        lvOT = (ListView) findViewById(R.id.listOC);
        techs = new ArrayList<String>();
        listTech = new ArrayList<TypePanne>();
        listPanne = new ArrayList<Panne>();

        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        dateDebut = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        actualiser = (Button) findViewById(R.id.refresh_liste);
        create = (Button) findViewById(R.id.refresh_liste2);
        emptyMsg.setVisibility(View.GONE);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        Intent in = getIntent();
        dateExtra = in.getStringExtra("dateFront");
        //Log.i("Zied Date Front ", dateExtra);
        Tab = new int[3];
        Tab = getDateFront(dateExtra);

        y1 = Tab[2];
        m1 = Tab[1];
        d1 = 1;

        String date_selected, date_selected1;
        date_selected = String.valueOf(d1) + "/" + String.valueOf(m1) + "/"
                + String.valueOf(y1);
        y2 = Tab[2];
        m2 = Tab[1];
        d2 = Tab[0];
        date_selected1 = String.valueOf(d2) + "/" + String.valueOf(m2) + "/"
                + String.valueOf(y2);


        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        // Log.i("Date Zied", date.toString());
        dateDebut.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/"
                + String.valueOf(year));
        dateFin.setText(String.valueOf(day) + "/" + String.valueOf(month + 1) + "/"
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
                if (!ControleDate()) {
                    ShowToast();
                } else {
                    AsyncCallWS ws = new AsyncCallWS();
                    ws.execute();
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
                i.putExtra("Activite", 1);
                i.putExtra("login", login);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    protected void onResume() {

        if (!ControleDate()) {
            ShowToast();
        } else {
            AsyncCallWS ws = new AsyncCallWS();
            ws.execute();
        }
        ActionItem clotureItem = new ActionItem(ID_CLOTURE, getResources()
                .getString(R.string.cloture), getResources().getDrawable(
                R.drawable.cloture));
        ActionItem viewItem = new ActionItem(ID_VIEW, getResources()
                .getString(R.string.view), getResources().getDrawable(
                R.drawable.view));
        final QuickAction mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(clotureItem);
        mQuickAction.addActionItem(viewItem);

        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_VIEW) {
                            Intent i = new Intent(getApplicationContext(), ViewPanneActivity.class);

                            i.putExtra("lieu", listPanne.get(mSelectedRow).getLieuPanne());
                            i.putExtra("description", listPanne.get(mSelectedRow).getDescription());
                            i.putExtra("duree", listPanne.get(mSelectedRow).getDuree());
                            i.putExtra("nom", listPanne.get(mSelectedRow).getNom());
                            i.putExtra("prenom", listPanne.get(mSelectedRow).getPrenom());
                            i.putExtra("type", listPanne.get(mSelectedRow).getTypePanne().getNomPanne());
                            i.putExtra("urgent", listPanne.get(mSelectedRow).isUrgent());
                            if (listPanne.get(mSelectedRow).getImage() != null) {
                                i.putExtra("image", scaleDownBitmap(getImageFromString(listPanne.get(mSelectedRow).getImage()), 100, getApplicationContext()));

                            } else {
                                i.putExtra("image", (Parcelable[]) null);

                            }
                            Log.i("Panne to show", listPanne.get(mSelectedRow).toString());
                            startActivity(i);
                        } else {
                            panneId = listPanne.get(mSelectedRow).getIdPanne();
                            comment = listPanne.get(mSelectedRow).getDescription();

                            Log.i("Panne à cloturer", listPanne.get(mSelectedRow).getDescription());
                            Log.i("Panne à cloturer ", String.valueOf(panneId));

                            ShowCloture(listPanne.get(mSelectedRow).getLieuPanne());
                        }
                    }
                });

        // setup on dismiss listener, set the icon back to normal
        mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        lvOT.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mSelectedRow = position;
                mQuickAction.show(view);
            }
        });

        techs = new ArrayList<String>();
        listTech = new ArrayList<TypePanne>();

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

    public void ShowToast() {
        Toast t = Toast.makeText(this,
                "Date erronée ! \n Veuillez entrer une date correcte.",
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
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
            text.setText(getResources().getString(R.string.declare_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            Intent i = new Intent(getApplicationContext(),
                    DashGouvernante.class);
            i.putExtra("login", login);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_error,
                    (ViewGroup) findViewById(R.id.custom_toast_echec));

            TextView text = (TextView) layout.findViewById(R.id.textError);
            text.setText(getResources().getString(R.string.daclare_not_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
    }

    private void ShowClotureMessage(Boolean b) {

        if (b) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_layout_id));

            TextView text = (TextView) layout.findViewById(R.id.textResult);
            text.setText(getResources().getString(R.string.cloture_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            Intent i = new Intent(getApplicationContext(), PannesList.class);
            i.putExtra("login", login);
            i.putExtra("dateFront", dateExtra);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_error,
                    (ViewGroup) findViewById(R.id.custom_toast_echec));

            TextView text = (TextView) layout.findViewById(R.id.textError);
            text.setText(getResources().getString(R.string.cloture_not_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
        i.putExtra("login", login);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    public int[] getDateFront(String dt) {
        int[] Tab = new int[3];
        if (dt == null) {

            java.util.Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            dt = formatter.format(date);

        }
        Log.i("Date Front", dt);

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

    private void ShowCloture(String roomNb) {

        builderMenu = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.menu_cloture_panne, null);
        builderMenu.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        spinner = (Spinner) v.findViewById(R.id.spTech);
        techs = new ArrayList<String>();
        listTech = new ArrayList<TypePanne>();
        AsyncCallWSListTech ws = new AsyncCallWSListTech();
        ws.execute();
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number.setText("" + roomNb);
        builderMenu.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            TypePanne tech = (TypePanne) spinner.getSelectedItem();
                            technicienId = tech.getIdPanne();
                            Log.i("Panne à cloturer TechId", String.valueOf(technicienId));
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AsyncClotureObjetWS cloture = new AsyncClotureObjetWS();
                                        cloture.execute();
                                    }
                                });

                            } catch (Exception ex) {
                                Log.i("Exception", ex.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        builderMenu.show();
    }

    private void displayPannesSpinner() {
        try {
            for (int i = 0; i < listTech.size(); i++) {
                techs.add(listTech.get(i).getNomPanne());
            }
            ArrayAdapter<TypePanne> dataAdapter = new ArrayAdapter<TypePanne>(this,
                    android.R.layout.simple_spinner_item, listTech);
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AsyncClotureObjetWS extends AsyncTask<String, String, String> {
        HttpTransportSE androidHttpTransport;
        String result = "False";

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(true);
            progressDialog.show();
            super.onPreExecute();
            Log.i("AsyncClotureObjetWS", "onPreExecute");
        }

        protected String doInBackground(String... params) {
            Log.i("AsyncClotureObjetWS", "doInBackground");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);

            PropertyInfo pi_objTrouveId = new PropertyInfo();
            pi_objTrouveId.setName("panneId");
            pi_objTrouveId.setValue(panneId);
            Log.i("AynckCloture panneId", String.valueOf(panneId));
            pi_objTrouveId.setType(Integer.class);
            request.addProperty(pi_objTrouveId);

            PropertyInfo pi_comment = new PropertyInfo();
            pi_comment.setName("comment");
            pi_comment.setValue(comment);
            Log.i("AynckCloture comment", comment);
            pi_comment.setType(String.class);
            request.addProperty(pi_comment);

            PropertyInfo pi_user_login = new PropertyInfo();
            pi_user_login.setName("user_login");
            pi_user_login.setValue(login);
            Log.i("AynckCloture login", login);
            pi_user_login.setType(String.class);
            request.addProperty(pi_user_login);

            PropertyInfo pi_technicienId = new PropertyInfo();
            pi_technicienId.setName("technicienId");
            pi_technicienId.setValue(technicienId);
            Log.i("AynckCloture techId", String.valueOf(technicienId));
            pi_technicienId.setType(Integer.class);
            request.addProperty(pi_technicienId);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            androidHttpTransport = new HttpTransportSE(getURL(), 30000);
            try {
                try {
                    Log.i("AsyncClotureObjetWS", envelope.toString());
                    androidHttpTransport.call(SOAP_ACTION3, envelope);

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
                        ShowClotureMessage(Boolean.parseBoolean(result));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            androidHttpTransport.reset();
            super.onPostExecute(result);
        }
    }

    public class AsyncCallWS extends
            AsyncTask<String, String, ArrayList<Panne>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(true);
            listPanne = new ArrayList<Panne>();
            progressDialog.show();
            super.onPreExecute();
            Log.i("AsyncCallWS", "onPreExecute");
        }

        protected void onPostExecute(ArrayList<Panne> listPanne) {
            lvOT.setAdapter(new PanneAdapter(getApplicationContext(),
                    listPanne));
            if (listPanne.size() == 0) {
                emptyMsg.setVisibility(View.VISIBLE);
            } else emptyMsg.setVisibility(View.GONE);
            androidHttpTransport.reset();
            progressDialog.dismiss();
            super.onPostExecute(listPanne);
        }

        protected ArrayList<Panne> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            PropertyInfo pi_dateDeb = new PropertyInfo();
            pi_dateDeb.setName("dateDeb");
            pi_dateDeb.setValue(getDate(dateDebut.getText().toString()));
            Log.i("DATE 1", getDate(dateDebut.getText().toString()));
            pi_dateDeb.setType(String.class);
            request.addProperty(pi_dateDeb);

            PropertyInfo pi_dateFin = new PropertyInfo();
            pi_dateFin.setName("dateFin");
            pi_dateFin.setValue(getDate(dateFin.getText().toString()));
            Log.i("DATE 2", getDate(dateFin.getText().toString()));
            pi_dateFin.setType(String.class);
            request.addProperty(pi_dateFin);

            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 30000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
                    response = (SoapObject) envelope.getResponse();
                    Log.i("Response", response.toString());
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
                            Panne objPanne;
                            SoapObject ObjetClotures = new SoapObject();
                            SoapObject objC = new SoapObject();

                            ObjetClotures = (SoapObject) response
                                    .getProperty(0);
                            Log.i("Object", ObjetClotures.toString());

                            if (ObjetClotures.toString().equals("anyType{}")) {
                                Toast.makeText(getApplicationContext(),
                                        "Pas de pannes.",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < ObjetClotures
                                    .getPropertyCount(); i++) {

                                objC = (SoapObject) ObjetClotures
                                        .getProperty(i);

                                objPanne = new Panne();
                                objPanne.setLieuPanne(objC.getProperty("lblLieu")
                                        .toString());

                                objPanne.setIdPanne(Integer.parseInt(objC
                                        .getProperty("lblId")
                                        .toString()));

                                objPanne.setDate(objC
                                        .getProperty("lblDate").toString()
                                        .subSequence(0, 10).toString());
                                Log.i("ZR Panne", objC
                                        .getProperty("lblDate").toString()
                                        .subSequence(0, 10).toString());

                                if (objC.getProperty("lblDescription")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setDescription("");
                                } else {
                                    objPanne.setDescription(objC.getProperty(
                                            "lblDescription").toString());
                                }

                                if (objC.getProperty("lblType")
                                        .toString().equals("anyType{}")) {
                                    TypePanne t = new TypePanne();
                                    t.setNomPanne("");
                                    objPanne.setTypePanne(t);
                                } else {
                                    TypePanne tp = new TypePanne();
                                    tp.setNomPanne(objC.getProperty(
                                            "lblType").toString());
                                    objPanne.setTypePanne(tp);
                                }

                                if (objC.getProperty("lblDuree")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setDuree(0);
                                } else {
                                    objPanne.setDuree(Integer.valueOf(objC.getProperty(
                                            "lblDuree").toString()));
                                }

                                if (objC.getProperty("lblNom")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setNom("");
                                } else {
                                    objPanne.setNom(objC.getProperty(
                                            "lblNom").toString());
                                }


                                if (objC.getProperty("lblPrenom")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setPrenom("");
                                } else {
                                    objPanne.setPrenom(objC.getProperty(
                                            "lblPrenom").toString());
                                }

                                if (objC.getProperty("lblUrgent")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setUrgent(false);
                                } else {
                                    objPanne.setUrgent(Boolean.valueOf(objC.getProperty(
                                            "lblUrgent").toString()));
                                }

                                if (objC.getProperty("lblImage")
                                        .toString().equals("anyType{}")) {
                                    objPanne.setImage(null);
                                } else {
                                    objPanne.setImage(objC.getProperty(
                                            "lblImage").toString());
                                }


                                Log.i("Panne", objPanne.toString());
                                listPanne.add(objPanne);

                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listPanne;
        }

    }

    // Get List Techniciens
    public class AsyncCallWSListTech extends AsyncTask<String, String, ArrayList<TypePanne>> {
        HttpTransportSE androidHttpTransport;

        protected void onPostExecute(ArrayList<TypePanne> listTech) {
            displayPannesSpinner();
            androidHttpTransport.reset();
            super.onPostExecute(listTech);
            Log.i("AsyncCallWSListTech", "onPreExecute");
        }

        protected ArrayList<TypePanne> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 30000);
            try {
                androidHttpTransport.call(SOAP_ACTION1, envelope);
                final SoapObject response = (SoapObject) envelope.getResponse();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TypePanne typePanne;
                        SoapObject Pannes = new SoapObject();
                        SoapObject Panne = new SoapObject();

                        Pannes = (SoapObject) response.getProperty(0);
                        for (int i = 0; i < Pannes.getPropertyCount(); i++) {

                            Panne = (SoapObject) Pannes.getProperty(i);

                            typePanne = new TypePanne();
                            typePanne.setIdPanne(Integer.parseInt(Panne
                                    .getProperty("lblId").toString()));
                            typePanne.setNomPanne(Panne.getProperty(
                                    "lblNom").toString());

                            listTech.add(typePanne);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listTech;
        }
    }
}
