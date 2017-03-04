package com.hotix.myhotixhousekeeping;

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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.hotix.myhotixhousekeeping.adapter.ObjetClotureAdapter;
import com.hotix.myhotixhousekeeping.model.ObjetCloture;
import com.hotix.myhotixhousekeeping.quickaction.ActionItem;
import com.hotix.myhotixhousekeeping.quickaction.QuickAction;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObjetsTrouves extends FragmentActivity implements
        OnTabChangeListener {
    public static final int MEDIA_TYPE_IMAGE = 1;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "MH_HOUSEKEEPING_OBJETS_TROUVES";
    private static final int ID_UPDATE = 1;
    private static final int ID_CLOTURE = 2;
    private static final int ID_VIEW = 3;
    static EditText lieu;
    static int idObjT;
    static int y1, y2, m1, m2, d1, d2;
    static String NameFile = "";
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/DeclarerObjetTrouves";
    public final String METHOD_NAME = "DeclarerObjetTrouves";
    public final String SOAP_ACTION2 = "http://tempuri.org/GetListeObjetsTrouvesCloture";
    public final String METHOD_NAME2 = "GetListeObjetsTrouvesCloture";
    public final String SOAP_ACTION3 = "http://tempuri.org/ClotureObjetTrouve";
    public final String METHOD_NAME3 = "ClotureObjetTrouve";
    CustomProgressDialog progressDialog;
    TabHost view;
    ViewAnimator viewAnimator1;
    EditText description, comment, nom, prenom, renduNom, renduPrenom,
            dateDebut, dateFin;
    Button declarer, actualiser;
    String prodId;
    String login, dateExtra;
    int[] Tab;
    ListView lvOT;
    ArrayList<ObjetCloture> listeOC;
    String nameImage;
    TextView btnImTextView;
    TextView btnViewImage, emptyMsg;
    LinearLayout imageLayout;
    String prodNum, Desc, Nom, Prenom, RNom, RPrenom, Lieu, Comment;
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

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            NameFile = "Room_" + lieu.getText().toString() + "_IMG_" + timeStamp + ".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "Room_" + lieu.getText().toString() + "_IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_objets_trouves);
        viewAnimator1 = (ViewAnimator) this.findViewById(R.id.viewFlipperOT);
        Bundle extras = getIntent().getExtras();
        login = extras.getString("login");
        lvOT = (ListView) findViewById(R.id.listOC);
        listeOC = new ArrayList<ObjetCloture>();
        view = (TabHost) findViewById(android.R.id.tabhost);
        view.setOnTabChangedListener(this);
        view.setup();
        addTab("Tab1", getResources().getString(R.string.tab1),
                android.R.drawable.ic_menu_preferences, R.id.tab1);
        addTab("Tab2", getResources().getString(R.string.tab2),
                android.R.drawable.ic_menu_call, R.id.tab2);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        dateDebut = (EditText) findViewById(R.id.dateDeb);
        dateFin = (EditText) findViewById(R.id.dateFin);
        lieu = (EditText) findViewById(R.id.lieu);
        description = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        renduNom = (EditText) findViewById(R.id.nomRendu);
        renduPrenom = (EditText) findViewById(R.id.prenomRendu);
        declarer = (Button) findViewById(R.id.btn_declarer);
        actualiser = (Button) findViewById(R.id.refresh_liste);
        btnViewImage = (TextView) findViewById(R.id.imageSaved);
        emptyMsg = (TextView) findViewById(R.id.emptyMsg);
        emptyMsg.setVisibility(View.GONE);
        btnViewImage.setVisibility(View.GONE);
        SpannableString string = new SpannableString(getResources().getString(R.string.image_saved));
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        btnViewImage.setText(string);
        imageLayout = (LinearLayout) findViewById(R.id.imageLi);
        btnViewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAMERA", "Click View Image");

                AlertDialog.Builder builder = new AlertDialog.Builder(ObjetsTrouves.this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.activity_image_view, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        Log.i("CAMERA", "here");
                        ImageView image = (ImageView) dialog.findViewById(R.id.imageView1);
                        Bitmap icon = getImageFromSD();
                        float imageWidthInPX = (float) image.getWidth();

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                                Math.round(imageWidthInPX * (float) icon.getHeight() / (float) icon.getWidth()));
                        image.setLayoutParams(layoutParams);
                        image.setImageBitmap(icon);

                    }
                });
                dialog.show();
            }
        });

        Intent in = getIntent();
        dateExtra = in.getStringExtra("dateFront");
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

        declarer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ChapmsRequis()) {
                    prodNum = lieu.getText().toString();
                    Desc = description.getText().toString();
                    Nom = nom.getText().toString();
                    Prenom = prenom.getText().toString();
                    RNom = renduNom.getText().toString();
                    RPrenom = renduPrenom.getText().toString();
                    Comment = comment.getText().toString();
                    Lieu = lieu.getText().toString();
                    Log.i("Data", prodNum + Desc + Nom + Prenom + RNom + RPrenom + Comment + Lieu);
                    AsyncDeclareObjetWS ws = new AsyncDeclareObjetWS();
                    ws.execute();
                }
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

        ActionItem updateItem = new ActionItem(ID_UPDATE, getResources()
                .getString(R.string.update), getResources().getDrawable(
                R.drawable.update));
        ActionItem clotureItem = new ActionItem(ID_CLOTURE, getResources()
                .getString(R.string.cloture), getResources().getDrawable(
                R.drawable.cloture));
        ActionItem viewItem = new ActionItem(ID_VIEW, getResources()
                .getString(R.string.view), getResources().getDrawable(
                R.drawable.view));
        final QuickAction mQuickAction = new QuickAction(this);
        mQuickAction.addActionItem(updateItem);
        mQuickAction.addActionItem(clotureItem);
        mQuickAction.addActionItem(viewItem);

        // setup the action item click listener
        mQuickAction
                .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(QuickAction quickAction, int pos,
                                            int actionId) {

                        if (actionId == ID_UPDATE) {
                            try {
                                UpdateObjetTrouve(listeOC.get(mSelectedRow)
                                        .getLieu(), listeOC.get(mSelectedRow)
                                        .getIdOT(), listeOC.get(mSelectedRow)
                                        .getNom(), listeOC.get(mSelectedRow)
                                        .getPrenom(), listeOC.get(mSelectedRow)
                                        .getRNom(), listeOC.get(mSelectedRow)
                                        .getRPrenom(), listeOC.get(mSelectedRow)
                                        .getCommentaire(), listeOC
                                        .get(mSelectedRow).getDescription());
                            } catch (Exception ex) {
                                Log.e("Excetiion", ex.toString());
                            }
                        } else if (actionId == ID_CLOTURE) {
                            idObjT = listeOC.get(mSelectedRow).getIdOT();
                            AsyncClotureObjetWS cloture = new AsyncClotureObjetWS();
                            cloture.execute();
                        } else {
                            try {
                                ViewObjetTrouve(listeOC.get(mSelectedRow)
                                        .getLieu(), listeOC.get(mSelectedRow)
                                        .getIdOT(), listeOC.get(mSelectedRow)
                                        .getNom(), listeOC.get(mSelectedRow)
                                        .getPrenom(), listeOC.get(mSelectedRow)
                                        .getRNom(), listeOC.get(mSelectedRow)
                                        .getRPrenom(), listeOC.get(mSelectedRow)
                                        .getCommentaire(), listeOC
                                        .get(mSelectedRow).getDescription(), listeOC
                                        .get(mSelectedRow).getImage());
                            } catch (Exception ex) {
                                Log.e("Excetiion", ex.toString());
                            }
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

    }

    public Bitmap getImageFromSD() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        String photoPath = mediaStorageDir.getPath() + "/" + nameImage;
        Log.i("Camera ", "Path image " + photoPath);
        Bitmap icon = BitmapFactory.decodeFile(photoPath, options);
        return icon;
    }

    private void addTab(String tag, String title, int icon, int content) {
        TabSpec spec = view.newTabSpec(tag);
        spec.setIndicator(title, getResources().getDrawable(icon));
        spec.setContent(content);
        view.addTab(spec);
    }

    @Override
    protected void onResume() {

        if (getAuthoriseImageSend()) {
            imageLayout.setVisibility(View.VISIBLE);

        } else {
            imageLayout.setVisibility(View.GONE);
        }
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
            i.putExtra("login", login);
            Log.i("login", login);
            i.putExtra("dateFront", dateExtra);
            Log.i("dateFront", dateExtra);
            this.startActivity(i);

            Log.i("here", login);
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
            i.putExtra("login", login);
            Log.i("login", login);
            i.putExtra("dateFront", dateExtra);
            Log.i("dateFront", dateExtra);
            if (image != null) {
                i.putExtra("image", scaleDownBitmap(getImageFromString(image), 100, getApplicationContext()));

            } else {
                i.putExtra("image", (Parcelable[]) null);

            }
            this.startActivity(i);

            Log.i("here", login);
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
    public void onTabChanged(String tabId) {
        if (tabId == "Tab2") {
            if (!ControleDate()) {
                ShowToast();
            } else {
                AsyncCallWS ws = new AsyncCallWS();
                ws.execute();
            }
        }
    }

    public void ShowToast() {
        Toast t = Toast.makeText(this,
                "Date erronée ! \n Veuillez entrer une date correcte.",
                Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    private boolean ChapmsRequis() {
        if (lieu.getText().toString().length() == 0) {
            lieu.setError("Chapms  requis");
        }
        if (description.getText().toString().length() == 0) {
            description.setError("Chapms  requis");
        }
        if (nom.getText().toString().length() == 0) {
            nom.setError("Chapms  requis");
        }
        if (prenom.getText().toString().length() == 0) {
            prenom.setError("Chapms  requis");
        }

        if (renduNom.getText().toString().length() == 0) {
            renduNom.setError("Chapms  requis");
        }

        if (renduPrenom.getText().toString().length() == 0) {
            renduPrenom.setError("Chapms  requis");
        }

        if (lieu.getText().toString().length() == 0
                || description.getText().toString().length() == 0
                || nom.getText().toString().length() == 0
                || prenom.getText().toString().length() == 0
                || renduNom.getText().toString().length() == 0
                || renduPrenom.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
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

    private void MessageErreurServeur() {
        Toast t = Toast
                .makeText(
                        getApplicationContext(),
                        "Erreur de connexion au serveur ! \nVeuillez réessayer s'il vous plait.",
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
            text.setText(getResources().getString(R.string.cloture_not_ok));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.panne, menu);
        if (getAuthoriseImageSend()) {
            MenuItem item = menu.findItem(R.id.photo_panne);
            item.setVisible(true);
        } else {
            MenuItem item = menu.findItem(R.id.photo_panne);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_panne:
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                } else {
                    captureImage();
                }

        }

        return super.onMenuItemSelected(featureId, item);
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

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Log.i("CAMERA", "captureImage START");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        Log.i("Camera Panne", "File Uri =" + fileUri);
        Log.i("CAMERA", "captureImage END");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("CAMERA", "onSaveInstanceState START");
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
        Log.i("Camera Panne", "File Uri =" + fileUri);
        Log.i("CAMERA", "onSaveInstanceState END");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("CAMERA", "onRestoreInstanceState START");

        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
        Log.i("Camera Panne", "onRestoreInstanceState " + fileUri);
        Log.i("CAMERA", "onRestoreInstanceState END");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("CAMERA", "onActivityResult START");

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Log.i("Camera Panne", "onActivityResult " + fileUri);
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Log.i("Camera Panne", "onActivityResult " + fileUri);
        }

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        File f = new File(mediaStorageDir, NameFile);
        Log.i("CAMERA", "onActivityResult FILE path :" + f);
        if (f.exists()) {
            nameImage = NameFile;
            btnViewImage.setVisibility(View.VISIBLE);
            Log.i("CAMERA", "onActivityResult FILE exist");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_saved),
                    Toast.LENGTH_LONG).show();
        } else {
            Log.i("CAMERA", "onActivityResult FILE DOES NOT exist");
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_saved_not),
                    Toast.LENGTH_LONG).show();
            btnViewImage.setVisibility(View.GONE);

        }
        Log.i("CAMERA", "onActivityResult END");

    }

    public Uri getOutputMediaFileUri(int type) {
        Log.i("CAMERA", "getOutputMediaFileUri START");
        return Uri.fromFile(getOutputMediaFile(type));
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

    public class AsyncDeclareObjetWS extends AsyncTask<String, String, String> {
        String result = "False";
        HttpTransportSE androidHttpTransport;

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
            pi_prodNum.setValue(null);
            pi_prodNum.setType(String.class);
            request.addProperty(pi_prodNum);

            PropertyInfo objTrouveDesc = new PropertyInfo();
            objTrouveDesc.setName("objTrouveDesc");
            objTrouveDesc.setValue(Desc);
            objTrouveDesc.setType(String.class);
            request.addProperty(objTrouveDesc);

            PropertyInfo objTrouveNom = new PropertyInfo();
            objTrouveNom.setName("objTrouveNom");
            objTrouveNom.setValue(Nom);
            objTrouveNom.setType(String.class);
            request.addProperty(objTrouveNom);

            PropertyInfo objTrouvePrenom = new PropertyInfo();
            objTrouvePrenom.setName("objTrouvePrenom");
            objTrouvePrenom.setValue(Prenom);
            objTrouvePrenom.setType(String.class);
            request.addProperty(objTrouvePrenom);

            PropertyInfo objTrouveLieu = new PropertyInfo();
            objTrouveLieu.setName("objTrouveLieu");
            objTrouveLieu.setValue(Lieu);
            objTrouveLieu.setType(String.class);
            request.addProperty(objTrouveLieu);

            PropertyInfo objRenduTrouveNom = new PropertyInfo();
            objRenduTrouveNom.setName("objTrouveRenduNom");
            objRenduTrouveNom.setValue(RNom);
            objRenduTrouveNom.setType(String.class);
            request.addProperty(objRenduTrouveNom);

            PropertyInfo objRenduTrouvePrenom = new PropertyInfo();
            objRenduTrouvePrenom.setName("objTrouveRenduPrenom");
            objRenduTrouvePrenom.setValue(RPrenom);
            objRenduTrouvePrenom.setType(String.class);
            request.addProperty(objRenduTrouvePrenom);


            PropertyInfo pi_login = new PropertyInfo();
            pi_login.setName("login");
            pi_login.setValue(login);
            pi_login.setType(String.class);
            request.addProperty(pi_login);

            PropertyInfo pi_comment = new PropertyInfo();
            pi_comment.setName("comment");
            if (Comment.equals("")) {
                pi_comment.setValue("");
            } else {
                pi_comment.setValue(Comment);
            }

            pi_comment.setType(String.class);
            request.addProperty(pi_comment);


            PropertyInfo pi_image = new PropertyInfo();
            pi_image.setName("ImageByteArray");
            if (getAuthoriseImageSend()) {
                Bitmap img = getImageFromSD();
                if (img != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    pi_image.setValue(temp);
                    Log.i("ImageByteArray => ", temp);
                } else {
                    pi_image.setValue(null);
                }

                pi_image.setType(String.class);
                request.addProperty(pi_image);
            } else {
                pi_image.setValue(null);
            }
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    result = envelope.getResponse().toString();
                } catch (SocketTimeoutException e) {
                    Log.e("Erreur", e.toString());
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
            progressDialog.dismiss();
            androidHttpTransport.reset();
            super.onPostExecute(result);
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
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo pi_objTrouveId = new PropertyInfo();
            pi_objTrouveId.setName("objTrouveId");
            pi_objTrouveId.setValue(idObjT);
            pi_objTrouveId.setType(Integer.class);
            request.addProperty(pi_objTrouveId);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
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
            AsyncTask<String, String, ArrayList<ObjetCloture>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            progressDialog.setCancelable(true);
            listeOC = new ArrayList<ObjetCloture>();
            progressDialog.show();
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<ObjetCloture> listeOC) {
            lvOT.setAdapter(new ObjetClotureAdapter(getApplicationContext(),
                    listeOC));
            if (listeOC.size() == 0) {
                emptyMsg.setVisibility(View.VISIBLE);
            } else {
                emptyMsg.setVisibility(View.GONE);
            }
            androidHttpTransport.reset();
            progressDialog.dismiss();
            super.onPostExecute(listeOC);
        }

        protected ArrayList<ObjetCloture> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;

            PropertyInfo pi_dateDeb = new PropertyInfo();
            pi_dateDeb.setName("dateDeb");
            pi_dateDeb.setValue(getDate(dateDebut.getText().toString()));
            pi_dateDeb.setType(String.class);
            request.addProperty(pi_dateDeb);

            PropertyInfo pi_dateFin = new PropertyInfo();
            pi_dateFin.setName("dateFin");
            pi_dateFin.setValue(getDate(dateFin.getText().toString()));
            pi_dateFin.setType(String.class);
            request.addProperty(pi_dateFin);

            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
                    response = (SoapObject) envelope.getResponse();
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
                            ObjetCloture objCloture;
                            SoapObject ObjetClotures = new SoapObject();
                            SoapObject objC = new SoapObject();

                            ObjetClotures = (SoapObject) response
                                    .getProperty(0);
                            if (ObjetClotures.toString().equals("anyType{}")) {
                                Toast.makeText(getApplicationContext(),
                                        "Pas d'objets trouvés.",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < ObjetClotures
                                    .getPropertyCount(); i++) {
                                objC = (SoapObject) ObjetClotures
                                        .getProperty(i);

                                objCloture = new ObjetCloture();
                                objCloture.setLieu(objC.getProperty("lblLieu")
                                        .toString());
                                objCloture.setIdOT(Integer.parseInt(objC
                                        .getProperty("lblIdObjetTrouve")
                                        .toString()));
                                objCloture.setDescription(objC.getProperty(
                                        "lblDescription").toString());
                                objCloture.setDateOT(objC
                                        .getProperty("lblDate").toString()
                                        .subSequence(0, 10).toString());
                                Log.i("ZR ObTr", objC
                                        .getProperty("lblDate").toString()
                                        .subSequence(0, 10).toString());

                                objCloture.setNom(objC.getProperty(
                                        "lblTrouveNom").toString());
                                objCloture.setPrenom(objC.getProperty(
                                        "lblTrouvePrenom").toString());

                                if (objC.getProperty("lblRenduNom").equals(
                                        "anyType{}")) {
                                    objCloture.setRNom("");
                                } else {
                                    objCloture.setRNom(objC.getProperty(
                                            "lblRenduNom").toString());
                                }

                                if (objC.getProperty("lblTrouvePrenom")
                                        .toString().equals("anyType{}")) {
                                    objCloture.setRPrenom("");
                                } else {
                                    objCloture.setRPrenom(objC.getProperty(
                                            "lblTrouvePrenom").toString());
                                }

                                if (objC.getProperty("lblCommentaire")
                                        .toString().equals("anyType{}")) {
                                    objCloture.setCommentaire("");
                                } else {
                                    objCloture.setCommentaire(objC.getProperty(
                                            "lblCommentaire").toString());
                                }
                                if (objC.getProperty("lblImage")
                                        .toString().equals("anyType{}")) {
                                    objCloture.setImage(null);
                                } else {
                                    objCloture.setImage(objC.getProperty(
                                            "lblImage").toString());
                                }

                                listeOC.add(objCloture);

                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listeOC;
        }

    }

}
