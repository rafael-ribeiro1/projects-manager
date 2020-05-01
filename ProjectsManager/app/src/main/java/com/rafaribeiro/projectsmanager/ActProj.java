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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActProj extends AppCompatActivity {

    DataBase db = new DataBase(this);

    Button btnAddProj, btnBackProj;
    ConstraintLayout noProj;
    ListView listProj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_proj);

        btnAddProj = findViewById(R.id.btnAddProj);
        btnBackProj = findViewById(R.id.btnBackProj);
        noProj = findViewById(R.id.noProj);
        listProj = findViewById(R.id.listProj);

        btnAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProj.this, ActAddProj.class);
                startActivity(intent);
                finish();
            }
        });

        listProj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView projListID = (TextView)view.findViewById(R.id.projListID);
                Project project = db.selectProject(Integer.parseInt(projListID.getText().toString()));
                //Project project = db.selectProjectByPosition(position);
                Intent intent = new Intent(ActProj.this, ActShowProj.class);
                intent.putExtra("PROJID", project.getIdProj());
                startActivity(intent);
                finish();
            }
        });
        listProj.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActProj.this);
                builder.setMessage(R.string.proj_long_click_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView projListID = (TextView)view.findViewById(R.id.projListID);
                                Project project = db.selectProject(Integer.parseInt(projListID.getText().toString()));
                                //Project project = db.selectProjectByPosition(position);
                                Intent intent = new Intent(ActProj.this, ActEditProj.class);
                                intent.putExtra("PROJID", project.getIdProj());
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ActProj.this);
                                builder2.setMessage(getResources().getString(R.string.proj_del_message))
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int which) {
                                                TextView projListID = (TextView)view.findViewById(R.id.projListID);
                                                Project project = db.selectProject(Integer.parseInt(projListID.getText().toString()));
                                                //Project project = db.selectProjectByPosition(position);
                                                db.deleteProject(project.getIdProj());
                                                dialog1.cancel();
                                                dialog.cancel();
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(getIntent());
                                                overridePendingTransition(0, 0);
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
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

        ArrayList<Project> projects = db.selectAllProjects();
        ArrayList<Project> projList = sortProjList(projects);
        if (projects.size() > 0) {
            ProjListAdapter adapter = new ProjListAdapter(this, projList);
            listProj.setAdapter(adapter);
            noProj.setVisibility(View.GONE);
        } else {
            noProj.setVisibility(View.VISIBLE);
        }

        btnBackProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProj.this, ActMain.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private ArrayList<Project> sortProjList(ArrayList<Project> projects) {
        ArrayList<Project> projList = new ArrayList<>();
        for (Project project: projects) if (project.getState() == Project.DOING) projList.add(project);
        for (Project project: projects) if (project.getState() == Project.TO_DO) projList.add(project);
        for (Project project: projects) if (project.getState() == Project.DONE) projList.add(project);
        return projList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActProj.this, ActMain.class);
        startActivity(intent);
        finish();
    }
}
