package com.hotix.myhotixhousekeeping.entities;

/**
 * Created by ziedrebhi on 22/03/2017.
 */

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Id",
        "Lieu",
        "NomTrouve",
        "PrenomTrouve",
        "NomRendu",
        "Prenomrendu",
        "Image",
        "Description",
        "Date",
        "Commentaire"
})
public class ObjetTrouveData implements Serializable {

    private final static long serialVersionUID = 890278546846107500L;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("Lieu")
    private String lieu;
    @JsonProperty("NomTrouve")
    private String nomTrouve;
    @JsonProperty("PrenomTrouve")
    private String prenomTrouve;
    @JsonProperty("NomRendu")
    private String nomRendu;
    @JsonProperty("Prenomrendu")
    private String prenomrendu;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Commentaire")
    private String commentaire;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public ObjetTrouveData() {
    }

    /**
     * @param id
     * @param lieu
     * @param nomRendu
     * @param description
     * @param image
     * @param prenomrendu
     * @param nomTrouve
     * @param date
     * @param prenomTrouve
     * @param commentaire
     */
    public ObjetTrouveData(int id, String lieu, String nomTrouve, String prenomTrouve, String nomRendu, String prenomrendu, String image, String description, String date, String commentaire) {
        super();
        this.id = id;
        this.lieu = lieu;
        this.nomTrouve = nomTrouve;
        this.prenomTrouve = prenomTrouve;
        this.nomRendu = nomRendu;
        this.prenomrendu = prenomrendu;
        this.image = image;
        this.description = description;
        this.date = date;
        this.commentaire = commentaire;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public ObjetTrouveData withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("Lieu")
    public String getLieu() {
        return lieu;
    }

    @JsonProperty("Lieu")
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public ObjetTrouveData withLieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    @JsonProperty("NomTrouve")
    public String getNomTrouve() {
        return nomTrouve;
    }

    @JsonProperty("NomTrouve")
    public void setNomTrouve(String nomTrouve) {
        this.nomTrouve = nomTrouve;
    }

    public ObjetTrouveData withNomTrouve(String nomTrouve) {
        this.nomTrouve = nomTrouve;
        return this;
    }

    @JsonProperty("PrenomTrouve")
    public String getPrenomTrouve() {
        return prenomTrouve;
    }

    @JsonProperty("PrenomTrouve")
    public void setPrenomTrouve(String prenomTrouve) {
        this.prenomTrouve = prenomTrouve;
    }

    public ObjetTrouveData withPrenomTrouve(String prenomTrouve) {
        this.prenomTrouve = prenomTrouve;
        return this;
    }

    @JsonProperty("NomRendu")
    public String getNomRendu() {
        return nomRendu;
    }

    @JsonProperty("NomRendu")
    public void setNomRendu(String nomRendu) {
        this.nomRendu = nomRendu;
    }

    public ObjetTrouveData withNomRendu(String nomRendu) {
        this.nomRendu = nomRendu;
        return this;
    }

    @JsonProperty("Prenomrendu")
    public String getPrenomrendu() {
        return prenomrendu;
    }

    @JsonProperty("Prenomrendu")
    public void setPrenomrendu(String prenomrendu) {
        this.prenomrendu = prenomrendu;
    }

    public ObjetTrouveData withPrenomrendu(String prenomrendu) {
        this.prenomrendu = prenomrendu;
        return this;
    }

    @JsonProperty("Image")
    public String getImage() {
        return image;
    }

    @JsonProperty("Image")
    public void setImage(String image) {
        this.image = image;
    }

    public ObjetTrouveData withImage(String image) {
        this.image = image;
        return this;
    }

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public ObjetTrouveData withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("Date")
    public String getDate() {
        return date;
    }

    @JsonProperty("Date")
    public void setDate(String date) {
        this.date = date;
    }

    public ObjetTrouveData withDate(String date) {
        this.date = date;
        return this;
    }

    @JsonProperty("Commentaire")
    public String getCommentaire() {
        return commentaire;
    }

    @JsonProperty("Commentaire")
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public ObjetTrouveData withCommentaire(String commentaire) {
        this.commentaire = commentaire;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ObjetTrouveData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
