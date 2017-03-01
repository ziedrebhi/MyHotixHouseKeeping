package com.hotix.myhotixhousekeeping.model;

public class ObjetCloture {
    String lieu;
    int idOT;
    String description;
    String dateOT;
    String Nom;
    String Prenom;
    String RNom;
    String RPrenom;
    String commentaire;
    String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getIdOT() {
        return idOT;
    }

    public void setIdOT(int idOT) {
        this.idOT = idOT;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateOT() {
        return dateOT;
    }

    public void setDateOT(String dateOT) {
        this.dateOT = dateOT;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public String getRNom() {
        return RNom;
    }

    public void setRNom(String rNom) {
        RNom = rNom;
    }

    public String getRPrenom() {
        return RPrenom;
    }

    public void setRPrenom(String rPrenom) {
        RPrenom = rPrenom;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "ObjetCloture{" +
                "lieu='" + lieu + '\'' +
                ", idOT=" + idOT +
                ", description='" + description + '\'' +
                ", dateOT='" + dateOT + '\'' +
                ", Nom='" + Nom + '\'' +
                ", Prenom='" + Prenom + '\'' +
                ", RNom='" + RNom + '\'' +
                ", RPrenom='" + RPrenom + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }
}
