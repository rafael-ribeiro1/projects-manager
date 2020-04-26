package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ActShowStu extends AppCompatActivity {

    DataBase db = new DataBase(this);

    boolean fromProj;
    int id;

    Button btnBack, btnDel, btnEdit;
    TextView tvName, tvFrom, tvLink, noMod;
    LinearLayout mod;
    CheckBox cbSDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_show_stu);

        // Verify if (valid) STUDY ID was passed. If not, goes to Studies List Activity
        fromProj = getIntent().getBooleanExtra("FROMPROJ", false);
        id = getIntent().getIntExtra("STUID", 0);
        final Study study = db.selectStudy(id);
        if (study == null) onBackPressed();

        // Views
        btnBack = findViewById(R.id.btnBackShowStu);
        btnDel = findViewById(R.id.btnDelShowStu);
        btnEdit = findViewById(R.id.btnEditShowStu);
        tvName = findViewById(R.id.tvNameShowStu);
        tvFrom = findViewById(R.id.tvFromShowStu);
        tvLink = findViewById(R.id.tvLinkShowStu);
        noMod = findViewById(R.id.noModShowStu);
        mod = findViewById(R.id.modShowStu);
        cbSDone = findViewById(R.id.cbSDoneShowStu);

        // Study values atribution to Views
        tvName.setText(study.getNameStu());
        tvFrom.setText(study.getFromStu().isEmpty() ? getResources().getString(R.string.n_a) : study.getFromStu());
        if (study.getLink().isEmpty()) tvLink.setText(R.string.n_a);
        else {
            tvLink.setText(study.getLink());
            Linkify.addLinks(tvLink, Linkify.WEB_URLS);
        }
        if (study.getnMod() > 0) {
            cbSDone.setVisibility(View.GONE);
            noMod.setVisibility(View.GONE);
            ArrayList<Module> mods = db.selectModulesOfStudy(id);
            for (Module m: mods) addModuleView(m);
        } else {
            if (study.getnMod() == -1) cbSDone.setChecked(true);
            cbSDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int done = isChecked ? -1 : 0;
                    if (study.getnMod() != done) db.updateStudy(new Study(study.getIdStu(),
                            study.getNameStu(), study.getFromStu(), study.getLink(),
                            study.getnMod()==-1 ? 0 : -1));
                }
            });
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActShowStu.this);
                builder.setMessage(getResources().getString(R.string.stu_del_message))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteStudy(id);
                                dialog.cancel();
                                goToStuList();
                                finishAffinity();
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActShowStu.this, ActEditStu.class);
                intent.putExtra("STUID", id);
                intent.putExtra("FROMPROJ", fromProj);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addModuleView(final Module module) {
        RelativeLayout modRL = new RelativeLayout(this);
        modRL.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        CheckBox cbMod = new CheckBox(this);
        RelativeLayout.LayoutParams cbParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        cbParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        cbMod.setLayoutParams(cbParams);
        cbMod.setId(View.generateViewId());
        if (module.getState() == Module.DONE) cbMod.setChecked(true);
        cbMod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int state = isChecked ? Module.DONE : Module.TO_DO;
                if (module.getState() != state) {
                    db.updateMod(new Module(module.getIdMod(), module.getStudyId(), module.getTitle(),
                            module.getState()==Module.DONE ? Module.TO_DO : Module.DONE));
                }
            }
        });
        TextView tvMod = new TextView(this);
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams.addRule(RelativeLayout.END_OF, cbMod.getId());
        tvParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvMod.setLayoutParams(tvParams);
        tvMod.setTextColor(getResources().getColor(android.R.color.black));
        tvMod.setTextSize(17);
        tvMod.setText(module.getTitle());
        modRL.addView(cbMod);
        modRL.addView(tvMod);
        mod.addView(modRL);
    }

    @Override
    public void onBackPressed() {
        if (fromProj) super.onBackPressed();
        else goToStuList();
    }

    private void goToStuList() {
        Intent intent = new Intent(ActShowStu.this, ActStu.class);
        startActivity(intent);
        finish();
    }

}
