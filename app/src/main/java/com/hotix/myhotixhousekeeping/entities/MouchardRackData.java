package com.hotix.myhotixhousekeeping.entities;

/**
 * Created by ziedrebhi on 25/03/2017.
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
        "Room",
        "Operation",
        "User",
        "Poste",
        "Date"
})
public class MouchardRackData implements Serializable {

    private final static long serialVersionUID = 7091465359412252385L;
    @JsonProperty("Room")
    private String room;
    @JsonProperty("Operation")
    private String operation;
    @JsonProperty("User")
    private String user;
    @JsonProperty("Poste")
    private String poste;
    @JsonProperty("Date")
    private String date;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public MouchardRackData() {
    }

    /**
     * @param operation
     * @param date
     * @param user
     * @param poste
     * @param room
     */
    public MouchardRackData(String room, String operation, String user, String poste, String date) {
        super();
        this.room = room;
        this.operation = operation;
        this.user = user;
        this.poste = poste;
        this.date = date;
    }

    @JsonProperty("Room")
    public String getRoom() {
        return room;
    }

    @JsonProperty("Room")
    public void setRoom(String room) {
        this.room = room;
    }

    public MouchardRackData withRoom(String room) {
        this.room = room;
        return this;
    }

    @JsonProperty("Operation")
    public String getOperation() {
        return operation;
    }

    @JsonProperty("Operation")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    public MouchardRackData withOperation(String operation) {
        this.operation = operation;
        return this;
    }

    @JsonProperty("User")
    public String getUser() {
        return user;
    }

    @JsonProperty("User")
    public void setUser(String user) {
        this.user = user;
    }

    public MouchardRackData withUser(String user) {
        this.user = user;
        return this;
    }

    @JsonProperty("Poste")
    public String getPoste() {
        return poste;
    }

    @JsonProperty("Poste")
    public void setPoste(String poste) {
        this.poste = poste;
    }

    public MouchardRackData withPoste(String poste) {
        this.poste = poste;
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

    public MouchardRackData withDate(String date) {
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

    public MouchardRackData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
