package com.zebra.rfid.demo.sdksample;

import android.util.Log;

import java.util.ArrayList;

public class AppData {
    private static AppData instance = null;
    public String s;

    public RFIDHandler rfidHandler;
    public ArrayList<ListData> listData;

    private AppData() {
        rfidHandler = new RFIDHandler();
        listData = new ArrayList<>();
    }

    public static synchronized AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    public void cleanupReader() {
        if (rfidHandler != null && rfidHandler.reader != null) {
            try {
                rfidHandler.stopInventory(); // stop if running

                if (rfidHandler.isReaderConnected()) {
                    rfidHandler.reader.Events.removeEventsListener(rfidHandler.eventHandler);
                    rfidHandler.reader.disconnect();
                }
            } catch (Exception e) {
                Log.e("AppData", "Error during RFID cleanup: " + e.getMessage());
                e.printStackTrace();
            } finally {
                rfidHandler.reader = null;
                rfidHandler.readers = null;
            }
        }
    }
}

