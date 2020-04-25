package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ActShowProj extends AppCompatActivity {

    DataBase db = new DataBase(this);

    int id;

    Button btnBack, btnDel, btnEdit;
    TextView tvName, tvAbst, tvDesc, noFunc, noReq;
    ImageView imgState;
    LinearLayout func, req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_show_proj);

        // Firstly verify if one valid ProjectID was passed via Intent. If not, goes to Project List Activity
        id = getIntent().getIntExtra("PROJID", 0);
        Project project = db.selectProject(id);
        if (project == null) goBack();

        // Views
        btnBack = findViewById(R.id.btnBackShowProj);
        btnDel = findViewById(R.id.btnDelShowProj);
        btnEdit = findViewById(R.id.btnEditShowProj);
        tvName = findViewById(R.id.tvNameShowProj);
        tvAbst = findViewById(R.id.tvAbstShowProj);
        tvDesc = findViewById(R.id.tvDescShowProj);
        imgState = findViewById(R.id.imgStateShowProj);
        func = findViewById(R.id.funcShowProj);
        noFunc = findViewById(R.id.noFuncShowProj);
        req = findViewById(R.id.reqShowProj);
        noReq = findViewById(R.id.noReqShowProj);

        // Set Project values on Views
        tvName.setText(project.getNameProj());
        switch (project.getState()) {
            case State.TO_DO:
                imgState.setImageResource(R.drawable.idea);
                break;
            case State.DOING:
                imgState.setImageResource(R.drawable.doing);
                break;
            case State.DONE:
                imgState.setImageResource(R.drawable.done);
                break;
        }
        tvAbst.setText(project.getAbstractProj());
        tvDesc.setText(project.getDescProj());
        String[] funcs = project.getFunctionalities();
        if (funcs.length > 0) {
            noFunc.setVisibility(View.GONE);
            for (String strFunc: funcs) {
                TextView tvFunc = new TextView(this);
                tvFunc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tvFunc.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
                tvFunc.setText(String.format("• %s", strFunc));
                func.addView(tvFunc);
            }
        }
        ArrayList<Requirement> reqs = db.selectReqsOfProject(id);
        if (reqs.size() > 0) {
            noReq.setVisibility(View.GONE);
            for (Requirement r: reqs) {
                TextView tvReq = new TextView(this);
                tvReq.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (r.getStudyId() > 0) {
                    tvReq.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    Study study = db.selectStudy(r.getStudyId());
                    tvReq.setText(study.getNameStu());
                    tvReq.setTypeface(null, Typeface.BOLD);
                    final int stuId = r.getStudyId();
                    tvReq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ActShowProj.this, ActShowStu.class);
                            intent.putExtra("STUID", stuId);
                            intent.putExtra("FROMPROJ", true);
                            startActivity(intent);
                        }
                    });
                } else {
                    tvReq.setTextColor(getResources().getColor(android.R.color.secondary_text_light));
                    tvReq.setText(r.getReq());
                }
                req.addView(tvReq);
            }
        }


        // btnBack OnClickListener - Go to Projects List Activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goBack(); }
        });

        // btnDel OnClickListener - Delete the Project and go to Projects List Activity
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActShowProj.this);
                builder.setMessage(getResources().getString(R.string.proj_del_message))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteProject(id);
                                dialog.cancel();
                                goBack();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog delete = builder.create();
                delete.show();
            }
        });

        // btnEdit OnClickListener - Go to Edit Project Activity
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActShowProj.this, ActEditProj.class);
                intent.putExtra("PROJID", id);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(ActShowProj.this, ActProj.class);
        startActivity(intent);
        finish();
    }
}
