package com.hotix.myhotixhousekeeping.entities;

/**
 * Created by ziedrebhi on 21/03/2017.
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
        "Type",
        "Duree",
        "Urgent",
        "Nom",
        "Prenom",
        "Image",
        "Description",
        "Date"
})
public class PanneData implements Serializable {

    private final static long serialVersionUID = 5314773151029893227L;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("Lieu")
    private String lieu;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Duree")
    private String duree;
    @JsonProperty("Urgent")
    private boolean urgent;
    @JsonProperty("Nom")
    private String nom;
    @JsonProperty("Prenom")
    private String prenom;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("Description")
    private String description;
    @JsonProperty("Date")
    private String date;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public PanneData() {
    }

    /**
     * @param prenom
     * @param id
     * @param lieu
     * @param description
     * @param urgent
     * @param image
     * @param duree
     * @param date
     * @param type
     * @param nom
     */
    public PanneData(int id, String lieu, String type, String duree, boolean urgent, String nom, String prenom, String image, String description, String date) {
        super();
        this.id = id;
        this.lieu = lieu;
        this.type = type;
        this.duree = duree;
        this.urgent = urgent;
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
        this.description = description;
        this.date = date;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public PanneData withId(int id) {
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

    public PanneData withLieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
    }

    public PanneData withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("Duree")
    public String getDuree() {
        return duree;
    }

    @JsonProperty("Duree")
    public void setDuree(String duree) {
        this.duree = duree;
    }

    public PanneData withDuree(String duree) {
        this.duree = duree;
        return this;
    }

    @JsonProperty("Urgent")
    public boolean isUrgent() {
        return urgent;
    }

    @JsonProperty("Urgent")
    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public PanneData withUrgent(boolean urgent) {
        this.urgent = urgent;
        return this;
    }

    @JsonProperty("Nom")
    public String getNom() {
        return nom;
    }

    @JsonProperty("Nom")
    public void setNom(String nom) {
        this.nom = nom;
    }

    public PanneData withNom(String nom) {
        this.nom = nom;
        return this;
    }

    @JsonProperty("Prenom")
    public String getPrenom() {
        return prenom;
    }

    @JsonProperty("Prenom")
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public PanneData withPrenom(String prenom) {
        this.prenom = prenom;
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

    public PanneData withImage(String image) {
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

    public PanneData withDescription(String description) {
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

    public PanneData withDate(String date) {
        this.date = date;
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

    public PanneData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}