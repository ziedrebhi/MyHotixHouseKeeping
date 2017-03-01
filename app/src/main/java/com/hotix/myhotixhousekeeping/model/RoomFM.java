package com.hotix.myhotixhousekeeping.model;

public class RoomFM {
    int etat;
    int empId;
    int prodId;
    int statutId;
    int typeHebId;
    int typeProdId;
    int prodNum;
    Boolean attributed;

    public Boolean getAttributed() {
        return attributed;
    }

    public void setAttributed(Boolean attributed) {
        this.attributed = attributed;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getStatutId() {
        return statutId;
    }

    public void setStatutId(int statutId) {
        this.statutId = statutId;
    }

    public int getTypeHebId() {
        return typeHebId;
    }

    public void setTypeHebId(int typeHebId) {
        this.typeHebId = typeHebId;
    }

    public int getTypeProdId() {
        return typeProdId;
    }

    public void setTypeProdId(int typeProdId) {
        this.typeProdId = typeProdId;
    }

    public int getProdNum() {
        return prodNum;
    }

    public void setProdNum(int prodNum) {
        this.prodNum = prodNum;
    }

}
