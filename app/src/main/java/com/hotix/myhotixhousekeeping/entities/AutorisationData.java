package com.hotix.myhotixhousekeeping.entities;

/**
 * Created by ziedrebhi on 29/03/2017.
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
        "Login",
        "HasConfig",
        "HasAddPanne",
        "HasAddObjet",
        "HasClosePanne",
        "HasCloseObjet",
        "HasMouchard",
        "HasChangeStatut",
        "HasEtatLieu",
        "HasViewClient",
        "HasFM"
})
public class AutorisationData implements Serializable {

    private final static long serialVersionUID = 7338738670701550650L;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("Login")
    private String login;
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public AutorisationData() {
    }

    /**
     * @param id
     * @param hasFM
     * @param hasChangeStatut
     * @param hasViewClient
     * @param hasCloseObjet
     * @param hasEtatLieu
     * @param login
     * @param hasClosePanne
     * @param hasConfig
     * @param hasMouchard
     * @param hasAddObjet
     * @param hasAddPanne
     */
    public AutorisationData(int id, String login, boolean hasConfig, boolean hasAddPanne, boolean hasAddObjet, boolean hasClosePanne, boolean hasCloseObjet, boolean hasMouchard, boolean hasChangeStatut, boolean hasEtatLieu, boolean hasViewClient, boolean hasFM) {
        super();
        this.id = id;
        this.login = login;
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
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public AutorisationData withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("Login")
    public String getLogin() {
        return login;
    }

    @JsonProperty("Login")
    public void setLogin(String login) {
        this.login = login;
    }

    public AutorisationData withLogin(String login) {
        this.login = login;
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

    public AutorisationData withHasConfig(boolean hasConfig) {
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

    public AutorisationData withHasAddPanne(boolean hasAddPanne) {
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

    public AutorisationData withHasAddObjet(boolean hasAddObjet) {
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

    public AutorisationData withHasClosePanne(boolean hasClosePanne) {
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

    public AutorisationData withHasCloseObjet(boolean hasCloseObjet) {
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

    public AutorisationData withHasMouchard(boolean hasMouchard) {
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

    public AutorisationData withHasChangeStatut(boolean hasChangeStatut) {
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

    public AutorisationData withHasEtatLieu(boolean hasEtatLieu) {
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

    public AutorisationData withHasViewClient(boolean hasViewClient) {
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

    public AutorisationData withHasFM(boolean hasFM) {
        this.hasFM = hasFM;
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

    public AutorisationData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}