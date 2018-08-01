package com.hotix.myhotixhousekeeping.entities;
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
        "Error",
        "HasConfig",
        "HasAddPanne",
        "HasAddObjet",
        "HasClosePanne",
        "HasCloseObjet",
        "HasMouchard",
        "HasChangeStatut",
        "HasEtatLieu",
        "HasViewClient",
        "HasFM",
        "Hotel"
})
public class LoginData implements Serializable {

    private final static long serialVersionUID = 7088294697518229912L;
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
    @JsonProperty("HasConfig")
    private boolean hasConfig;
    @JsonProperty("HasAddPanne")
    private boolean hasAddPanne;
    @JsonProperty("HasAddObjet")
    private boolean hasAddObjet;
    @JsonProperty("HasClosePanne")
    private boolean hasClosePanne;
    @JsonProperty("HasCloseObjet")
    private boolean hasCloseObjet;
    @JsonProperty("HasMouchard")
    private boolean hasMouchard;
    @JsonProperty("HasChangeStatut")
    private boolean hasChangeStatut;
    @JsonProperty("HasEtatLieu")
    private boolean hasEtatLieu;
    @JsonProperty("HasViewClient")
    private boolean hasViewClient;
    @JsonProperty("HasFM")
    private boolean hasFM;
    @JsonProperty("Hotel")
    private String hotel;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public LoginData() {
    }

    /**
     *
     * @param etages
     * @param hasFM
     * @param error
     * @param hasViewClient
     * @param hasEtatLieu
     * @param femmesMenage
     * @param hasClosePanne
     * @param typesPanne
     * @param hasConfig
     * @param hasMouchard
     * @param hasAddPanne
     * @param hasAddObjet
     * @param id
     * @param techniciens
     * @param hasChangeStatut
     * @param profileId
     * @param hasCloseObjet
     * @param hotel
     * @param fullName
     * @param dateFront
     */
    public LoginData(int id, int profileId, String fullName, String dateFront, List<Etage> etages, List<Technicien> techniciens, List<TypesPanne> typesPanne, List<FemmesMenage> femmesMenage, int error, boolean hasConfig, boolean hasAddPanne, boolean hasAddObjet, boolean hasClosePanne, boolean hasCloseObjet, boolean hasMouchard, boolean hasChangeStatut, boolean hasEtatLieu, boolean hasViewClient, boolean hasFM, String hotel) {
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
        this.hasConfig = hasConfig;
        this.hasAddPanne = hasAddPanne;
        this.hasAddObjet = hasAddObjet;
        this.hasClosePanne = hasClosePanne;
        this.hasCloseObjet = hasCloseObjet;
        this.hasMouchard = hasMouchard;
        this.hasChangeStatut = hasChangeStatut;
        this.hasEtatLieu = hasEtatLieu;
        this.hasViewClient = hasViewClient;
        this.hasFM = hasFM;
        this.hotel = hotel;
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

    @JsonProperty("HasConfig")
    public boolean isHasConfig() {
        return hasConfig;
    }

    @JsonProperty("HasConfig")
    public void setHasConfig(boolean hasConfig) {
        this.hasConfig = hasConfig;
    }

    public LoginData withHasConfig(boolean hasConfig) {
        this.hasConfig = hasConfig;
        return this;
    }

    @JsonProperty("HasAddPanne")
    public boolean isHasAddPanne() {
        return hasAddPanne;
    }

    @JsonProperty("HasAddPanne")
    public void setHasAddPanne(boolean hasAddPanne) {
        this.hasAddPanne = hasAddPanne;
    }

    public LoginData withHasAddPanne(boolean hasAddPanne) {
        this.hasAddPanne = hasAddPanne;
        return this;
    }

    @JsonProperty("HasAddObjet")
    public boolean isHasAddObjet() {
        return hasAddObjet;
    }

    @JsonProperty("HasAddObjet")
    public void setHasAddObjet(boolean hasAddObjet) {
        this.hasAddObjet = hasAddObjet;
    }

    public LoginData withHasAddObjet(boolean hasAddObjet) {
        this.hasAddObjet = hasAddObjet;
        return this;
    }

    @JsonProperty("HasClosePanne")
    public boolean isHasClosePanne() {
        return hasClosePanne;
    }

    @JsonProperty("HasClosePanne")
    public void setHasClosePanne(boolean hasClosePanne) {
        this.hasClosePanne = hasClosePanne;
    }

    public LoginData withHasClosePanne(boolean hasClosePanne) {
        this.hasClosePanne = hasClosePanne;
        return this;
    }

    @JsonProperty("HasCloseObjet")
    public boolean isHasCloseObjet() {
        return hasCloseObjet;
    }

    @JsonProperty("HasCloseObjet")
    public void setHasCloseObjet(boolean hasCloseObjet) {
        this.hasCloseObjet = hasCloseObjet;
    }

    public LoginData withHasCloseObjet(boolean hasCloseObjet) {
        this.hasCloseObjet = hasCloseObjet;
        return this;
    }

    @JsonProperty("HasMouchard")
    public boolean isHasMouchard() {
        return hasMouchard;
    }

    @JsonProperty("HasMouchard")
    public void setHasMouchard(boolean hasMouchard) {
        this.hasMouchard = hasMouchard;
    }

    public LoginData withHasMouchard(boolean hasMouchard) {
        this.hasMouchard = hasMouchard;
        return this;
    }

    @JsonProperty("HasChangeStatut")
    public boolean isHasChangeStatut() {
        return hasChangeStatut;
    }

    @JsonProperty("HasChangeStatut")
    public void setHasChangeStatut(boolean hasChangeStatut) {
        this.hasChangeStatut = hasChangeStatut;
    }

    public LoginData withHasChangeStatut(boolean hasChangeStatut) {
        this.hasChangeStatut = hasChangeStatut;
        return this;
    }

    @JsonProperty("HasEtatLieu")
    public boolean isHasEtatLieu() {
        return hasEtatLieu;
    }

    @JsonProperty("HasEtatLieu")
    public void setHasEtatLieu(boolean hasEtatLieu) {
        this.hasEtatLieu = hasEtatLieu;
    }

    public LoginData withHasEtatLieu(boolean hasEtatLieu) {
        this.hasEtatLieu = hasEtatLieu;
        return this;
    }

    @JsonProperty("HasViewClient")
    public boolean isHasViewClient() {
        return hasViewClient;
    }

    @JsonProperty("HasViewClient")
    public void setHasViewClient(boolean hasViewClient) {
        this.hasViewClient = hasViewClient;
    }

    public LoginData withHasViewClient(boolean hasViewClient) {
        this.hasViewClient = hasViewClient;
        return this;
    }

    @JsonProperty("HasFM")
    public boolean isHasFM() {
        return hasFM;
    }

    @JsonProperty("HasFM")
    public void setHasFM(boolean hasFM) {
        this.hasFM = hasFM;
    }

    public LoginData withHasFM(boolean hasFM) {
        this.hasFM = hasFM;
        return this;
    }

    @JsonProperty("Hotel")
    public String getHotel() {
        return hotel;
    }

    @JsonProperty("Hotel")
    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public LoginData withHotel(String hotel) {
        this.hotel = hotel;
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