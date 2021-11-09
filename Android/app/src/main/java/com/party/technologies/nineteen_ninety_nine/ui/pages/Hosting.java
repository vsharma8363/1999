package com.party.technologies.nineteen_ninety_nine.ui.pages;

import androidx.appcompat.app.AppCompatActivity;
import com.party.technologies.nineteen_ninety_nine.R;
import com.party.technologies.nineteen_ninety_nine.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hosting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);
        // Define back button logic.
        Button back = findViewById(R.id.back_hosting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });
    }
}