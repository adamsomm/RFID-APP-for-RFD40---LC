package com.zebra.rfid.demo.sdksample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    RFIDHandler rfidHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_options);
        TextView nameText = findViewById(R.id.IDtextView);

        String tagID = getIntent().getStringExtra("TagID");
        nameText.setText(tagID);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            tagID = extras.getString("TagID");
            nameText.setText(tagID);
        }
        nameText.setText(tagID);
    }


}

