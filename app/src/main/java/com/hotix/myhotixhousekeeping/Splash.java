package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
public class Splash extends Activity {
    protected Animation fadeIn;
    protected ImageView logo;
    TextView nom_app;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        logo = (ImageView) findViewById(R.id.logoSplash);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        logo.setVisibility(View.VISIBLE);
        Thread loading = new Thread() {
            public void run() {
                try {
                    logo.startAnimation(fadeIn);
                    sleep(3000);
                    Intent main = new Intent(Splash.this, Connexion.class);
                    startActivity(main);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();

                }
            }
        };

        loading.start();
    }
}
