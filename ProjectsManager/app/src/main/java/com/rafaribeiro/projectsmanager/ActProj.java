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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActProj extends AppCompatActivity {

    DataBase db = new DataBase(this);

    Button btnAddProj;
    ConstraintLayout noProj;
    ListView listProj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_proj);

        btnAddProj = findViewById(R.id.btnAddProj);
        noProj = findViewById(R.id.noProj);
        listProj = findViewById(R.id.listProj);

        btnAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActProj.this, ActAddProj.class);
                startActivity(intent);
            }
        });

        listProj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = db.selectProjectByPosition(position);
                /*
                Go to project page
                 */
            }
        });
        listProj.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ActProj.this);
                builder.setMessage(R.string.proj_long_click_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Project project = db.selectProjectByPosition(position);
                                /*
                                Go to edit project page
                                 */
                            }
                        })
                        .setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ActProj.this);
                                builder2.setMessage(R.string.proj_del_message)
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int which) {
                                                Project project = db.selectProjectByPosition(position);
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
        if (projects.size() > 0) {
            ProjListAdapter adapter = new ProjListAdapter(this, projects);
            listProj.setAdapter(adapter);
            noProj.setVisibility(View.GONE);
            /*List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (int i = 0; i < projects.size(); i++) {
                Map<String, String> datum = new HashMap<String, String>(2);
                Project project = projects.get(i);
                datum.put("name", project.getNameProj());
                datum.put("abstract", project.getAbstractProj());
                data.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2,
                    new String[] {"name", "abstract"}, new int[] {android.R.id.text1, android.R.id.text2});
            listProj.setAdapter(adapter);*/
        } else {
            noProj.setVisibility(View.VISIBLE);
        }
    }
}
