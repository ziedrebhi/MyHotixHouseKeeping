package com.hotix.myhotixhousekeeping.model;

public class ClientPrevue {
    String ResaId;
    int ROOM;
    String NOM;
    String PRENOM;
    String ARRIVEE;
    String DEPART;

    public String getResaId() {
        return ResaId;
    }

    public void setResaId(String resaId) {
        ResaId = resaId;
    }

    public int getROOM() {
        return ROOM;
    }

    public void setROOM(int rOOM) {
        ROOM = rOOM;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String nOM) {
        NOM = nOM;
    }

    public String getPRENOM() {
        return PRENOM;
    }

    public void setPRENOM(String pRENOM) {
        PRENOM = pRENOM;
    }

    public String getARRIVEE() {
        return ARRIVEE;
    }

    public void setARRIVEE(String aRRIVEE) {
        ARRIVEE = aRRIVEE;
    }

    public String getDEPART() {
        return DEPART;
    }

    public void setDEPART(String dEPART) {
        DEPART = dEPART;
    }

}
