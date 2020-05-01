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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

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
                finish();
            }
        });

        listStu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView stuListID = (TextView)view.findViewById(R.id.stuListID);
                Study study = db.selectStudy(Integer.parseInt(stuListID.getText().toString()));
                //Study study = db.selectStudyByPosition(position);
                Intent intent = new Intent(ActStu.this, ActShowStu.class);
                intent.putExtra("STUID", study.getIdStu());
                startActivity(intent);
                finish();
            }
        });
        listStu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActStu.this);
                builder.setMessage(R.string.stu_long_click_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView stuListID = (TextView)view.findViewById(R.id.stuListID);
                                Study study = db.selectStudy(Integer.parseInt(stuListID.getText().toString()));
                                //Study study = db.selectStudyByPosition(position);
                                Intent intent = new Intent(ActStu.this, ActEditStu.class);
                                intent.putExtra("STUID", study.getIdStu());
                                startActivity(intent);
                                finish();
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
                                                TextView stuListID = (TextView)view.findViewById(R.id.stuListID);
                                                Study study = db.selectStudy(Integer.parseInt(stuListID.getText().toString()));
                                                //Study study = db.selectStudyByPosition(position);
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
        ArrayList<Study> stuList = sortStuList(studies);
        if (studies.size() > 0) {
            StuListAdapter adapter = new StuListAdapter(this, stuList);
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

    private ArrayList<Study> sortStuList(ArrayList<Study> studies) {
        Comparator<Study> comparator = new Comparator<Study>() {
            @Override
            public int compare(Study s1, Study s2) {
                int prog1 = s1.getProgress(ActStu.this);
                int prog2 = s2.getProgress(ActStu.this);

                return prog1 - prog2;
            }
        };
        studies.sort(comparator);
        return studies;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActStu.this, ActMain.class);
        startActivity(intent);
        finish();
    }
}
