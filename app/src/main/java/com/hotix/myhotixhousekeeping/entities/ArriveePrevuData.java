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
        "SocId",
        "Resa",
        "Client",
        "DateArrive",
        "DateDepart",
        "Room"
})
public class ArriveePrevuData implements Serializable {

    private final static long serialVersionUID = -5729680505916427712L;
    @JsonProperty("SocId")
    private int socId;
    @JsonProperty("Resa")
    private int resa;
    @JsonProperty("Client")
    private String client;
    @JsonProperty("DateArrive")
    private String dateArrive;
    @JsonProperty("DateDepart")
    private String dateDepart;
    @JsonProperty("Room")
    private String room;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public ArriveePrevuData() {
    }

    /**
     * @param client
     * @param dateDepart
     * @param dateArrive
     * @param room
     * @param resa
     * @param socId
     */
    public ArriveePrevuData(int socId, int resa, String client, String dateArrive, String dateDepart, String room) {
        super();
        this.socId = socId;
        this.resa = resa;
        this.client = client;
        this.dateArrive = dateArrive;
        this.dateDepart = dateDepart;
        this.room = room;
    }

    @JsonProperty("SocId")
    public int getSocId() {
        return socId;
    }

    @JsonProperty("SocId")
    public void setSocId(int socId) {
        this.socId = socId;
    }

    public ArriveePrevuData withSocId(int socId) {
        this.socId = socId;
        return this;
    }

    @JsonProperty("Resa")
    public int getResa() {
        return resa;
    }

    @JsonProperty("Resa")
    public void setResa(int resa) {
        this.resa = resa;
    }

    public ArriveePrevuData withResa(int resa) {
        this.resa = resa;
        return this;
    }

    @JsonProperty("Client")
    public String getClient() {
        return client;
    }

    @JsonProperty("Client")
    public void setClient(String client) {
        this.client = client;
    }

    public ArriveePrevuData withClient(String client) {
        this.client = client;
        return this;
    }

    @JsonProperty("DateArrive")
    public String getDateArrive() {
        return dateArrive;
    }

    @JsonProperty("DateArrive")
    public void setDateArrive(String dateArrive) {
        this.dateArrive = dateArrive;
    }

    public ArriveePrevuData withDateArrive(String dateArrive) {
        this.dateArrive = dateArrive;
        return this;
    }

    @JsonProperty("DateDepart")
    public String getDateDepart() {
        return dateDepart;
    }

    @JsonProperty("DateDepart")
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public ArriveePrevuData withDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
        return this;
    }

    @JsonProperty("Room")
    public String getRoom() {
        return room;
    }

    @JsonProperty("Room")
    public void setRoom(String room) {
        this.room = room;
    }

    public ArriveePrevuData withRoom(String room) {
        this.room = room;
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

    public ArriveePrevuData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}