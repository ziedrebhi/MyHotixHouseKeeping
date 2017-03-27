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
        "Date",
        "CapaCHB",
        "CapaPax",
        "ResCHB",
        "ResPax",
        "ArrCHB",
        "ArrPax",
        "DepCHB",
        "DepPax",
        "TotCHB",
        "TotPax",
        "OccCHB",
        "OccPax",
        "OPTCHB",
        "OPTPax",
        "Reste"
})
public class PrevisionData implements Serializable {

    private final static long serialVersionUID = -1156386179225656172L;
    @JsonProperty("Date")
    private String date;
    @JsonProperty("CapaCHB")
    private String capaCHB;
    @JsonProperty("CapaPax")
    private String capaPax;
    @JsonProperty("ResCHB")
    private String resCHB;
    @JsonProperty("ResPax")
    private String resPax;
    @JsonProperty("ArrCHB")
    private String arrCHB;
    @JsonProperty("ArrPax")
    private String arrPax;
    @JsonProperty("DepCHB")
    private String depCHB;
    @JsonProperty("DepPax")
    private String depPax;
    @JsonProperty("TotCHB")
    private String totCHB;
    @JsonProperty("TotPax")
    private String totPax;
    @JsonProperty("OccCHB")
    private String occCHB;
    @JsonProperty("OccPax")
    private String occPax;
    @JsonProperty("OPTCHB")
    private String oPTCHB;
    @JsonProperty("OPTPax")
    private String oPTPax;
    @JsonProperty("Reste")
    private String reste;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public PrevisionData() {
    }

    /**
     * @param occPax
     * @param totCHB
     * @param arrPax
     * @param reste
     * @param totPax
     * @param oPTPax
     * @param capaCHB
     * @param date
     * @param resPax
     * @param resCHB
     * @param depPax
     * @param arrCHB
     * @param oPTCHB
     * @param capaPax
     * @param depCHB
     * @param occCHB
     */
    public PrevisionData(String date, String capaCHB, String capaPax, String resCHB, String resPax, String arrCHB, String arrPax, String depCHB, String depPax, String totCHB, String totPax, String occCHB, String occPax, String oPTCHB, String oPTPax, String reste) {
        super();
        this.date = date;
        this.capaCHB = capaCHB;
        this.capaPax = capaPax;
        this.resCHB = resCHB;
        this.resPax = resPax;
        this.arrCHB = arrCHB;
        this.arrPax = arrPax;
        this.depCHB = depCHB;
        this.depPax = depPax;
        this.totCHB = totCHB;
        this.totPax = totPax;
        this.occCHB = occCHB;
        this.occPax = occPax;
        this.oPTCHB = oPTCHB;
        this.oPTPax = oPTPax;
        this.reste = reste;
    }

    @JsonProperty("Date")
    public String getDate() {
        return date;
    }

    @JsonProperty("Date")
    public void setDate(String date) {
        this.date = date;
    }

    public PrevisionData withDate(String date) {
        this.date = date;
        return this;
    }

    @JsonProperty("CapaCHB")
    public String getCapaCHB() {
        return capaCHB;
    }

    @JsonProperty("CapaCHB")
    public void setCapaCHB(String capaCHB) {
        this.capaCHB = capaCHB;
    }

    public PrevisionData withCapaCHB(String capaCHB) {
        this.capaCHB = capaCHB;
        return this;
    }

    @JsonProperty("CapaPax")
    public String getCapaPax() {
        return capaPax;
    }

    @JsonProperty("CapaPax")
    public void setCapaPax(String capaPax) {
        this.capaPax = capaPax;
    }

    public PrevisionData withCapaPax(String capaPax) {
        this.capaPax = capaPax;
        return this;
    }

    @JsonProperty("ResCHB")
    public String getResCHB() {
        return resCHB;
    }

    @JsonProperty("ResCHB")
    public void setResCHB(String resCHB) {
        this.resCHB = resCHB;
    }

    public PrevisionData withResCHB(String resCHB) {
        this.resCHB = resCHB;
        return this;
    }

