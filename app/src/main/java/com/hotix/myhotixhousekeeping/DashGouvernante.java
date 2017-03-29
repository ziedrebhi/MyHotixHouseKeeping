package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.hotix.myhotixhousekeeping.prevision.ListViewMultiChartActivity;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

public class DashGouvernante extends Activity implements OnClickListener {


    Button room, team, complaint, clientAP, pannes, prev;
    String login, dateFront;
    AlertDialog.Builder adc;
    View layout, v;
    TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_gouvernante);

        room = (Button) findViewById(R.id.btn_rack);
        team = (Button) findViewById(R.id.team_manage);
        complaint = (Button) findViewById(R.id.objTrouves);
        clientAP = (Button) findViewById(R.id.clientAr);
        pannes = (Button) findViewById(R.id.maintenance);
        prev = (Button) findViewById(R.id.prev);

        login = UserInfoModel.getInstance().getLogin();
        int profile_id = UserInfoModel.getInstance().getUser().getData().getProfileId();
        if (profile_id == 16) {
            // Maintenance
            complaint.setVisibility(View.INVISIBLE);
            team.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        room.setOnClickListener(this);
        team.setOnClickListener(this);
        complaint.setOnClickListener(this);
        clientAP.setOnClickListener(this);
        pannes.setOnClickListener(this);
        prev.setOnClickListener(this);
        dateFront = UserInfoModel.getInstance().getUser().getData().getDateFront();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rack:
                Intent i = new Intent(this, RoomRack.class);
                i.putExtra("login", login);
                this.startActivity(i);
                finish();
                break;

            case R.id.team_manage:
                if (UserInfoModel.getInstance().getUser().getData().isHasFM()) {
                    Intent i1 = new Intent(this, FemmeMenage.class);
                    i1.putExtra("login", login);
                    i1.putExtra("dateFront", dateFront);
                    this.startActivity(i1);
                } else {
                    ShowAlert(getResources().getString(R.string.msg_connecting_not_authorised));
                }
                break;

            case R.id.objTrouves:
                Intent i2 = new Intent(this, ObjetsTrouveListActivity.class);
                i2.putExtra("login", login);
                i2.putExtra("dateFront", dateFront);
                this.startActivity(i2);
                finish();
                break;

            case R.id.clientAr:
                Intent i3 = new Intent(this, ListeClientsAP.class);
                i3.putExtra("login", login);
                i3.putExtra("dateFront", dateFront);
                this.startActivity(i3);
                finish();
                break;
            case R.id.maintenance:
                Intent i4 = new Intent(this, PannesListActivity.class);
                i4.putExtra("login", login);
                i4.putExtra("dateFront", dateFront);
                this.startActivity(i4);
                finish();
                break;
            case R.id.prev:
                Intent i5 = new Intent(this, ListViewMultiChartActivity.class);
                this.startActivity(i5);

                break;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash, menu);
        if (!UserInfoModel.getInstance().getUser().getData().isHasConfig()) {
            MenuItem item = menu.findItem(R.id.config);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.close:

                openDialogConnexion();
                //finish();
                break;
            case R.id.config:

                Intent i5 = new Intent(this, SettingsActivity.class);
                this.startActivity(i5);

                //finish();
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void openDialogConnexion() {

        adc = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.deconnexion, null);
        adc.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        adc.setPositiveButton(getResources().getString(R.string.oui),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), Connexion.class);
                        startActivity(i);
                        finish();
                    }
                });

        adc.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        adc.show();
    }


    @Override
    public void onBackPressed() {
        openDialogConnexion();
    }

}
