package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.model.TypePanne;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DeclarationPanne extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "MY_HOTIX_HOUSEKEEPING";
    static int num_chb;
    static String NameFile = "";
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/GetListeTypesPannes";
    public final String METHOD_NAME = "GetListeTypesPannes";
    public final String SOAP_ACTION2 = "http://tempuri.org/ReclamerPanne";
    public final String METHOD_NAME2 = "ReclamerPanne";
    Spinner spinner;
    List<String> types;
    ArrayList<TypePanne> listTypePanne;
    EditText numCHB, dureeTraitement, nomDeclaration, prenomDeclaration, description;
    CheckBox urgent;
    int prodId;
    String login, nameImage;
    Button btnReclamer;
    CustomProgressDialog progressDialog;
    TextView btnViewImage;
    int typePanneId;
    int idActivite = 0;
    String NumChb = "-1";
    private Uri fileUri;

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
            NameFile = "Room_" + num_chb + "_IMG_" + timeStamp + ".jpg";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "Room_" + num_chb + "_IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declaration_panne);

        listTypePanne = new ArrayList<TypePanne>();
        spinner = (Spinner) findViewById(R.id.spTypePanne);
        types = new ArrayList<String>();
        numCHB = (EditText) findViewById(R.id.numCHB);
        dureeTraitement = (EditText) findViewById(R.id.dureeTraitement);
        nomDeclaration = (EditText) findViewById(R.id.nomDeclaration);
        prenomDeclaration = (EditText) findViewById(R.id.prenomDeclaration);
        description = (EditText) findViewById(R.id.description);
        urgent = (CheckBox) findViewById(R.id.cbxUrgent);
        btnReclamer = (Button) findViewById(R.id.btn_reclamer);
        btnViewImage = (TextView) findViewById(R.id.image);
        btnViewImage.setVisibility(View.GONE);
        SpannableString string = new SpannableString(getResources().getString(R.string.image_saved));
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        btnViewImage.setText(string);
        btnViewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAMERA", "Click View Image");

                AlertDialog.Builder builder = new AlertDialog.Builder(DeclarationPanne.this);
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
    }

    public Bitmap getImageFromSD() {
        try {
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
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onResume() {

        progressDialog = new CustomProgressDialog(this, R.drawable.loading);

        Bundle extras = getIntent().getExtras();
        num_chb = extras.getInt("num_chb");
        prodId = extras.getInt("prod_id");
        idActivite = extras.getInt("Activite");
        login = extras.getString("login");
        if (num_chb == -1) {
            // numCHB.setFocusable(true);
        } else {
            numCHB.setFocusable(false);
            numCHB.setText(String.valueOf(num_chb));
        }
        dureeTraitement.setText("0");

        AsyncCallWS ws = new AsyncCallWS();
        ws.execute();

        btnReclamer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ChapmsRequis()) {
                    TypePanne tech = (TypePanne) spinner.getSelectedItem();
                    typePanneId = tech.getIdPanne();
                    if (num_chb == -1) {
                        NumChb = numCHB.getText().toString();
                    }
                    AsyncReclamerPanne wsPanne = new AsyncReclamerPanne();
                    wsPanne.execute();
                }
            }
        });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.panne, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo_panne:
                if (!isDeviceSupportCamera()) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Your device doesn't support camera",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.i("Camera Panne", "Capturer Image");
                    captureImage();
                }
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onBackPressed() {
        if (idActivite == 1) {
            Intent i = new Intent(getApplicationContext(), PannesList.class);
            i.putExtra("login", login);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(getApplicationContext(), RoomRack.class);
            i.putExtra("login", login);
            startActivity(i);
            finish();
        }
        super.onBackPressed();
    }

    private void MessageErreurServeur() {
        try {
            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Erreur de connexion au serveur ! \n Veuillez réessayer s'il vous plait.",
                    Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ResaReclamation(Boolean success) {
        try {
            if (success) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_layout_id));

                TextView text = (TextView) layout.findViewById(R.id.textResult);
                text.setText(getResources().getString(R.string.succes_panne));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                if (idActivite == 1) {
                    Intent i = new Intent(getApplicationContext(), PannesList.class);
                    i.putExtra("login", login);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getApplicationContext(), RoomRack.class);
                    i.putExtra("login", login);
                    startActivity(i);
                }
                finish();
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_error,
                        (ViewGroup) findViewById(R.id.custom_toast_echec));

                TextView text = (TextView) layout.findViewById(R.id.textError);
                text.setText(getResources().getString(R.string.error_panne));

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayPannesSpinner() {
        try {
            for (int i = 0; i < listTypePanne.size(); i++) {
                types.add(listTypePanne.get(i).getNomPanne());
            }
            ArrayAdapter<TypePanne> dataAdapter = new ArrayAdapter<TypePanne>(this,
                    android.R.layout.simple_spinner_item, listTypePanne);
            dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openProg() {
        try {
            progressDialog.setCancelable(true);
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getURL() {
        String URL = null;
        try {
            SharedPreferences sp = PreferenceManager
                    .getDefaultSharedPreferences(this);
            URL = sp.getString("serveur", "");
            URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return URL;
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

    private boolean ChapmsRequis() {
        if (dureeTraitement.getText().toString().length() == 0) {
            dureeTraitement.setError("Chapms  requis");
        }
        if (nomDeclaration.getText().toString().length() == 0) {
            nomDeclaration.setError("Chapms  requis");
        }
        if (prenomDeclaration.getText().toString().length() == 0) {
            prenomDeclaration.setError("Chapms  requis");
        }

        if (description.getText().toString().length() == 0) {
            description.setError("Chapms  requis");
        }

        if (dureeTraitement.getText().toString().length() == 0
                || nomDeclaration.getText().toString().length() == 0
                || prenomDeclaration.getText().toString().length() == 0
                || description.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public class AsyncCallWS extends AsyncTask<String, String, ArrayList<TypePanne>> {
        HttpTransportSE androidHttpTransport;

        protected void onPostExecute(ArrayList<TypePanne> listTypePanne) {
            displayPannesSpinner();
            androidHttpTransport.reset();
            super.onPostExecute(listTypePanne);
        }

        protected ArrayList<TypePanne> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL());
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
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
                                    .getProperty("IdTypePanne").toString()));
                            typePanne.setNomPanne(Panne.getProperty(
                                    "lblDescription").toString());

                            listTypePanne.add(typePanne);
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return listTypePanne;
        }
    }

    public class AsyncReclamerPanne extends AsyncTask<String, String, String> {
        HttpTransportSE androidHttpTransport;
        String result = "False";

        @Override
        protected void onPreExecute() {
            openProg();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            androidHttpTransport.reset();
            super.onPostExecute(result);
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

            PropertyInfo pi_prodId = new PropertyInfo();
            pi_prodId.setName("prodId");
            pi_prodId.setValue(prodId);
            Log.i("prodId => ", Integer.toString(prodId));
            pi_prodId.setType(Integer.class);
            request.addProperty(pi_prodId);

            PropertyInfo pi_typePanneId = new PropertyInfo();
            pi_typePanneId.setName("typePanneId");
            pi_typePanneId.setValue(typePanneId);
            Log.i("typePanneId => ", Integer.toString(typePanneId));
            pi_typePanneId.setType(Integer.class);
            request.addProperty(pi_typePanneId);

            PropertyInfo pi_urgent = new PropertyInfo();
            pi_urgent.setName("urgent");
            if (urgent.isChecked()) {
                pi_urgent.setValue(true);
            } else {
                pi_urgent.setValue(false);
            }
            Log.i("urgent => ", pi_urgent.getValue().toString());
            pi_urgent.setType(Boolean.class);
            request.addProperty(pi_urgent);

            PropertyInfo pi_duree = new PropertyInfo();
            pi_duree.setName("duree");
            pi_duree.setValue(Integer.parseInt(dureeTraitement.getText().toString()));
            Log.i("duree => ", dureeTraitement.getText().toString());
            pi_duree.setType(String.class);
            request.addProperty(pi_duree);

            PropertyInfo pi_lieu = new PropertyInfo();
            pi_lieu.setName("lieu");
            if (num_chb == -1) {
                pi_lieu.setValue(NumChb);
            } else {
                pi_lieu.setValue(num_chb);
            }
            //Log.i("lieu => ", Integer.toString(num_chb));
            pi_lieu.setType(String.class);
            request.addProperty(pi_lieu);

            PropertyInfo pi_nom = new PropertyInfo();
            pi_nom.setName("nom");
            pi_nom.setValue(nomDeclaration.getText().toString());
            Log.i("nom => ", nomDeclaration.getText().toString());
            pi_nom.setType(String.class);
            request.addProperty(pi_nom);

            PropertyInfo pi_prenom = new PropertyInfo();
            pi_prenom.setName("prenom");
            pi_prenom.setValue(prenomDeclaration.getText().toString());
            Log.i("prenom => ", prenomDeclaration.getText().toString());
            pi_prenom.setType(String.class);
            request.addProperty(pi_prenom);

            PropertyInfo pi_user_login = new PropertyInfo();
            pi_user_login.setName("user_login");
            pi_user_login.setValue(login);
            Log.i("user_login => ", login);
            pi_user_login.setType(String.class);
            request.addProperty(pi_user_login);

            PropertyInfo pi_comment = new PropertyInfo();
            pi_comment.setName("comment");
            pi_comment.setValue(description.getText().toString());
            Log.i("comment => ", description.getText().toString());
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
            } else {
                pi_image.setValue(null);
            }
            pi_image.setType(String.class);
            request.addProperty(pi_image);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
                    result = envelope.getResponse().toString();
                    Log.i("result ======> ", result);
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
                        ResaReclamation(Boolean.parseBoolean(result));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }
}
