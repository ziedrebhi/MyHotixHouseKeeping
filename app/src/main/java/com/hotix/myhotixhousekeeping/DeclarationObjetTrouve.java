package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DeclarationObjetTrouve extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "MY_HOTIX_HOUSEKEEPING";
    static String num_chb;
    static String NameFile = "";

    String login, nameImage;
    EditText lieu, description, comment, nom, prenom, renduNom, renduPrenom;
    Button declarer, actualiser;
    TextView btnViewImage;
    LinearLayout imageLayout;
    String prodNum, Desc, Nom, Prenom, RNom, RPrenom, Lieu, Comment;
    Boolean isNew = true;
    String TAG = this.getClass().getSimpleName();
    ProgressDialog pd;
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
        setContentView(R.layout.activity_declaration_objet);
        pd = new ProgressDialog(DeclarationObjetTrouve.this);
        lieu = (EditText) findViewById(R.id.lieu);
        description = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        renduNom = (EditText) findViewById(R.id.nomRendu);
        renduPrenom = (EditText) findViewById(R.id.prenomRendu);
        declarer = (Button) findViewById(R.id.btn_declarer);

        btnViewImage = (TextView) findViewById(R.id.imageSaved);

        btnViewImage.setVisibility(View.GONE);
        SpannableString string = new SpannableString(getResources().getString(R.string.image_saved));
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        btnViewImage.setText(string);
        imageLayout = (LinearLayout) findViewById(R.id.imageLi);
        btnViewImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAMERA", "Click View Image");

                AlertDialog.Builder builder = new AlertDialog.Builder(DeclarationObjetTrouve.this);
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


        declarer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ChampsRequis()) {
                    prodNum = lieu.getText().toString();
                    Desc = description.getText().toString();
                    Nom = nom.getText().toString();
                    Prenom = prenom.getText().toString();
                    RNom = renduNom.getText().toString();
                    RPrenom = renduPrenom.getText().toString();
                    Comment = comment.getText().toString();
                    Lieu = lieu.getText().toString();
                    Log.i("Data", prodNum + Desc + Nom + Prenom + RNom + RPrenom + Comment + Lieu);
                    if (isConnected())
                        new HttpRequestTaskReclamObjet().execute();
                    else {
                        ShowAlert(getResources().getString(R.string.acces_denied));
                    }
                }
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

        if (getAuthoriseImageSend()) {
            imageLayout.setVisibility(View.VISIBLE);

        } else {
            imageLayout.setVisibility(View.GONE);
        }

        super.onResume();
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

        finish();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
        super.onBackPressed();
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

    private boolean ChampsRequis() {
        if (lieu.getText().toString().length() == 0) {
            lieu.setError(getResources().getString(R.string.connect_data));
        }
        if (description.getText().toString().length() == 0) {
            description.setError(getResources().getString(R.string.connect_data));
        }
        if (nom.getText().toString().length() == 0) {
            nom.setError(getResources().getString(R.string.connect_data));
        }
        if (prenom.getText().toString().length() == 0) {
            prenom.setError(getResources().getString(R.string.connect_data));
        }

        if (renduNom.getText().toString().length() == 0) {
            renduNom.setError(getResources().getString(R.string.connect_data));
        }

        if (renduPrenom.getText().toString().length() == 0) {
            renduPrenom.setError(getResources().getString(R.string.connect_data));
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
        return false;
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

    private class HttpRequestTaskReclamObjet extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;

        String image;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);


            if (getAuthoriseImageSend()) {
                Bitmap img = getImageFromSD();
                if (img != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    image = temp;
                    Log.i("ImageByteArray => ", temp);
                } else {
                    image = "";
                }
            } else {
                image = "";
            }


        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "DeclarerObjetTrouves?" +
                        "prodNum=" + prodNum +
                        "&objTrouveDesc=" + Desc +
                        "&objTrouveNom=" + Nom +
                        "&objTrouvePrenom=" + Prenom +
                        "&objTrouveLieu=" + Lieu +
                        "&objTrouveRenduNom=" + RNom +
                        "&objTrouveRenduPrenom=" + RPrenom +
                        "&login=" + UserInfoModel.getInstance().getLogin() +
                        "&comment=" + Comment +
                        "&ImageByteArray=" + image;
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
                //ShowAlert(getResources().getString(R.string.msg_connecting_error));
            }

            return null;
        }

        @Override
        protected void onPostExecute(SuccessModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    ShowAlert(getResources().getString(R.string.msg_objet_create_ok));
                    finish();
                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_objet_create_error));
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
