package com.zebra.rfid.demo.sdksample;

import java.util.ArrayList; /**
 * Sample app to connect to the reader,to do inventory and basic barcode scan
 * We can also set antenna settings and singulation control
 */
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
}
