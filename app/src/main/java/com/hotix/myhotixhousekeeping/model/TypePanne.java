package com.hotix.myhotixhousekeeping.model;

public class TypePanne {
    int idPanne;
    String nomPanne;

    public int getIdPanne() {
        return idPanne;
    }

    public void setIdPanne(int idPanne) {
        this.idPanne = idPanne;
    }

    public String getNomPanne() {
        return nomPanne;
    }

    public void setNomPanne(String nomPanne) {
        this.nomPanne = nomPanne;
    }

    @Override
    public String toString() {
        return nomPanne;
    }
}
