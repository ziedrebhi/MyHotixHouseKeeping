package com.hotix.myhotixhousekeeping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hotix.myhotixhousekeeping.adapter.CustomFilter;
import com.hotix.myhotixhousekeeping.adapter.CustomGrid;
import com.hotix.myhotixhousekeeping.model.Etage;
import com.hotix.myhotixhousekeeping.model.Room;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class RoomRack extends Activity {

    static CustomProgressDialog progressDialog;
    static Room CHB;
    static int roomNb, idStatutNew, etatCBX = 0;
    static String commentHS = "";
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/GetListEtatChambres";
    public final String METHOD_NAME = "GetListEtatChambres";
    public final String SOAP_ACTION2 = "http://tempuri.org/ChangerProduitStaut";
    public final String METHOD_NAME2 = "ChangerProduitStaut";
    public final String SOAP_ACTION3 = "http://tempuri.org/GetListEtagesHotel";
    public final String METHOD_NAME3 = "GetListEtagesHotel";
    AlertDialog.Builder builderMenu, legende, builderMenuEtat, builderHS;
    TextView header, room_number;
    View v;
    GridView grid, gridFilter;
    ArrayList<Room> listRoom, listRoomFiltre;
    RadioButton radioEtat, radioReclamer, vac_clean, vac_dirty, occ_clean,
            occ_dirty, out_of_order;
    Boolean changed = false;
    String login;
    EditText hsComment;
    Boolean b = true;
    Button refresh;
    ArrayList<Etage> listEtage;
    String strlistEtage;

    private static void openProg() {
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_rack);
        grid = (GridView) findViewById(R.id.grid);
        gridFilter = (GridView) findViewById(R.id.gridFilter);
        refresh = (Button) findViewById(R.id.refresh_room_rack);
        if ((listEtage == null)) {
            strlistEtage = ";-1;";
        }
    }

    @Override
    protected void onResume() {
        listRoomFiltre = new ArrayList<Room>();
        CHB = new Room();
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);
        Bundle extras = getIntent().getExtras();
        login = extras.getString("login");

        grid.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                CHB = listRoom.get(position);
                roomNb = CHB.getNumChb();
                showRoomMenu();

                return false;
            }

        });

        refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                strlistEtage = ";-1;";
                for (int i = 0; i < listEtage.size(); i++) {
                    if (listEtage.get(i).getCHECKED() == true) {
                        strlistEtage = strlistEtage
                                + listEtage.get(i).getIDETAGE() + ";";
                    }
                }

                AsyncCallWS task = new AsyncCallWS();
                task.execute();

            }
        });

        AsyncFilterWS ws = new AsyncFilterWS();
        ws.execute();

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), DashGouvernante.class);
        i.putExtra("login", login);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rack, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.infos_rack:
                showLegend();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
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
            text.setText(getResources()
                    .getString(R.string.succes_change_status));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            Intent i = new Intent(getApplicationContext(), RoomRack.class);
            i.putExtra("login", login);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_error,
                    (ViewGroup) findViewById(R.id.custom_toast_echec));

            TextView text = (TextView) layout.findViewById(R.id.textError);
            text.setText(getResources().getString(R.string.error_change_status));

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    private void showLegend() {
        legende = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.custom, null);
        legende.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);

        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);

        legende.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        legende.show();
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

        builderMenuEtat.setPositiveButton("OK",
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
                                    showToast("Impossible d'effectuer ce changement .");
                                }
                            } else {
                                if (AllowChange()) {
                                    AsyncChangeEtatWS ws = new AsyncChangeEtatWS();
                                    ws.execute();

                                } else {
                                    showToast("Impossible d'effectuer ce changement .");
                                }
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
        builderMenuEtat.show();
    }

    private void showRoomMenu() {
        builderMenu = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        v = inflater.inflate(R.layout.menu_chambre, null);
        builderMenu.setView(v);
        header = (TextView) v.findViewById(R.id.textHeader);

        radioEtat = (RadioButton) v.findViewById(R.id.radioEtat);
        radioReclamer = (RadioButton) v.findViewById(R.id.radioComplaint);

        room_number = (TextView) v.findViewById(R.id.chb_menu);
        Typeface font = Typeface.createFromAsset(getAssets(), "thirsty.otf");
        header.setTypeface(font);
        room_number.setText("" + roomNb);
        builderMenu.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (radioEtat.isChecked()) {
                                if (CHB.getStatutId() == 1
                                        || CHB.getStatutId() == 2
                                        || CHB.getStatutId() == 3
                                        || CHB.getStatutId() == 4
                                        || CHB.getStatutId() == 7) {
                                    showMenuEtat();
                                } else {
                                    Toast t = Toast.makeText(
                                            getApplicationContext(),
                                            "Changement d'état non autorisé.",
                                            Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                }
                            } else if (radioReclamer.isChecked()) {
                                Intent i = new Intent(getApplicationContext(),
                                        DeclarationPanne.class);
                                i.putExtra("prod_id", CHB.getProdId());
                                i.putExtra("num_chb", CHB.getNumChb());
                                i.putExtra("login", login);
                                startActivity(i);
                                finish();
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
        builderHS.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (hsComment.getText().toString().trim().length() == 0) {
                            showToast("Veuillez écrire un commentaire.");
                            setCommentHS();
                        } else {
                            commentHS = hsComment.getText().toString();
                            AsyncChangeEtatWS ws = new AsyncChangeEtatWS();
                            ws.execute();
                        }

                    }
                }).setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        commentHS = "";
                    }
                });
        builderHS.show();
    }

    private void showToast(String s) {
        Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public class AsyncCallWS extends AsyncTask<String, String, ArrayList<Room>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            listRoom = new ArrayList<Room>();
            openProg();
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<Room> listRoom) {
            grid.setAdapter(new CustomGrid(RoomRack.this, listRoom));
            progressDialog.dismiss();
            androidHttpTransport.reset();
            super.onPostExecute(listRoom);
        }

        protected ArrayList<Room> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo pi_strlistEtage = new PropertyInfo();
            pi_strlistEtage.setName("strlistEtage");
            pi_strlistEtage.setValue(strlistEtage);
            pi_strlistEtage.setType(String.class);
            request.addProperty(pi_strlistEtage);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
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

                            Room room;
                            SoapObject Rooms = new SoapObject();
                            SoapObject Rooom = new SoapObject();

                            Rooms = (SoapObject) response.getProperty(0);
                            if (Rooms.toString().equals("anyType{}")) {
                                Toast.makeText(getApplicationContext(),
                                        "Impossible de charger les chambres  ! ",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < Rooms.getPropertyCount(); i++) {

                                Rooom = (SoapObject) Rooms.getProperty(i);

                                room = new Room();
                                room.setProdId(Integer.parseInt(Rooom
                                        .getProperty("prodId").toString()));
                                room.setIdTypeHeb(Integer.parseInt(Rooom
                                        .getProperty("idTypeHeb").toString()));
                                room.setIdTypeProd(Integer.parseInt(Rooom
                                        .getProperty("IdTypeProd").toString()));
                                room.setLblTypeProd(Rooom.getProperty(
                                        "lblTypeProd").toString());
                                room.setStatutId(Integer.parseInt(Rooom
                                        .getProperty("StatutId").toString()));
                                room.setLblStatut(Rooom
                                        .getProperty("lblStatut").toString());
                                room.setNumChb(Integer.parseInt(Rooom
                                        .getProperty("NumChb").toString()));
                                room.setAttributed(Boolean.parseBoolean(Rooom
                                        .getProperty("lblAttributed")
                                        .toString()));

                                listRoom.add(room);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listRoom;
        }
    }

    public class AsyncChangeEtatWS extends AsyncTask<String, String, String> {
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
            super.onPostExecute(result);
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo user = new PropertyInfo();
            user.setName("user");
            user.setValue(login);
            user.setType(String.class);
            request.addProperty(user);

            PropertyInfo prodId = new PropertyInfo();
            prodId.setName("prodId");
            prodId.setValue(CHB.getProdId());
            prodId.setType(Integer.class);
            request.addProperty(prodId);

            PropertyInfo pi_typeHebId = new PropertyInfo();
            pi_typeHebId.setName("typeHebId");
            pi_typeHebId.setValue(CHB.getIdTypeHeb());
            pi_typeHebId.setType(Integer.class);
            request.addProperty(pi_typeHebId);

            PropertyInfo pi_typeProdId = new PropertyInfo();
            pi_typeProdId.setName("typeProdId");
            pi_typeProdId.setValue(CHB.getIdTypeProd());
            pi_typeProdId.setType(Integer.class);
            request.addProperty(pi_typeProdId);

            PropertyInfo pi_statut = new PropertyInfo();
            pi_statut.setName("statut");
            pi_statut.setValue(idStatutNew);
            pi_statut.setType(Integer.class);
            request.addProperty(pi_statut);

            PropertyInfo pi_oldStatut = new PropertyInfo();
            pi_oldStatut.setName("oldStatut");
            pi_oldStatut.setValue(CHB.getStatutId());
            pi_oldStatut.setType(Integer.class);
            request.addProperty(pi_oldStatut);

            PropertyInfo pi_comment = new PropertyInfo();
            pi_comment.setName("comment");
            pi_comment.setValue(commentHS);
            pi_comment.setType(String.class);
            request.addProperty(pi_comment);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION2, envelope);
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
                        ShowEtatMessage(Boolean.parseBoolean(result));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class AsyncFilterWS extends
            AsyncTask<String, String, ArrayList<Etage>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            openProg();
            listEtage = new ArrayList<Etage>();
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<Etage> listEtage) {
            progressDialog.dismiss();
            gridFilter.setAdapter(new CustomFilter(RoomRack.this, listEtage));

            super.onPostExecute(listEtage);
        }

        protected ArrayList<Etage> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION3, envelope);
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
                            Log.i("Reponse:", response.toString());
                            Etage etage;
                            SoapObject Etages = new SoapObject();
                            SoapObject oneetage = new SoapObject();

                            Etages = (SoapObject) response.getProperty(0);
                            if (Etages.toString().equals("anyType{}")) {
                                Toast.makeText(getApplicationContext(),
                                        "Impossible de charger les étages ! ",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < Etages.getPropertyCount(); i++) {

                                oneetage = (SoapObject) Etages.getProperty(i);

                                etage = new Etage();
                                etage.setBLOC(Integer.parseInt(oneetage
                                        .getProperty("BLOC").toString()));
                                etage.setIDETAGE(Integer.parseInt(oneetage
                                        .getProperty("IDETAGE").toString()));
                                etage.setLBLETAGE(oneetage.getProperty(
                                        "LBLETAGE").toString());
                                etage.setCHECKED(false);
                                listEtage.add(etage);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listEtage;
        }
    }

}
