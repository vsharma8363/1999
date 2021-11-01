package com.party.technologies.nineteen_ninety_nine.data;

import java.util.HashMap;
import java.util.Map;

public class Party {

    private Map<String, Object> attributes;

    public Party(String hostID, String name, String description, String address, String apartmentUnit, double longitude, double latitude) {
        attributes = new HashMap<String, Object>();
        attributes.put("hostID", hostID);
        attributes.put("partyName", name);
        attributes.put("partyDescription", description);
        attributes.put("address", address);
        attributes.put("apartment_unit", apartmentUnit);
        attributes.put("longitude", longitude);
        attributes.put("latitude", latitude);
    }

    public Party(Map<String, Object> serialized) {
        this.attributes = serialized;
    }

    public String getHostID() {
        return (String)this.attributes.get("hostID");
    }

    public String getPartyName() {
        return (String)this.attributes.get("partyName");
    }

    public String getPartyDescription() {
        return (String)this.attributes.get("partyDescription");
    }

    public String getAddress() {
        return (String)this.attributes.get("address");
    }

    public String getApartment_unit() {
        return (String)this.attributes.get("apartment_unit");
    }

    public double getLongitude() {
        return (Double)this.attributes.get("longitude");
    }

    public double getLatitude() {
        return (Double)this.attributes.get("latitude");
    }

    public void setPartyName(String partyName) {
        attributes.put("partyName", partyName);
    }

    public void setPartyDescription(String partyDescription) {
        attributes.put("partyDescription", partyDescription);
    }

    public void setAddress(String address) {
        attributes.put("address", address);
    }

    public void setApartment_unit(String apartment_unit) {
        attributes.put("apartment_unit", apartment_unit);
    }

    public void setPartyID(String partyID) {
        attributes.put("party_id", partyID);
    }

    public String getPartyID() {
        return (String)attributes.get("party_id");
    }

    public void setLongitude(double longitude) {
        attributes.put("longitude", longitude);
    }

    public void setLatitude(double latitude) {
        attributes.put("latitude", latitude);
    }

    public Map<String, Object> getHashMap() {
        return this.attributes;
    }
}