package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class Splash extends Activity {
    TextView nom_app;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        nom_app = (TextView) findViewById(R.id.textMH);
        version = (TextView) findViewById(R.id.version);

        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        nom_app.setTypeface(font);
        version.setText("Hotix © \nVersion " + BuildConfig.VERSION_NAME);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(Splash.this, Connexion.class);
                startActivity(i);
                finish();
            }
        }, 3000);
    }
}
