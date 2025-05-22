package com.zebra.rfid.demo.sdksample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zebra.rfid.api3.TagData;
import com.zebra.scannercontrol.SDKHandler;

import java.util.HashSet;
import java.util.Set;


/**
 * Sample app to connect to the reader,to do inventory and basic barcode scan
 * We can also set antenna settings and singulation control
 */

public class MainActivity extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface {

    public TextView statusTextViewRFID = null;
    public TextView textrfid, scanResult;
    RFIDHandler rfidHandler;
    final static String TAG = "RFID_SAMPLE";
    public static SDKHandler sdkHandler;
    public static String fristTagScan = null;

    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RFID Handler
        statusTextViewRFID = findViewById(R.id.textViewStatusrfid);
        textrfid = findViewById(R.id.edittextrfid);
        scanResult = findViewById(R.id.scanResult);
        rfidHandler = new RFIDHandler();
        //rfidHandler.onCreate(this);


        //Scanner Initializations
        //Handling Runtime BT permissions for Android 12 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_PERMISSION_REQUEST_CODE);
            } else {
                rfidHandler.onCreate(this);
            }

        } else {
            rfidHandler.onCreate(this);
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                rfidHandler.onCreate(this);
            } else {
                Toast.makeText(this, "Bluetooth Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.antenna_settings) {
            String result = rfidHandler.Test1();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.Singulation_control) {
            String result = rfidHandler.Test2();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.Default) {
            String result = rfidHandler.Defaults();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //rfidHandler.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String result = rfidHandler.onResume();
        statusTextViewRFID.setText(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rfidHandler.onDestroy();
    }


    // Update the StartInventory method to clear the display
    public void StartInventory(View view) {
        textrfid.setText("Scanning...");
        rfidHandler.performInventory();
    }

    public void writeToTag(View view) throws InterruptedException {
//        rfidHandler.writeEPC(fristTagScan, "123456781234567812345678");
        // Or if you want to use the input field:
        TextView inputSerial = findViewById(R.id.inputSerial);
        String inputHexString = inputSerial.getText().toString();
        String inputASCIIString = asciiToHex(inputHexString);
        rfidHandler.writeEPC(fristTagScan, inputASCIIString);

    }

    public String hexToASCII(String hexString) {
        if (hexString == null || hexString.isEmpty()) {
            return "";
        }
        if (hexString.length() % 2 != 0) {// each ASCII value is 2 hex digits
            System.err.println("Hex string must have an even number of characters for ASCII conversion.");
            return "";
        }
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
        StringBuilder asciiString = new StringBuilder();
        try {
            for (int i = 0; i < hexString.length(); i += 2) {
                String hexPair = hexString.substring(i, i + 2);
                int decimal = Integer.parseInt(hexPair, 16);
                asciiString.append((char) decimal);
            }
        }catch (NumberFormatException e){
            System.err.println("Invalid hex string: " + hexString);
            return "";
    }
        return asciiString.toString();
}
    public String asciiToHex(String asciiString) {
        if (asciiString == null || asciiString.isEmpty()) {
            return "";
        }

        StringBuilder hexBuilder = new StringBuilder();
        try {
            for (int i = 0; i < asciiString.length(); i++) {
                int ascii = (int) asciiString.charAt(i);
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

public void scanCode(View view) {
    rfidHandler.scanCode();

}


public void testFunction(View view) {
    rfidHandler.testFunction();
}

public void StopInventory(View view) {
    rfidHandler.stopInventory();
}

private StringBuilder tagListBuilder = new StringBuilder();
private final Set<String> seenTags = new HashSet<>();

@Override
public void handleTagdata(TagData[] tagData) {
    if (tagData == null || tagData.length == 0) return;

    fristTagScan = tagData[0].getTagID();

    synchronized (this) {
        // Clear the builder only if we're starting a new inventory
        if (textrfid.getText().toString().equals("Scanning...")) {
            tagListBuilder = new StringBuilder();
            seenTags.clear(); // Reset duplicates tracking
        }

        // Append new tags to the builder
        for (TagData tag : tagData) {
            String tagASCII = hexToASCII(tag.getTagID());
//                if (tag.getPeakRSSI() > -100) {
            String tagEntry = tagASCII + " , RSSI: " + tag.getPeakRSSI() + "\n";
            String uniqueID = tag.getTagID();
            // Only append if not already seen
            if (seenTags.add(uniqueID)) {
                tagListBuilder.append(tagEntry).append("\n");
            }
//                }
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textrfid.setText(tagListBuilder.toString());
            }
        });
    }
}

@Override
public void handleTriggerPress(boolean pressed) {
    if (pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textrfid.setText("Scanning...");
            }
        });
        rfidHandler.performInventory();
    } else {
        rfidHandler.stopInventory();
    }
}
//    @Override
//    public void handleTriggerPress(boolean pressed) {
//        if (pressed) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    textrfid.setText("");
//                }
//            });
//            rfidHandler.performInventory();
//        } else
//            rfidHandler.stopInventory();
//    }

@Override
public void barcodeData(String val) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            scanResult.setText("Scan Result : " + val);
        }
    });

}

@Override
public void sendToast(String val) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, val, Toast.LENGTH_SHORT).show();
        }
    });

}


}