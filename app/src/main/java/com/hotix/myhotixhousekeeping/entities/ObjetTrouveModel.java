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
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "status"
})
public class ObjetTrouveModel implements Serializable {

    private final static long serialVersionUID = 3361588951800555833L;
    @JsonProperty("data")
    private List<ObjetTrouveData> data = null;
    @JsonProperty("status")
    private boolean status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public ObjetTrouveModel() {
    }

    /**
     * @param status
     * @param data
     */
    public ObjetTrouveModel(List<ObjetTrouveData> data, boolean status) {
        super();
        this.data = data;
        this.status = status;
    }

    @JsonProperty("data")
    public List<ObjetTrouveData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<ObjetTrouveData> data) {
        this.data = data;
    }

    public ObjetTrouveModel withData(List<ObjetTrouveData> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("status")
    public boolean isStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(boolean status) {
        this.status = status;
    }

    public ObjetTrouveModel withStatus(boolean status) {
        this.status = status;
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

    public ObjetTrouveModel withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}