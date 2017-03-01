package com.hotix.myhotixhousekeeping.model;

public class Panne {
    int hotelId;
    int idPanne;
    String prodId;
    TypePanne TypePanne;
    boolean urgent;
    int duree;
    String lieuPanne;
    String nom;
    String prenom;
    String Description;
    String dateReparation;
    String date;
    String heureReparation;
    String reparerPar;
    int etatPanne;
    String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getIdPanne() {
        return idPanne;
    }

    public void setIdPanne(int idPanne) {
        this.idPanne = idPanne;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public com.hotix.myhotixhousekeeping.model.TypePanne getTypePanne() {
        return TypePanne;
    }

    public void setTypePanne(com.hotix.myhotixhousekeeping.model.TypePanne typePanne) {
        TypePanne = typePanne;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getLieuPanne() {
        return lieuPanne;
    }

    public void setLieuPanne(String lieuPanne) {
        this.lieuPanne = lieuPanne;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateReparation() {
        return dateReparation;
    }

    public void setDateReparation(String dateReparation) {
        this.dateReparation = dateReparation;
    }

    public String getHeureReparation() {
        return heureReparation;
    }

    public void setHeureReparation(String heureReparation) {
        this.heureReparation = heureReparation;
    }

    public String getReparerPar() {
        return reparerPar;
    }

    public void setReparerPar(String reparerPar) {
        this.reparerPar = reparerPar;
    }

    public int getEtatPanne() {
        return etatPanne;
    }

    public void setEtatPanne(int etatPanne) {
        this.etatPanne = etatPanne;
    }

    @Override
    public String toString() {
        return "Panne{" +
                "hotelId=" + hotelId +
                ", idPanne=" + idPanne +
                ", prodId='" + prodId + '\'' +
                ", TypePanne=" + TypePanne +
                ", urgent=" + urgent +
                ", duree=" + duree +
                ", lieuPanne='" + lieuPanne + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", Description='" + Description + '\'' +
                ", dateReparation='" + dateReparation + '\'' +
                ", date='" + date + '\'' +
                ", heureReparation='" + heureReparation + '\'' +
                ", reparerPar='" + reparerPar + '\'' +
                ", etatPanne=" + etatPanne +
                ", Image='" + Image + '\'' +
                '}';
    }
}
