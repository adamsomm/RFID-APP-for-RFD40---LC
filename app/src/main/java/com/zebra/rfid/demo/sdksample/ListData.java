package com.zebra.rfid.demo.sdksample;

public class ListData {
    private String tagID;
    private String rssi;

    public ListData(String tagID, String rssi) {
        this.tagID = tagID;
        this.rssi = rssi;
    }

    public String getTagID() {
        return tagID;
    }

    public void setTagID(String tagID) {
        this.tagID = tagID;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}
