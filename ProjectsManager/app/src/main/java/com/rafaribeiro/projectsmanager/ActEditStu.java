package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActEditStu extends AppCompatActivity {

    DataBase db = new DataBase(this);

    Button btnBack, btnAdd;
    EditText edtName, edtFrom, edtLink;
    LinearLayout mod;
    ImageButton btnAddMod;
    TextView noMod;
    CheckBox cbSDone;

    ArrayList<Module> mods;
    int nMod = 0;

    boolean fromProj;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_stu);

        // Verify if (valid) STUDY ID was passed. If not, goes to Studies List Activity
        fromProj = getIntent().getBooleanExtra("FROMPROJ", false);
        id = getIntent().getIntExtra("STUID", 0);
        final Study study = db.selectStudy(id);
        if (study == null) {
            Intent intent = new Intent(ActEditStu.this, ActStu.class);
            startActivity(intent);
            finish();
        }

        // Views
        btnBack = findViewById(R.id.btnBackAddStu);
        btnAdd = findViewById(R.id.btnAddAddStu);
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_edit, 0);
        edtName = findViewById(R.id.edtNameAddStu);
        edtFrom = findViewById(R.id.edtFromAddStu);
        edtLink = findViewById(R.id.edtLinkAddStu);
        mod = findViewById(R.id.modAddStu);
        btnAddMod = findViewById(R.id.btnAddModAddStu);
        noMod = findViewById(R.id.noModAddStu);
        cbSDone = findViewById(R.id.cbSDoneAddStu);

        // Modules ArrayList inicialization
        mods = new ArrayList<>();

        // On delete module buttons click listener
        final View.OnClickListener delModListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                int idMod;
                try {
                    idMod = Integer.parseInt(tag.split("_")[1]);
                } catch (Exception e) {
                    idMod = -1;
                    Log.e("error", e.getMessage());
                }
                if (idMod != -1) {
                    String tagRL = "modRL_" + idMod;
                    RelativeLayout rl = (RelativeLayout)v.getRootView().findViewWithTag(tagRL);
                    mod.removeView(rl);
                    Module module = new Module(idMod);
                    mods.remove(module);
                    if (mods.size() == 0) {
                        noMod.setVisibility(View.VISIBLE);
                        cbSDone.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ActEditStu.this, "Error deleting module", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Values atribution to Views
        edtName.setText(study.getNameStu());
        edtFrom.setText(study.getFromStu());
        edtLink.setText(study.getLink());
        ArrayList<Module> stuMods = db.selectModulesOfStudy(id);
        for (Module m: stuMods) {
            m.setIdMod(nMod);
            mods.add(m);
            addModRL(m, delModListener);
        }
        if (study.getnMod() == -1) cbSDone.setChecked(true);
        if (study.getnMod() > 0) cbSDone.setVisibility(View.GONE);

        // Back button clicked listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed(); }
        });

        // Edit button clicked listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ActEditStu.this, R.string.fill_stu_name, Toast.LENGTH_SHORT).show();
                } else {
                    int inMod = mods.size() > 0 ? mods.size() : (cbSDone.isChecked() ? -1 : 0);
                    Study study = new Study(id, edtName.getText().toString().trim(),
                            edtFrom.getText().toString().trim(), edtLink.getText().toString().trim(), inMod);
                    db.updateStudy(study);
                    db.deleteTableMod(id);
                    for (Module m: mods) {
                        m.setStudyId(id);
                        CheckBox cbMod = v.getRootView().findViewWithTag("doneMod_" + m.getIdMod());
                        m.setState(cbMod.isChecked() ? Module.DONE : Module.TO_DO);
                        db.insertMod(m);
                    }
                    onBackPressed();
                }
            }
        });

        // Add Mod button clicked listener
        btnAddMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ActEditStu.this);
                dialog.setContentView(R.layout.dialog_add_mod);
                dialog.setCancelable(true);

                Button btnAddMod = (Button) dialog.findViewById(R.id.btnAddModAMD);
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancelAMD);
                final EditText edtTitle = (EditText) dialog.findViewById(R.id.edtTitleAMD);
                btnAddMod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtTitle.getText().toString().trim().equals("")) {
                            Toast.makeText(ActEditStu.this, "Empty title", Toast.LENGTH_SHORT).show();
                        } else {
                            Module module = new Module(nMod, 0, edtTitle.getText().toString().trim());
                            mods.add(module);
                            addModRL(module, delModListener);
                            dialog.cancel();
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    private void addModRL(Module module, View.OnClickListener delModListener) {
        RelativeLayout modRL = new RelativeLayout(this);
        modRL.setTag("modRL_" + nMod);
        modRL.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        modRL.setPadding(0, 0, 20, 0);
        CheckBox cbMod = new CheckBox(this);
        RelativeLayout.LayoutParams cbParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cbParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        cbParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        cbMod.setLayoutParams(cbParams);
        cbMod.setTag("doneMod_" + nMod);
        cbMod.setId(View.generateViewId());
        if (module.getState() == Module.DONE) cbMod.setChecked(true);
        TextView tvMod = new TextView(this);
        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        tvParams.addRule(RelativeLayout.END_OF, cbMod.getId());
        tvParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvMod.setLayoutParams(tvParams);
        tvMod.setTextColor(getResources().getColor(android.R.color.black));
        tvMod.setTextSize(17);
        tvMod.setText(module.getTitle());
        ImageButton btnDelMod = new ImageButton(this);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        btnDelMod.setLayoutParams(btnParams);
        btnDelMod.setImageResource(android.R.drawable.ic_delete);
        btnDelMod.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnDelMod.setTag("delMod_" + nMod);
        btnDelMod.setOnClickListener(delModListener);
        modRL.addView(cbMod);
        modRL.addView(tvMod);
        modRL.addView(btnDelMod);
        mod.addView(modRL);
        noMod.setVisibility(View.GONE);
        cbSDone.setVisibility(View.GONE);
        nMod++;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActEditStu.this, ActShowStu.class);
        intent.putExtra("STUID", id);
        intent.putExtra("FROMPROJ", fromProj);
        startActivity(intent);
        finish();
    }

}
