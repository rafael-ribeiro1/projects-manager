package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActMain extends AppCompatActivity {

    ImageButton btnProj, btnStu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProj = findViewById(R.id.btnProj);
        btnStu = findViewById(R.id.btnStu);

        btnProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActMain.this, ActProj.class);
                startActivity(intent);
                finish();
            }
        });
        btnStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActMain.this, ActStu.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