    @JsonProperty("ResPax")
    public String getResPax() {
        return resPax;
    }

    @JsonProperty("ResPax")
    public void setResPax(String resPax) {
        this.resPax = resPax;
    }

    public PrevisionData withResPax(String resPax) {
        this.resPax = resPax;
        return this;
    }

    @JsonProperty("ArrCHB")
    public String getArrCHB() {
        return arrCHB;
    }

    @JsonProperty("ArrCHB")
    public void setArrCHB(String arrCHB) {
        this.arrCHB = arrCHB;
    }

    public PrevisionData withArrCHB(String arrCHB) {
        this.arrCHB = arrCHB;
        return this;
    }

    @JsonProperty("ArrPax")
    public String getArrPax() {
        return arrPax;
    }

    @JsonProperty("ArrPax")
    public void setArrPax(String arrPax) {
        this.arrPax = arrPax;
    }

    public PrevisionData withArrPax(String arrPax) {
        this.arrPax = arrPax;
        return this;
    }

    @JsonProperty("DepCHB")
    public String getDepCHB() {
        return depCHB;
    }

    @JsonProperty("DepCHB")
    public void setDepCHB(String depCHB) {
        this.depCHB = depCHB;
    }

    public PrevisionData withDepCHB(String depCHB) {
        this.depCHB = depCHB;
        return this;
    }

    @JsonProperty("DepPax")
    public String getDepPax() {
        return depPax;
    }

    @JsonProperty("DepPax")
    public void setDepPax(String depPax) {
        this.depPax = depPax;
    }

    public PrevisionData withDepPax(String depPax) {
        this.depPax = depPax;
        return this;
    }

    @JsonProperty("TotCHB")
    public String getTotCHB() {
        return totCHB;
    }

    @JsonProperty("TotCHB")
    public void setTotCHB(String totCHB) {
        this.totCHB = totCHB;
    }

    public PrevisionData withTotCHB(String totCHB) {
        this.totCHB = totCHB;
        return this;
    }

    @JsonProperty("TotPax")
    public String getTotPax() {
        return totPax;
    }

    @JsonProperty("TotPax")
    public void setTotPax(String totPax) {
        this.totPax = totPax;
    }

    public PrevisionData withTotPax(String totPax) {
        this.totPax = totPax;
        return this;
    }

    @JsonProperty("OccCHB")
    public String getOccCHB() {
        return occCHB;
    }

    @JsonProperty("OccCHB")
    public void setOccCHB(String occCHB) {
        this.occCHB = occCHB;
    }

    public PrevisionData withOccCHB(String occCHB) {
        this.occCHB = occCHB;
        return this;
    }

    @JsonProperty("OccPax")
    public String getOccPax() {
        return occPax;
    }

    @JsonProperty("OccPax")
    public void setOccPax(String occPax) {
        this.occPax = occPax;
    }

    public PrevisionData withOccPax(String occPax) {
        this.occPax = occPax;
        return this;
    }

    @JsonProperty("OPTCHB")
    public String getOPTCHB() {
        return oPTCHB;
    }

    @JsonProperty("OPTCHB")
    public void setOPTCHB(String oPTCHB) {
        this.oPTCHB = oPTCHB;
    }

    public PrevisionData withOPTCHB(String oPTCHB) {
        this.oPTCHB = oPTCHB;
        return this;
    }

    @JsonProperty("OPTPax")
    public String getOPTPax() {
        return oPTPax;
    }

    @JsonProperty("OPTPax")
    public void setOPTPax(String oPTPax) {
        this.oPTPax = oPTPax;
    }

    public PrevisionData withOPTPax(String oPTPax) {
        this.oPTPax = oPTPax;
        return this;
    }

    @JsonProperty("Reste")
    public String getReste() {
        return reste;
    }

    @JsonProperty("Reste")
    public void setReste(String reste) {
        this.reste = reste;
    }

    public PrevisionData withReste(String reste) {
        this.reste = reste;
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

    public PrevisionData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}