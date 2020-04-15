package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class ActStu extends AppCompatActivity {

    DataBase db = new DataBase(this);

    Button btnAddStu, btnBackStu;
    ConstraintLayout noStu;
    ListView listStu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_stu);

        btnAddStu = findViewById(R.id.btnAddStu);
        btnBackStu = findViewById(R.id.btnBackStu);
        noStu = findViewById(R.id.noStu);
        listStu = findViewById(R.id.listStu);

        btnAddStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActStu.this, ActAddStu.class);
                startActivity(intent);
            }
        });

        listStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Study study = db.selectStudyByPosition(position);
                /*
                Go to Study page
                 */
            }
        });
        listStu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActStu.this);
                builder.setMessage(R.string.stu_long_click_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Study study = db.selectStudyByPosition(position);
                                /*
                                Go to edit study page
                                 */
                            }
                        })
                        .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ActStu.this);
                                builder2.setMessage(R.string.stu_del_message)
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int which) {
                                                Study study = db.selectStudyByPosition(position);
                                                db.deleteStudy(study.getIdStu());
                                                dialog1.cancel();
                                                dialog.cancel();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition(0, 0);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int which) {
                                                dialog1.cancel();
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alert2 = builder2.create();
                                alert2.show();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        ArrayList<Study> studies = db.selectAllStudies();
        if (studies.size() > 0) {
            StuListAdapter adapter = new StuListAdapter(this, studies);
            listStu.setAdapter(adapter);
            noStu.setVisibility(View.GONE);
        } else {
            noStu.setVisibility(View.VISIBLE);
        }

        btnBackStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActStu.this, ActMain.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActStu.this, ActMain.class);
        startActivity(intent);
        finish();
    }
}
