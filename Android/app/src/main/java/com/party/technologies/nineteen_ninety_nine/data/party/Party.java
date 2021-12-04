package com.party.technologies.nineteen_ninety_nine.data.party;

import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.party.technologies.nineteen_ninety_nine.data.user.UserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Party {

    private Map<String, Object> attributes;
    private ArrayList<String> guestsApproved;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    private long startTime;
    private long endTime;

    public ArrayList<String> getPartyImages() {
        return partyImages;
    }

    public void setPartyImages(ArrayList<String> partyImages) {
        this.partyImages = partyImages;
    }

    private ArrayList<String> partyImages;

    public void setGuestsApproved(ArrayList<String> guestsApproved) {
        this.guestsApproved = guestsApproved;
    }

    public void setGuestsPending(ArrayList<String> guestsPending) {
        this.guestsPending = guestsPending;
    }

    public void setGuestsDenied(ArrayList<String> guestsDenied) {
        this.guestsDenied = guestsDenied;
    }

    public ArrayList<String> getGuestsApproved() {
        return guestsApproved;
    }

    public ArrayList<String> getGuestsPending() {
        return guestsPending;
    }

    public ArrayList<String> getGuestsDenied() {
        return guestsDenied;
    }

    private ArrayList<String> guestsPending;
    private ArrayList<String> guestsDenied;

    public Party() {
        attributes = new HashMap<String, Object>();
        guestsApproved = new ArrayList<String>();
        partyImages = new ArrayList<String>();
        guestsPending = new ArrayList<String>();
        guestsDenied = new ArrayList<String>();
    }

    public Party(String hostID, String name, String description, String address, String apartmentUnit, double longitude, double latitude, ArrayList<String> partyImages, long startTime, long endTime) {
        attributes = new HashMap<String, Object>();
        attributes.put("hostID", hostID);
        attributes.put("partyName", name);
        attributes.put("partyDescription", description);
        attributes.put("address", address);
        if(apartmentUnit == null)
            attributes.put("apartment_unit", "None");
        else
            attributes.put("apartment_unit", apartmentUnit);
        attributes.put("longitude", longitude);
        attributes.put("latitude", latitude);
        guestsApproved = new ArrayList<String>();
        guestsPending = new ArrayList<String>();
        guestsDenied = new ArrayList<String>();
        this.partyImages = partyImages;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getHostID() {
        return (String)this.attributes.get("hostID");
    }
    public void setHostID(String hostID) {
        this.attributes.put("hostID", hostID);
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


    public void addApprovedGuest(String UID) {
        removeFromArrayList(this.guestsDenied, UID);
        removeFromArrayList(this.guestsPending, UID);
        removeFromArrayList(this.guestsApproved, UID);
        this.guestsApproved.add(UID);
    }


    public void addPendingGuest(String UID) {
        removeFromArrayList(this.guestsDenied, UID);
        removeFromArrayList(this.guestsPending, UID);
        removeFromArrayList(this.guestsApproved, UID);
        this.guestsPending.add(UID);
    }

    public void addDeniedGuest(String UID) {
        removeFromArrayList(this.guestsDenied, UID);
        removeFromArrayList(this.guestsPending, UID);
        removeFromArrayList(this.guestsApproved, UID);
        this.guestsDenied.add(UID);
    }

    public void removeFromArrayList(ArrayList<String> list, String string) {
        while(list.remove(string)) {}
    }
}