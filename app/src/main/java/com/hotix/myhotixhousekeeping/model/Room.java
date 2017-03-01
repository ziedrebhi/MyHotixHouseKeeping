package com.hotix.myhotixhousekeeping.model;

import android.graphics.drawable.Drawable;

public class Room {

    int idTypeHeb;
    int IdTypeProd;
    int prodId;
    int NumChb;
    int statutId;
    String lblStatut;
    String lblTypeProd;
    Boolean attributed;
    Drawable icon;

    public int getIdTypeHeb() {
        return idTypeHeb;
    }

    public void setIdTypeHeb(int idTypeHeb) {
        this.idTypeHeb = idTypeHeb;
    }

    public int getIdTypeProd() {
        return IdTypeProd;
    }

    public void setIdTypeProd(int idTypeProd) {
        IdTypeProd = idTypeProd;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getNumChb() {
        return NumChb;
    }

    public void setNumChb(int numChb) {
        NumChb = numChb;
    }

    public int getStatutId() {
        return statutId;
    }

    public void setStatutId(int statutId) {
        this.statutId = statutId;
    }

    public String getLblStatut() {
        return lblStatut;
    }

    public void setLblStatut(String lblStatut) {
        this.lblStatut = lblStatut;
    }

    public String getLblTypeProd() {
        return lblTypeProd;
    }

    public void setLblTypeProd(String lblTypeProd) {
        this.lblTypeProd = lblTypeProd;
    }

    public Boolean getAttributed() {
        return attributed;
    }

    public void setAttributed(Boolean attributed) {
        this.attributed = attributed;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
