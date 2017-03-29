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
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class ViewPanneActivity extends Activity {
    TextView desc, nom, prenom, duree;
    TextView type, lieu, imgView, tech;
    Button close;
    CheckBox urg;
    String lieuP, Nom, Prenom, typeP, description, image, technicien;
    String dureeP;
    Boolean isUrgent = false;
    Bitmap bitmap;
    LinearLayout linearImage, linearCloture;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_panne);
        desc = (TextView) findViewById(R.id.comment);
        nom = (TextView) findViewById(R.id.nom);
        prenom = (TextView) findViewById(R.id.prenom);
        duree = (TextView) findViewById(R.id.dureeT);
        urg = (CheckBox) findViewById(R.id.cbxUrgent);
        linearImage = (LinearLayout) findViewById(R.id.LinearLayoutImage);

        linearCloture = (LinearLayout) findViewById(R.id.LinearLayoutCloture);
        duree.setEnabled(false);
        nom.setEnabled(false);
        prenom.setEnabled(false);
        desc.setEnabled(false);
        urg.setEnabled(false);

        type = (TextView) findViewById(R.id.spTypePanne);
        lieu = (TextView) findViewById(R.id.lieu);
        tech = (TextView) findViewById(R.id.textViewImaage2);
        imgView = (TextView) findViewById(R.id.textViewImaage);

        close = (Button) findViewById(R.id.btn_update);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgView.setVisibility(View.GONE);
        SpannableString string = new SpannableString(getResources().getString(R.string.image_saved));
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        imgView.setText(string);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("CAMERA", "Click View Image");

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewPanneActivity.this);
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

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onResume() {
        Bundle extras = getIntent().getExtras();
        lieuP = extras.getString("lieu");
        Nom = extras.getString("nom");
        Prenom = extras.getString("prenom");
        description = extras.getString("description");
        technicien = extras.getString("technicien");
        dureeP = extras.getString("duree", "");
        isUrgent = extras.getBoolean("urgent", false);
        bitmap = getIntent().getParcelableExtra("image");
        Log.i("DUREE", String.valueOf(dureeP));
        typeP = extras.getString("type");
        if (!technicien.equals("")) {
            linearCloture.setVisibility(View.VISIBLE);
            tech.setText(technicien);
        }

        nom.setText(Nom);
        prenom.setText(Prenom);
        duree.setText(String.valueOf(dureeP));
        desc.setText(description);
        type.setText(typeP);
        lieu.setText(lieuP);
        urg.setChecked(isUrgent);
        if (bitmap == null) {
            imgView.setVisibility(View.GONE);
        } else {
            imgView.setVisibility(View.VISIBLE);
        }
        if (getAuthoriseImageSend()) {
            linearImage.setVisibility(View.VISIBLE);
        } else {
            linearImage.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
