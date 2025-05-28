package com.zebra.rfid.demo.sdksample;

import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashSet;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {

    private RFIDHandler rfidHandler;
    private String tagID;
    public TextView textrfid, scanResult;
    private final Set<String> seenTags = new HashSet<>();
    private StringBuilder tagListBuilder = new StringBuilder();
    private TextView nameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_options);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        rfidHandler = AppData.getInstance().rfidHandler;

        if (rfidHandler != null && rfidHandler.isInventoryRunning) {
            rfidHandler.stopInventory();
            rfidHandler.isInventoryRunning = false;
        } else if (rfidHandler == null) {
            Toast.makeText(this, "RFID Handler not initialized", Toast.LENGTH_SHORT).show();
        }

        tagID = getIntent().getStringExtra("TagID");
        nameText = findViewById(R.id.IDtextView);
        nameText.setText(tagID != null ? tagID : "No Tag ID");
    }


    // Remove throws from this method (handle inside)
    public void writeToTag2(View view) {
        if (rfidHandler == null || tagID == null) {
            Toast.makeText(this, "Handler or TagID not ready", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rfidHandler.isReaderConnected()) {
            Toast.makeText(this, "Reader not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView inputSerial = findViewById(R.id.inputSerial2);
        String inputHexString = inputSerial.getText().toString();
        if (inputHexString.isEmpty()) {
            Toast.makeText(this, "Please enter a serial number", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputASCIIString = asciiToHex(inputHexString);
        String hexTagID = asciiToHex(tagID);
        if (rfidHandler.writeEPC(hexTagID, inputASCIIString)){
            tagID = inputHexString;
            runOnUiThread(() -> {
                nameText.setText(tagID != null ? tagID : "No Tag ID");
            });
        }
    }
    public String asciiToHex(String asciiString) {
        if (asciiString == null || asciiString.isEmpty()) {
            return "";
        }

        StringBuilder hexBuilder = new StringBuilder();
        try {
            for (int i = 0; i < asciiString.length(); i++) {
                int ascii = asciiString.charAt(i);
                String hex = Integer.toHexString(ascii).toUpperCase();
                if (hex.length() == 1) {
                    hexBuilder.append("0"); // Ensure two-digit hex
                }
                hexBuilder.append(hex);
            }
        } catch (Exception e) {
            System.err.println("Error converting ASCII to Hex: " + e.getMessage());
            return "";
        }

        String hexString = hexBuilder.toString();

        if (hexString.length() < 24) {
            // Pad with '20' until length is 24
            while (hexString.length() < 24) {
                hexString += "20";
            }
            // Trim in case we overshoot 24 characters
            if (hexString.length() > 24) {
                hexString = hexString.substring(0, 24);
            }
        } else if (hexString.length() > 24) {
            // Trim the string to 24 characters
            hexString = hexString.substring(0, 24);
        }

        return hexString;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        rfidHandler.onResume();
//        statusTextViewRFID.setText(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }

}