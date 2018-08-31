package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.adapter.CustomAdapterSpinnerEtage;
import com.hotix.myhotixhousekeeping.adapter.CustomGrid;
import com.hotix.myhotixhousekeeping.entities.Etage;
import com.hotix.myhotixhousekeeping.entities.Guest;
import com.hotix.myhotixhousekeeping.entities.RackModel;
import com.hotix.myhotixhousekeeping.entities.RackRoomData;
import com.hotix.myhotixhousekeeping.entities.SuccessModel;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;
import com.hotix.myhotixhousekeeping.utils.UserInfoModel;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoomRack extends Activity {

    static CustomProgressDialog progressDialog;
    static RackRoomData CHB;
    static int idStatutNew;
    static String commentHS = "", roomNb;
    AlertDialog.Builder builderMenu, legende, builderMenuEtat, builderHS, builderMenuGuests, builderMenuEtatLieu;
    TextView header, room_number;
    View v;
    GridView grid;
    List<RackRoomData> listRoom;
    RadioButton radioEtat, radioReclamer, vac_clean, vac_dirty, occ_clean,
            occ_dirty, out_of_order, radioGuest;
    String login;
    EditText hsComment;
    Boolean b = true;

    Spinner listEtagesSpin;
    List<com.hotix.myhotixhousekeeping.entities.Etage> etages;
    ListView listGuests;
    /*
    Menus
     */
    RadioButton radioEtatLieu;
    Boolean etatTv = true, etatBar = true, etatServ = true;
    /*
    * Get RackRoom
    */
    String TAG = this.getClass().getSimpleName();
    String EtageId = "-1", BlocId = "-1";
    ProgressDialog pd;
    private CustomAdapterSpinnerEtage adapterEtages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_rack);
        // overridePendingTransition(R.anim.move_left_out_activity, R.anim.move_right_in_activity);
        pd = new ProgressDialog(RoomRack.this);

        /* Grid View for RoomRack */
        grid = (GridView) findViewById(R.id.grid);
        float scalefactor = getResources().getDisplayMetrics().density * 100;
        int number = getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number / (float) scalefactor);
        grid.setNumColumns(columns);
        listRoom = new ArrayList<RackRoomData>();
        etages = new ArrayList<Etage>();

        etages.addAll(UserInfoModel.getInstance().getUser().getData().getEtages());
        etages.add(new Etage(-1, -1, getResources().getString(R.string.all)));

        /* Etages Filter*/
        //etages = UserInfoModel.getInstance().getUser().getData().getEtages();
        listEtagesSpin = (Spinner) findViewById(R.id.listEtages);
        adapterEtages = new CustomAdapterSpinnerEtage(RoomRack.this,
                android.R.layout.simple_dropdown_item_1line, etages);
        listEtagesSpin.setAdapter(adapterEtages);

        listEtagesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                com.hotix.myhotixhousekeeping.entities.Etage etage = adapterEtages.getItem(position);
                EtageId = String.valueOf(etage.getId());
                BlocId = String.valueOf(etage.getBlocId());
                listRoom = new ArrayList<RackRoomData>();
                if (isConnected())
                    new HttpRequestTaskRackRoom().execute();
                else ShowAlert(getResources().getString(R.string.msg_acces_denied));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rack, menu);
        if (!UserInfoModel.getInstance().getUser().getData().isHasMouchard()) {
            MenuItem item = menu.findItem(R.id.mouchard_rack);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infos_rack:
                showLegend();
                break;
            case R.id.mouchard_rack:
                showMouchard();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onResume() {

        CHB = new RackRoomData();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CHB = listRoom.get(position);
                roomNb = CHB.getNumChb();
                if (UserInfoModel.getInstance().getUser().getData().isHasChangeStatut()
                        || UserInfoModel.getInstance().getUser().getData().isHasEtatLieu()
                        || UserInfoModel.getInstance().getUser().getData().isHasViewClient()
                        || UserInfoModel.getInstance().getUser().getData().isHasAddPanne()
                        ) {

                    showRoomMenu();
                }


            }
        });

        super.onResume();
    }

    public boolean HasOnlyView() {
        return (!UserInfoModel.getInstance().getUser().getData().isHasChangeStatut()
                && !UserInfoModel.getInstance().getUser().getData().isHasEtatLieu()
                && UserInfoModel.getInstance().getUser().getData().isHasViewClient()
                && !UserInfoModel.getInstance().getUser().getData().isHasAddPanne()
        );
    }

    public void showMouchard() {
        Intent i = new Intent(getApplicationContext(),
                MouchardRackListActivity.class);

        startActivity(i);
    }

    private void showRoomMenu() {

        builderMenu = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.menu_chambre, null);
        builderMenu.setView(v);


        header = (TextView) v.findViewById(R.id.textHeader);

        radioEtat = (RadioButton) v.findViewById(R.id.radioEtat);
        radioReclamer = (RadioButton) v.findViewById(R.id.radioComplaint);
        radioGuest = (RadioButton) v.findViewById(R.id.radioCViewGuests);
        radioEtatLieu = (RadioButton) v.findViewById(R.id.radioEtatLieu);

        if (!UserInfoModel.getInstance().getUser().getData().isHasChangeStatut())
            radioEtat.setVisibility(View.GONE);
        if (!UserInfoModel.getInstance().getUser().getData().isHasAddPanne())
            radioReclamer.setVisibility(View.GONE);
        if (!UserInfoModel.getInstance().getUser().getData().isHasViewClient())
            radioGuest.setVisibility(View.GONE);
        if (!UserInfoModel.getInstance().getUser().getData().isHasEtatLieu())
            radioEtatLieu.setVisibility(View.GONE);

        if (!(CHB.getStatutId() == 1
                || CHB.getStatutId() == 2
                || CHB.getStatutId() == 3
                || CHB.getStatutId() == 4
                || CHB.getStatutId() == 5
                || CHB.getStatutId() == 6
                || CHB.getStatutId() == 7
                || CHB.getStatutId() == 8)) {
            radioEtat.setVisibility(View.GONE);

        }

        if (CHB.getStatutId() == 1 || CHB.getStatutId() == 2 || CHB.getStatutId() == 7) {
            radioEtatLieu.setVisibility(View.GONE);
        }
        if (CHB.getGuests().size() == 0) {
            radioGuest.setVisibility(View.GONE);
            radioEtat.setChecked(true);
        }
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number.setText("" + roomNb);
        builderMenu.setPositiveButton(getResources().getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (radioEtat.isChecked()) {
                                if (CHB.getStatutId() == 1
                                        || CHB.getStatutId() == 2
                                        || CHB.getStatutId() == 3
                                        || CHB.getStatutId() == 4
                                        || CHB.getStatutId() == 5
                                        || CHB.getStatutId() == 6
                                        || CHB.getStatutId() == 7
                                        || CHB.getStatutId() == 8) {
                                    showMenuEtat();
                                } else {
                                    ShowAlert(getResources().getString(R.string.msg_change_etat_cannot));
                                }
                            } else if (radioReclamer.isChecked()) {
                                Intent i = new Intent(getApplicationContext(),
                                        DeclarationPanne.class);
                                i.putExtra("prod_id", CHB.getProdId());
                                i.putExtra("num_chb", CHB.getNumChb());
                                i.putExtra("login", login);
                                i.putExtra("room", true);
                                startActivity(i);
                                //finish();
                            } else if (radioGuest.isChecked()) {
                                showMenuGuests();

                            } else if (radioEtatLieu.isChecked()) {
                                showMenuEtatLieu();
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
        if (!(HasOnlyView() && (CHB.getStatutId() == 7 || CHB.getStatutId() == 1 || CHB.getStatutId() == 2))) {
            builderMenu.show();
        }

    }

    private void showMenuEtat() {
        builderMenuEtat = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.etat_chambre_menu, null);
        builderMenuEtat.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        room_number.setText("" + roomNb);
        vac_clean = (RadioButton) v.findViewById(R.id.vac_clean);
        vac_dirty = (RadioButton) v.findViewById(R.id.vac_dirty);
        occ_clean = (RadioButton) v.findViewById(R.id.occ_clean);
        occ_dirty = (RadioButton) v.findViewById(R.id.occ_dirty);
        out_of_order = (RadioButton) v.findViewById(R.id.out_of_order);

        switch (CHB.getStatutId()) {
            case 1:
                vac_clean.setChecked(true);
                break;
            case 2:
                vac_dirty.setChecked(true);
                break;
            case 3:
                occ_clean.setChecked(true);
                break;
            case 4:
                occ_dirty.setChecked(true);
                break;
            case 7:
                out_of_order.setChecked(true);
                break;
        }

        builderMenuEtat.setPositiveButton(getResources().getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (vac_clean.isChecked()) {
                                idStatutNew = 1;
                            } else if (vac_dirty.isChecked()) {
                                idStatutNew = 2;
                            } else if (occ_clean.isChecked()) {
                                idStatutNew = 3;
                            } else if (occ_dirty.isChecked()) {
                                idStatutNew = 4;
                            } else if (out_of_order.isChecked()) {
                                idStatutNew = 7;
                            }

                            if (idStatutNew == 7) {
                                if (AllowChange()) {
                                    setCommentHS();
                                } else {
                                    ShowAlert(getResources().getString(R.string.msg_change_etat_error));
                                }
                            } else {
                                if (AllowChange()) {
                                    if (isConnected())
                                        new HttpRequestTaskUpdateStatutRoom().execute();
                                    else
                                        ShowAlert(getResources().getString(R.string.msg_acces_denied));

                                } else {
                                    ShowAlert(getResources().getString(R.string.msg_change_etat_error));
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showRoomMenu();
                    }
                });
        builderMenuEtat.show();
    }

    private void showMenuGuests() {
        builderMenuGuests = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.etat_view_guests, null);
        builderMenuGuests.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        listGuests = (ListView) v.findViewById(R.id.listGuest);
        room_number.setText("" + roomNb);
        String[] values = new String[CHB.getGuests().size()];
        List<Guest> guests = CHB.getGuests();
        for (int i = 0; i < CHB.getGuests().size(); i++) {
            values[i] = guests.get(i).getName();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listGuests.setAdapter(adapter);
        builderMenuGuests.setPositiveButton(getResources().getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        showRoomMenu();
                    }
                });
        builderMenuGuests.show();
    }

    private void showMenuEtatLieu() {
        builderMenuEtatLieu = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.etat_view_lieu, null);
        builderMenuEtatLieu.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        room_number.setText("" + roomNb);
        final Switch tv = (Switch) v.findViewById(R.id.tv);
        final Switch minibar = (Switch) v.findViewById(R.id.minibar);
        final Switch serv = (Switch) v.findViewById(R.id.serviette);
        tv.setTextOn(getResources().getString(R.string.etat_lieu_ok));
        tv.setTextOff(getResources().getString(R.string.etat_lieu_ko));
        minibar.setTextOn(getResources().getString(R.string.etat_lieu_ok));
        minibar.setTextOff(getResources().getString(R.string.etat_lieu_ko));
        serv.setTextOn(getResources().getString(R.string.etat_lieu_ok));
        serv.setTextOff(getResources().getString(R.string.etat_lieu_ko));

        serv.setChecked(CHB.isEtatServiette());
        tv.setChecked(CHB.isEtatTV());
        minibar.setChecked(CHB.isEtatBar());

        builderMenuEtatLieu.setPositiveButton(getResources().getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        etatBar = minibar.isChecked();
                        etatTv = tv.isChecked();
                        etatServ = serv.isChecked();

                        new HttpRequestTaskUpdateEtatLieu().execute();
                    }
                }).setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showRoomMenu();
                    }
                });
        builderMenuEtatLieu.show();
    }

    private void showLegend() {
        legende = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.custom, null);
        legende.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);

        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);

        legende.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        legende.show();
    }

    private Boolean AllowChange() {
        switch (CHB.getStatutId()) {
            case 1:
                b = (idStatutNew != 2 && idStatutNew != 7) ? false : true;
                break;
            case 2:
                b = (idStatutNew != 1 && idStatutNew != 7) ? false : true;
                break;

            case 3:
                b = (idStatutNew != 4) ? false : true;
                break;

            case 4:
                b = (idStatutNew != 3) ? false : true;
                break;

            case 7:
                b = (idStatutNew != 1 && idStatutNew != 2) ? false : true;
                break;
        }

        return b;
    }

    private void setCommentHS() {
        builderHS = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.comment_hs, null);
        builderHS.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number = (TextView) v.findViewById(R.id.chb_menu);
        room_number.setText("" + roomNb);
        hsComment = (EditText) v.findViewById(R.id.comment_hs);
        builderHS.setPositiveButton(getResources().getString(R.string.OK),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (hsComment.getText().toString().trim().length() == 0) {
                            showToast(getResources().getString(R.string.comment_requis));
                            setCommentHS();
                        } else {
                            commentHS = hsComment.getText().toString();
                            new HttpRequestTaskUpdateStatutRoom().execute();
                        }

                    }
                }).setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        commentHS = "";
                    }
                });
        builderHS.show();
    }

    private void showToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
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

        Log.i("URL", URL.toString());
        return URL;
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

    private class HttpRequestTaskRackRoom extends AsyncTask<Void, Void, RackModel> {
        RackModel response = null;
        String etageId, blocId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);

            etageId = EtageId;
            blocId = BlocId;


        }

        @Override
        protected RackModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "GetEtatRackRoom?etage=" + etageId + "&bloc=" + blocId;
                Log.i("URL", url.toString());
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.setErrorHandler(new MyErrorHandler());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                try {
                    response = restTemplate.getForObject(url, RackModel.class);
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
        protected void onPostExecute(RackModel greeting) {
            pd.dismiss();
            if (response != null) {
                if (response.isStatus()) {
                    // Ok
                    grid.setAdapter(new CustomGrid(RoomRack.this, response.getData()));
                    listRoom = response.getData();
                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_loading_error));
                }

            } else

            {
                ShowAlert(getResources().getString(R.string.msg_loading_error));
            }


        }
    }

    /*
    Update Status Room
    */
    private class HttpRequestTaskUpdateStatutRoom extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;
        String etageId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);


            etageId = EtageId;


        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "ChangerProduitStaut?user=" + UserInfoModel.getInstance().getLogin() +
                        "&prodId=" + CHB.getProdId() +
                        "&typeHebId=" + CHB.getTypeHebergement() +
                        "&typeProdId=" + CHB.getTypeProduitId() +
                        "&statut=" + idStatutNew +
                        "&oldStatut=" + CHB.getStatutId() +
                        "&comment=" + commentHS;
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
                    new HttpRequestTaskRackRoom().execute();

                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.msg_change_etat_error_srv));
                }

            } else

            {
                ShowAlert(getResources().getString(R.string.msg_connecting_error));
            }


        }
    }

    /*
    Update Etat Lieu
    */
    private class HttpRequestTaskUpdateEtatLieu extends AsyncTask<Void, Void, SuccessModel> {
        SuccessModel response = null;
        String etageId;
        int bar = 1, tv = 1, serv = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(2);
            if (!etatBar)
                bar = 0;
            if (!etatServ)
                serv = 0;
            if (!etatTv)
                tv = 0;
        }

        @Override
        protected SuccessModel doInBackground(Void... params) {
            try {
                final String url = getURLAPI() + "UpdateEtatLieu?" +
                        "&prodId=" + CHB.getProdId() +
                        "&etatTv=" + tv +
                        "&etatBar=" + bar +
                        "&etatSer=" + serv;
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
                    //ShowAlert(getResources().getString(R.string.etat_lieu_update_ok));
                    new HttpRequestTaskRackRoom().execute();
                } else {
                    // Error
                    ShowAlert(getResources().getString(R.string.etat_lieu_update_ko));
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


