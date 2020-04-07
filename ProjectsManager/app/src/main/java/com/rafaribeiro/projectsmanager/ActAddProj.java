package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActAddProj extends AppCompatActivity {

    Button btnBackAddProj, btnAddAddProj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_proj);

        // Views
        btnBackAddProj = findViewById(R.id.btnBackAddProj);
        btnAddAddProj = findViewById(R.id.btnAddAddProj);

        // On Back button pressed
        btnBackAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActAddProj.super.onBackPressed();
            }
        });

        //On Add button pressed
        btnAddAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Verification and Add code
                 */
            }
        });
    }
}
