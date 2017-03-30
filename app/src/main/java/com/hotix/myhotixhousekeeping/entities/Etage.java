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
        "BlocId",
        "Id",
        "Name"
})
public class Etage implements Serializable {

    private final static long serialVersionUID = 3813561247730180110L;
    @JsonProperty("BlocId")
    private int blocId;
    @JsonProperty("Id")
    private int id;
    @JsonProperty("Name")
    private String name;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Etage() {
    }

    /**
     *
     * @param id
     * @param blocId
     * @param name
     */
    public Etage(int blocId, int id, String name) {
        super();
        this.blocId = blocId;
        this.id = id;
        this.name = name;
    }

    @JsonProperty("BlocId")
    public int getBlocId() {
        return blocId;
    }

    @JsonProperty("BlocId")
    public void setBlocId(int blocId) {
        this.blocId = blocId;
    }

    public Etage withBlocId(int blocId) {
        this.blocId = blocId;
        return this;
    }

    @JsonProperty("Id")
    public int getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(int id) {
        this.id = id;
    }

    public Etage withId(int id) {
        this.id = id;
        return this;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    public Etage withName(String name) {
        this.name = name;
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

    public Etage withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}