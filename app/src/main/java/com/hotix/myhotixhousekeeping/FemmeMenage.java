package com.hotix.myhotixhousekeeping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.fragments.flipviewer.AnimationFactory;
import com.fragments.flipviewer.AnimationFactory.FlipDirection;
import com.hotix.myhotixhousekeeping.adapter.CustomAffect;
import com.hotix.myhotixhousekeeping.adapter.FemmeMenageAdapter;
import com.hotix.myhotixhousekeeping.model.FMenage;
import com.hotix.myhotixhousekeeping.model.RoomFM;
import com.hotix.myhotixhousekeeping.utils.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class FemmeMenage extends FragmentActivity {

    static int fmId;
    public final String NAMESPACE = "http://tempuri.org/";
    public final String SOAP_ACTION = "http://tempuri.org/GetListeFemmesMenage";
    public final String METHOD_NAME = "GetListeFemmesMenage";
    public final String SOAP_ACTION2 = "http://tempuri.org/GetListeAffectionFM";
    public final String METHOD_NAME2 = "GetListeAffectionFM";
    public final String SOAP_ACTION3 = "http://tempuri.org/AffectationFemmeMenage";
    public final String METHOD_NAME3 = "AffectationFemmeMenage";
    CustomProgressDialog progressDialog;
    ArrayList<FMenage> listFM;
    ArrayList<RoomFM> listRoom, listRoomNA, listRoomA;
    ListView lvFM;
    ViewAnimator viewAnimator;
    GridView gridAff, gridNonAff;
    Button btnFM, btn_up, btn_down;
    MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_femme_menage);
        lvFM = (ListView) findViewById(R.id.listFM);
        viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipperFM);
        gridAff = (GridView) findViewById(R.id.gridAffect);
        gridNonAff = (GridView) findViewById(R.id.gridNonAffect);
        btnFM = (Button) findViewById(R.id.btn_affecterFM);
        btn_up = (Button) findViewById(R.id.btn_up);
        btn_down = (Button) findViewById(R.id.btn_down);

    }

    @Override
    protected void onResume() {
        progressDialog = new CustomProgressDialog(this, R.drawable.loading);

        ListeFMWS ws = new ListeFMWS();
        ws.execute();

        listRoomA = new ArrayList<RoomFM>();
        listRoomNA = new ArrayList<RoomFM>();

        lvFM.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                fmId = listFM.get(position).getIdFM();
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.RIGHT_LEFT);
                item.setVisible(true);
                ListeAffWS ws = new ListeAffWS();
                ws.execute();
            }
        });

        gridNonAff.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RoomFM rfm = new RoomFM();
                rfm = listRoomNA.get(position);
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                listRoomNA.remove(rfm);
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                listRoomA.add(rfm);
                CNA.notifyDataSetChanged();
                gridNonAff.setAdapter(CNA);

            }
        });

        gridAff.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                RoomFM rfm = new RoomFM();
                rfm = listRoomA.get(position);
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                listRoomA.remove(rfm);
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                listRoomNA.add(rfm);
                CNA.notifyDataSetChanged();
                gridNonAff.setAdapter(CNA);

            }
        });

        btnFM.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listRoomA.size() > 0) {
                    AffectationWS af = new AffectationWS();
                    af.execute();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "La liste des chambres à affecter est vide.",
                            Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }

            }
        });

        btn_up.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RoomFM rfm = new RoomFM();
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                for (int i = 0; i < listRoomA.size(); i++) {
                    rfm = listRoomA.get(i);
                    listRoomNA.add(rfm);
                }
                CNA.notifyDataSetChanged();
                listRoomA.clear();
                CA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                gridNonAff.setAdapter(CNA);
            }
        });

        btn_down.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                RoomFM rfm = new RoomFM();
                CustomAffect CA = new CustomAffect(getApplicationContext(),
                        listRoomA);
                CustomAffect CNA = new CustomAffect(getApplicationContext(),
                        listRoomNA);
                for (int i = 0; i < listRoomNA.size(); i++) {
                    rfm = listRoomNA.get(i);
                    listRoomA.add(rfm);
                }
                CA.notifyDataSetChanged();
                listRoomNA.clear();
                CNA.notifyDataSetChanged();
                gridAff.setAdapter(CA);
                gridNonAff.setAdapter(CNA);
            }
        });

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fm, menu);
        item = menu.findItem(R.id.retourtop);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retourtop:
                AnimationFactory.flipTransition(viewAnimator,
                        FlipDirection.LEFT_RIGHT);
                item.setVisible(false);
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

    private void AffectatoinResa(Boolean b) {
        if (b) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.custom_toast_layout_id));

            TextView text = (TextView) layout.findViewById(R.id.textResult);
            text.setText("Succés d'affectation de femme de ménage.");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            Intent i = new Intent(getApplicationContext(), FemmeMenage.class);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_error,
                    (ViewGroup) findViewById(R.id.custom_toast_echec));

            TextView text = (TextView) layout.findViewById(R.id.textError);
            text.setText("Echec d'affectation de femme de ménage.");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            AnimationFactory.flipTransition(viewAnimator,
                    FlipDirection.LEFT_RIGHT);
        }
    }

    // Afficher le Gridview
    private void displayRoomGrid() {
        listRoomNA = new ArrayList<RoomFM>();
        listRoomA = new ArrayList<RoomFM>();
        for (int i = 0; i < listRoom.size(); i++) {

            if (listRoom.get(i).getEtat() == 1) {
                listRoomA.add(listRoom.get(i));
            } else {
                listRoomNA.add(listRoom.get(i));
            }
        }

        gridAff.setAdapter(new CustomAffect(FemmeMenage.this, listRoomA));
        gridNonAff.setAdapter(new CustomAffect(FemmeMenage.this, listRoomNA));
    }

    private void openProg() {
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    private String GetChaineAffection() {
        String chaine = "";
        for (int i = 0; i < listRoomA.size(); i++) {
            chaine = chaine + String.valueOf(listRoomA.get(i).getTypeHebId())
                    + "," + String.valueOf(listRoomA.get(i).getTypeProdId())
                    + "," + String.valueOf(listRoomA.get(i).getProdId()) + ";";
        }
        return chaine.substring(0, chaine.length() - 1);
    }

    public String getURL() {
        String URL = null;
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(this);
        URL = sp.getString("serveur", "");
        URL = "http://" + URL + "/hngwebsetup/WebService/HNGHousekeeping.asmx";
        return URL;
    }

    public class ListeFMWS extends
            AsyncTask<String, String, ArrayList<FMenage>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            openProg();
            listFM = new ArrayList<FMenage>();
            super.onPreExecute();
        }

        protected void onPostExecute(ArrayList<FMenage> listFM) {
            lvFM.setAdapter(new FemmeMenageAdapter(getApplicationContext(),
                    listFM));
            androidHttpTransport.reset();
            progressDialog.dismiss();
            super.onPostExecute(listFM);
        }

        protected ArrayList<FMenage> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
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
                            FMenage femmeMenage;
                            SoapObject Femmes = new SoapObject();
                            SoapObject Femme = new SoapObject();

                            Femmes = (SoapObject) response.getProperty(0);
                            if (Femmes.toString().equals("anyType{}")) {
                                Toast.makeText(getApplicationContext(),
                                        "Impossible de charger les femmes de ménage  ! ",
                                        Toast.LENGTH_LONG).show();
                            }
                            for (int i = 0; i < Femmes.getPropertyCount(); i++) {

                                Femme = (SoapObject) Femmes.getProperty(i);

                                femmeMenage = new FMenage();
                                femmeMenage.setIdFM(Integer
                                        .parseInt(Femme.getProperty(
                                                "lblIdEmploye").toString()));
                                femmeMenage.setMatricule(Femme.getProperty(
                                        "lblMatricule").toString());
                                femmeMenage.setNom(Femme.getProperty("lblNom")
                                        .toString());
                                femmeMenage.setPrenom(Femme.getProperty(
                                        "lblPrenom").toString());

                                listFM.add(femmeMenage);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return listFM;
        }
    }

    // Chaine affectation femme de ménage

    public class ListeAffWS extends
            AsyncTask<String, String, ArrayList<RoomFM>> {
        SoapObject response = null;
        HttpTransportSE androidHttpTransport;

        @Override
        protected void onPreExecute() {
            listRoom = new ArrayList<RoomFM>();
            openProg();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<RoomFM> listRoom) {
            displayRoomGrid();
            progressDialog.dismiss();
            androidHttpTransport.reset();
            super.onPostExecute(listRoom);
        }

        protected ArrayList<RoomFM> doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);

            PropertyInfo pi_fmId = new PropertyInfo();
            pi_fmId.setName("empID");
            pi_fmId.setValue(fmId);
            Log.i("ID IMP", fmId + "");
            pi_fmId.setType(Integer.class);
            request.addProperty(pi_fmId);

            envelope.dotNet = true;
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
                            Log.i("response", response.toString());
                            RoomFM room;
                            SoapObject Rooms = new SoapObject();
                            SoapObject Rooom = new SoapObject();

                            Rooms = (SoapObject) response.getProperty(0);
                            for (int i = 0; i < Rooms.getPropertyCount(); i++) {
                                Rooom = (SoapObject) Rooms.getProperty(i);
                                room = new RoomFM();

                                room.setEtat(Integer.parseInt(Rooom
                                        .getProperty("ETAT").toString()));
                                room.setEmpId(Integer.parseInt(Rooom
                                        .getProperty("EMPID").toString()));
                                room.setProdId(Integer.parseInt(Rooom
                                        .getProperty("PRODID").toString()));
                                room.setStatutId(Integer.parseInt(Rooom
                                        .getProperty("STATUTID").toString()));
                                room.setTypeHebId(Integer.parseInt(Rooom
                                        .getProperty("TYPEHEBID").toString()));
                                room.setTypeProdId(Integer.parseInt(Rooom
                                        .getProperty("TYPEPRODID").toString()));
                                room.setProdNum(Integer.parseInt(Rooom
                                        .getProperty("PRODNUM").toString()));
                                room.setAttributed(Boolean.parseBoolean(Rooom
                                        .getProperty("ATTRIBUTED").toString()));

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

    public class AffectationWS extends AsyncTask<String, String, String> {
        HttpTransportSE androidHttpTransport;
        String result = "False";

        @Override
        protected void onPreExecute() {
            openProg();
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME3);

            PropertyInfo pi_intEmployeId = new PropertyInfo();
            pi_intEmployeId.setName("intEmployeId");
            pi_intEmployeId.setValue(fmId);
            pi_intEmployeId.setType(Integer.class);
            request.addProperty(pi_intEmployeId);

            PropertyInfo pi_chaine_affectation = new PropertyInfo();
            pi_chaine_affectation.setName("chaine_affectation");
            pi_chaine_affectation.setValue(GetChaineAffection());
            pi_chaine_affectation.setType(String.class);
            request.addProperty(pi_chaine_affectation);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            androidHttpTransport = new HttpTransportSE(getURL(), 5000);
            try {
                try {
                    androidHttpTransport.call(SOAP_ACTION, envelope);
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
                        AffectatoinResa(Boolean.parseBoolean(result));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Long result) {
            progressDialog.dismiss();
            androidHttpTransport.reset();
        }

    }
}
