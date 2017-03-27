package com.hotix.myhotixhousekeeping.entities;

/**
 * Created by ziedrebhi on 23/03/2017.
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
        "prodId",
        "typeHebId",
        "statutId",
        "typeProd",
        "attributed",
        "prodNum"
})
public class AffectationFMData implements Serializable {

    private final static long serialVersionUID = -5926894081651855288L;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("prodId")
    private int prodId;
    @JsonProperty("typeHebId")
    private int typeHebId;
    @JsonProperty("statutId")
    private int statutId;
    @JsonProperty("typeProd")
    private int typeProd;
    @JsonProperty("attributed")
    private boolean attributed;
    @JsonProperty("prodNum")
    private String prodNum;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public AffectationFMData() {
    }

    /**
     * @param id
     * @param typeProd
     * @param attributed
     * @param statutId
     * @param prodId
     * @param typeHebId
     * @param prodNum
     */
    public AffectationFMData(int id, int prodId, int typeHebId, int statutId, int typeProd, boolean attributed, String prodNum) {
        super();
        this.id = id;
        this.prodId = prodId;
        this.statutId = statutId;
        this.typeProd = typeProd;
        this.attributed = attributed;
        this.prodNum = prodNum;
        this.typeHebId = typeHebId;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public AffectationFMData withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("typeHebId")
    public int getTypeHebId() {
        return typeHebId;
    }

    @JsonProperty("typeHebId")
    public void setTypeHebId(int typeHebId) {
        this.typeHebId = typeHebId;
    }

    public AffectationFMData withTypeHebId(int typeHebId) {
        this.typeHebId = typeHebId;
        return this;
    }

    @JsonProperty("prodId")
    public int getProdId() {
        return prodId;
    }

    @JsonProperty("prodId")
    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public AffectationFMData withProdId(int prodId) {
        this.prodId = prodId;
        return this;
    }

    @JsonProperty("statutId")
    public int getStatutId() {
        return statutId;
    }

    @JsonProperty("statutId")
    public void setStatutId(int statutId) {
        this.statutId = statutId;
    }

    public AffectationFMData withStatutId(int statutId) {
        this.statutId = statutId;
        return this;
    }

    @JsonProperty("typeProd")
    public int getTypeProd() {
        return typeProd;
    }

    @JsonProperty("typeProd")
    public void setTypeProd(int typeProd) {
        this.typeProd = typeProd;
    }

    public AffectationFMData withTypeProd(int typeProd) {
        this.typeProd = typeProd;
        return this;
    }

    @JsonProperty("attributed")
    public boolean isAttributed() {
        return attributed;
    }

    @JsonProperty("attributed")
    public void setAttributed(boolean attributed) {
        this.attributed = attributed;
    }

    public AffectationFMData withAttributed(boolean attributed) {
        this.attributed = attributed;
        return this;
    }

    @JsonProperty("prodNum")
    public String getProdNum() {
        return prodNum;
    }

    @JsonProperty("prodNum")
    public void setProdNum(String prodNum) {
        this.prodNum = prodNum;
    }

    public AffectationFMData withProdNum(String prodNum) {
        this.prodNum = prodNum;
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

    public AffectationFMData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}