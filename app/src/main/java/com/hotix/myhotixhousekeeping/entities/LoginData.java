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
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Id",
        "ProfileId",
        "FullName",
        "DateFront",
        "Etages",
        "Techniciens",
        "TypesPanne",
        "FemmesMenage",
        "Error"
})
public class LoginData implements Serializable {

    private final static long serialVersionUID = 35355859151924552L;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("ProfileId")
    private int profileId;
    @JsonProperty("FullName")
    private String fullName;
    @JsonProperty("DateFront")
    private String dateFront;
    @JsonProperty("Etages")
    private List<Etage> etages = null;
    @JsonProperty("Techniciens")
    private List<Technicien> techniciens = null;
    @JsonProperty("TypesPanne")
    private List<TypesPanne> typesPanne = null;
    @JsonProperty("FemmesMenage")
    private List<FemmesMenage> femmesMenage = null;
    @JsonProperty("Error")
    private int error;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public LoginData() {
    }

    /**
     * @param etages
     * @param id
     * @param techniciens
     * @param error
     * @param profileId
     * @param dateFront
     * @param fullName
     * @param femmesMenage
     * @param typesPanne
     */
    public LoginData(int id, int profileId, String fullName, String dateFront, List<Etage> etages, List<Technicien> techniciens, List<TypesPanne> typesPanne, List<FemmesMenage> femmesMenage, int error) {
        super();
        this.id = id;
        this.profileId = profileId;
        this.fullName = fullName;
        this.dateFront = dateFront;
        this.etages = etages;
        this.techniciens = techniciens;
        this.typesPanne = typesPanne;
        this.femmesMenage = femmesMenage;
        this.error = error;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public LoginData withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("ProfileId")
    public int getProfileId() {
        return profileId;
    }

    @JsonProperty("ProfileId")
    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public LoginData withProfileId(int profileId) {
        this.profileId = profileId;
        return this;
    }

    @JsonProperty("FullName")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("FullName")
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LoginData withFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    @JsonProperty("DateFront")
    public String getDateFront() {
        return dateFront;
    }

    @JsonProperty("DateFront")
    public void setDateFront(String dateFront) {
        this.dateFront = dateFront;
    }

    public LoginData withDateFront(String dateFront) {
        this.dateFront = dateFront;
        return this;
    }

    @JsonProperty("Etages")
    public List<Etage> getEtages() {
        return etages;
    }

    @JsonProperty("Etages")
    public void setEtages(List<Etage> etages) {
        this.etages = etages;
    }

    public LoginData withEtages(List<Etage> etages) {
        this.etages = etages;
        return this;
    }

    @JsonProperty("Techniciens")
    public List<Technicien> getTechniciens() {
        return techniciens;
    }

    @JsonProperty("Techniciens")
    public void setTechniciens(List<Technicien> techniciens) {
        this.techniciens = techniciens;
    }

    public LoginData withTechniciens(List<Technicien> techniciens) {
        this.techniciens = techniciens;
        return this;
    }

    @JsonProperty("TypesPanne")
    public List<TypesPanne> getTypesPanne() {
        return typesPanne;
    }

    @JsonProperty("TypesPanne")
    public void setTypesPanne(List<TypesPanne> typesPanne) {
        this.typesPanne = typesPanne;
    }

    public LoginData withTypesPanne(List<TypesPanne> typesPanne) {
        this.typesPanne = typesPanne;
        return this;
    }

    @JsonProperty("FemmesMenage")
    public List<FemmesMenage> getFemmesMenage() {
        return femmesMenage;
    }

    @JsonProperty("FemmesMenage")
    public void setFemmesMenage(List<FemmesMenage> femmesMenage) {
        this.femmesMenage = femmesMenage;
    }

    public LoginData withFemmesMenage(List<FemmesMenage> femmesMenage) {
        this.femmesMenage = femmesMenage;
        return this;
    }

    @JsonProperty("Error")
    public int getError() {
        return error;
    }

    @JsonProperty("Error")
    public void setError(int error) {
        this.error = error;
    }

    public LoginData withError(int error) {
        this.error = error;
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

    public LoginData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}