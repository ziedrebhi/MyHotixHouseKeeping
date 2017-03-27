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
        "TypeHebergement",
        "Etage",
        "TypeProduitId",
        "TypeProduit",
        "ProdId",
        "NumChb",
        "StatutId",
        "Statut",
        "isAttributed",
        "Guests",
        "EtatTV",
        "EtatBar",
        "EtatServiette"
})
public class RackRoomData implements Serializable {

    private final static long serialVersionUID = -7763266649330055778L;
    @JsonProperty("TypeHebergement")
    private int typeHebergement;
    @JsonProperty("Etage")
    private int etage;
    @JsonProperty("TypeProduitId")
    private int typeProduitId;
    @JsonProperty("TypeProduit")
    private String typeProduit;
    @JsonProperty("ProdId")
    private int prodId;
    @JsonProperty("NumChb")
    private String numChb;
    @JsonProperty("StatutId")
    private int statutId;
    @JsonProperty("Statut")
    private String statut;
    @JsonProperty("isAttributed")
    private boolean isAttributed;
    @JsonProperty("Guests")
    private List<Guest> guests = null;
    @JsonProperty("EtatTV")
    private boolean etatTV;
    @JsonProperty("EtatBar")
    private boolean etatBar;
    @JsonProperty("EtatServiette")
    private boolean etatServiette;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public RackRoomData() {
    }

    /**
     * @param numChb
     * @param typeHebergement
     * @param typeProduit
     * @param statut
     * @param statutId
     * @param etatServiette
     * @param prodId
     * @param typeProduitId
     * @param guests
     * @param isAttributed
     * @param etage
     * @param etatTV
     * @param etatBar
     */
    public RackRoomData(int typeHebergement, int etage, int typeProduitId, String typeProduit, int prodId, String numChb, int statutId, String statut, boolean isAttributed, List<Guest> guests, boolean etatTV, boolean etatBar, boolean etatServiette) {
        super();
        this.typeHebergement = typeHebergement;
        this.etage = etage;
        this.typeProduitId = typeProduitId;
        this.typeProduit = typeProduit;
        this.prodId = prodId;
        this.numChb = numChb;
        this.statutId = statutId;
        this.statut = statut;
        this.isAttributed = isAttributed;
        this.guests = guests;
        this.etatTV = etatTV;
        this.etatBar = etatBar;
        this.etatServiette = etatServiette;
    }

    @JsonProperty("TypeHebergement")
    public int getTypeHebergement() {
        return typeHebergement;
    }

    @JsonProperty("TypeHebergement")
    public void setTypeHebergement(int typeHebergement) {
        this.typeHebergement = typeHebergement;
    }

    public RackRoomData withTypeHebergement(int typeHebergement) {
        this.typeHebergement = typeHebergement;
        return this;
    }

    @JsonProperty("Etage")
    public int getEtage() {
        return etage;
    }

    @JsonProperty("Etage")
    public void setEtage(int etage) {
        this.etage = etage;
    }

    public RackRoomData withEtage(int etage) {
        this.etage = etage;
        return this;
    }

    @JsonProperty("TypeProduitId")
    public int getTypeProduitId() {
        return typeProduitId;
    }

    @JsonProperty("TypeProduitId")
    public void setTypeProduitId(int typeProduitId) {
        this.typeProduitId = typeProduitId;
    }

    public RackRoomData withTypeProduitId(int typeProduitId) {
        this.typeProduitId = typeProduitId;
        return this;
    }

    @JsonProperty("TypeProduit")
    public String getTypeProduit() {
        return typeProduit;
    }

    @JsonProperty("TypeProduit")
    public void setTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public RackRoomData withTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
        return this;
    }

    @JsonProperty("ProdId")
    public int getProdId() {
        return prodId;
    }

    @JsonProperty("ProdId")
    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public RackRoomData withProdId(int prodId) {
        this.prodId = prodId;
        return this;
    }

    @JsonProperty("NumChb")
    public String getNumChb() {
        return numChb;
    }

    @JsonProperty("NumChb")
    public void setNumChb(String numChb) {
        this.numChb = numChb;
    }

    public RackRoomData withNumChb(String numChb) {
        this.numChb = numChb;
        return this;
    }

    @JsonProperty("StatutId")
    public int getStatutId() {
        return statutId;
    }

    @JsonProperty("StatutId")
    public void setStatutId(int statutId) {
        this.statutId = statutId;
    }

    public RackRoomData withStatutId(int statutId) {
        this.statutId = statutId;
        return this;
    }

    @JsonProperty("Statut")
    public String getStatut() {
        return statut;
    }

    @JsonProperty("Statut")
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public RackRoomData withStatut(String statut) {
        this.statut = statut;
        return this;
    }

    @JsonProperty("isAttributed")
    public boolean isIsAttributed() {
        return isAttributed;
    }

    @JsonProperty("isAttributed")
    public void setIsAttributed(boolean isAttributed) {
        this.isAttributed = isAttributed;
    }

    public RackRoomData withIsAttributed(boolean isAttributed) {
        this.isAttributed = isAttributed;
        return this;
    }

    @JsonProperty("Guests")
    public List<Guest> getGuests() {
        return guests;
    }

    @JsonProperty("Guests")
    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public RackRoomData withGuests(List<Guest> guests) {
        this.guests = guests;
        return this;
    }

    @JsonProperty("EtatTV")
    public boolean isEtatTV() {
        return etatTV;
    }

    @JsonProperty("EtatTV")
    public void setEtatTV(boolean etatTV) {
        this.etatTV = etatTV;
    }

    public RackRoomData withEtatTV(boolean etatTV) {
        this.etatTV = etatTV;
        return this;
    }

    @JsonProperty("EtatBar")
    public boolean isEtatBar() {
        return etatBar;
    }

    @JsonProperty("EtatBar")
    public void setEtatBar(boolean etatBar) {
        this.etatBar = etatBar;
    }

    public RackRoomData withEtatBar(boolean etatBar) {
        this.etatBar = etatBar;
        return this;
    }

    @JsonProperty("EtatServiette")
    public boolean isEtatServiette() {
        return etatServiette;
    }

    @JsonProperty("EtatServiette")
    public void setEtatServiette(boolean etatServiette) {
        this.etatServiette = etatServiette;
    }

    public RackRoomData withEtatServiette(boolean etatServiette) {
        this.etatServiette = etatServiette;
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

    public RackRoomData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}