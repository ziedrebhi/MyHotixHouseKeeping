package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

public class ViewObjetTrouveActivity extends Activity {

    String lieu, Nom, Prenom, RNom, RPrenom, commentaire, description, login,
            dateExtra, image;
    int idOT;
    EditText lieuUpdate, descriptionUpdate, comment, nom, prenom, renduNom,
            renduPrenom;
    Button btn_update;
    CustomProgressDialog progressDialog;
    TextView imgView;
    Bitmap bitmap;
    LinearLayout imageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_objet_trouve);

        lieuUpdate = (EditText) findViewById(R.id.lieu);
        descriptionUpdate = (EditText) findViewById(R.id.description);
        comment = (EditText) findViewById(R.id.comment);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        renduNom = (EditText) findViewById(R.id.nomRendu);
        renduPrenom = (EditText) findViewById(R.id.prenomRendu);
        btn_update = (Button) findViewById(R.id.btn_update);
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        imgView = (TextView) findViewById(R.id.textViewImaage);
        imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
        imgView.setVisibility(View.GONE);
        SpannableString string = new SpannableString(getResources().getString(R.string.image_saved));
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        imgView.setText(string);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAMERA", "Click View Image");

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewObjetTrouveActivity.this);
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
                        Bitmap icon = bitmap;
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

    public Bitmap getImage() {
        try {

            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return decodedByte;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    protected void onResume() {
        try {
            Bundle extras = getIntent().getExtras();
            dateExtra = extras.getString("dateFront");
            login = extras.getString("login");
            lieu = extras.getString("lieu");
            idOT = extras.getInt("idOT");
            Nom = extras.getString("Nom");
            Prenom = extras.getString("Prenom");
            RNom = extras.getString("RNom");
            RPrenom = extras.getString("RPrenom");
            commentaire = extras.getString("commentaire");
            description = extras.getString("description");
            bitmap = getIntent().getParcelableExtra("image");
            lieuUpdate.setText(lieu);
            descriptionUpdate.setText(description);
            if (commentaire == null) {
                comment.setText("");
            } else {
                comment.setText(commentaire);
            }

            nom.setText(Nom);
            prenom.setText(Prenom);
            renduNom.setText(RNom);
            renduPrenom.setText(RPrenom);
            nom.setEnabled(false);
            prenom.setEnabled(false);
            renduNom.setEnabled(false);
            renduPrenom.setEnabled(false);
            comment.setEnabled(false);
            descriptionUpdate.setEnabled(false);
            lieuUpdate.setEnabled(false);
            btn_update.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            if (bitmap == null) {
                imgView.setVisibility(View.GONE);
            } else {
                imgView.setVisibility(View.VISIBLE);
            }


            if (getAuthoriseImageSend()) {
                imageLayout.setVisibility(View.VISIBLE);
            } else {
                imageLayout.setVisibility(View.GONE);

            }
            super.onResume();
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
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

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }
}
