package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActEditProj extends AppCompatActivity {

    DataBase db = new DataBase(this);

    Button btnBack, btnAdd;
    EditText edtName, edtAbst, edtDesc, func_0;
    LinearLayout func, req;
    RadioGroup radState;
    ImageButton btnAddReq;
    TextView noReq;

    ArrayList<EditText> funcs;
    int nFunc = 0;
    ArrayList<Requirement> reqs;
    int nReq = 0;

    int id;

    private final String SEPARATOR = "/0//1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_proj);

        // Firstly verify if one valid ProjectID was passed via Intent. If not, goes to Project List Activity
        id = getIntent().getIntExtra("PROJID", 0);
        Project project = db.selectProject(id);
        if (project == null) goToProjList();

        // Views
        btnBack = findViewById(R.id.btnBackAddProj);
        btnAdd = findViewById(R.id.btnAddAddProj);
        btnAdd.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_edit, 0);
        edtName = findViewById(R.id.edtNameAddProj);
        edtAbst = findViewById(R.id.edtAbstAddProj);
        edtDesc = findViewById(R.id.edtDescAddProj);
        func = findViewById(R.id.funcAddProj);
        func_0 = findViewById(R.id.func_0);
        func_0.setTag("func_0");
        func_0.setVisibility(View.GONE);
        radState = findViewById(R.id.radState);
        req = findViewById(R.id.reqAddProj);
        btnAddReq = findViewById(R.id.btnAddReqAddProj);
        noReq = findViewById(R.id.noReqAddProj);

        // Funcionalities ArrayList inicialization
        funcs = new ArrayList<>();
        //funcs.add(func_0);
        // Requirements ArrayList inicialization
        reqs = new ArrayList<>();

        TextWatcher funcTextChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0 && start == 0 && s.toString().length() > 0) addFuncEdt(this, "");
                if (before > 0 && s.toString().length() == 0) {
                    EditText edtToRem = null;
                    for (EditText edt: funcs) {
                        if (edt.getText().hashCode() == s.hashCode()) {
                            func.removeView(edt);
                            edtToRem = edt;
                        }
                    }
                    if (edtToRem != null) funcs.remove(edtToRem);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        // On delete requirement button click listener
        final View.OnClickListener delReqListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                int idReq;
                try {
                    idReq = Integer.parseInt(tag.split("_")[1]);
                } catch (Exception e) {
                    idReq = -1;
                    Log.e("error", e.getMessage());
                }
                if (idReq != -1) {
                    String tagRL = "reqRL_" + idReq;
                    RelativeLayout rl = (RelativeLayout)v.getRootView().findViewWithTag(tagRL);
                    req.removeView(rl);
                    Requirement requirement = new Requirement(idReq);
                    reqs.remove(requirement);
                    if (reqs.size() == 0) noReq.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(ActEditProj.this, "Error deleting requirement", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Values atribution to Views
        edtName.setText(project.getNameProj());
        edtAbst.setText(project.getAbstractProj());
        edtDesc.setText(project.getDescProj());
        String[] projFuncs = project.getFunctionalities();
        for (String f: projFuncs) {
            addFuncEdt(funcTextChanged, f);
        }
        addFuncEdt(funcTextChanged, "");
        switch (project.getState()) {
            case State.TO_DO:
                radState.check(R.id.radState1);
                break;
            case State.DOING:
                radState.check(R.id.radState2);
                break;
            case State.DONE:
                radState.check(R.id.radState3);
                break;
        }
        final ArrayList<Requirement> projReqs = db.selectReqsOfProject(id);
        for (Requirement r: projReqs) {
            r.setIdReq(nReq);
            reqs.add(r);
            addReqRL(r, delReqListener);
        }

        // Back button clicked listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goBack(); }
        });

        // Edit button clicked listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().trim().isEmpty() ||
                        edtAbst.getText().toString().trim().isEmpty() ||
                        edtDesc.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ActEditProj.this, R.string.add_proj_error, Toast.LENGTH_SHORT).show();
                } else {
                    String sfuncs = "";
                    if (funcs.size() == 1) {
                        sfuncs = funcs.get(0).getText().toString().trim();
                    } else if (funcs.size() > 1) {
                        sfuncs = funcs.get(0).getText().toString().trim();
                        for (int i = 1; i < funcs.size(); i++) {
                            EditText edt = funcs.get(i);
                            sfuncs += SEPARATOR + edt.getText().toString().trim();
                        }
                    }
                    int state = 0;
                    switch (radState.getCheckedRadioButtonId()) {
                        case R.id.radState1:
                            state = State.TO_DO;
                            break;
                        case R.id.radState2:
                            state = State.DOING;
                            break;
                        case R.id.radState3:
                            state = State.DONE;
                            break;
                    }
                    Project upProject = new Project(id, edtName.getText().toString().trim(),
                            edtAbst.getText().toString().trim(), edtDesc.getText().toString().trim(),
                            sfuncs, state, reqs.size());
                    db.updateProject(upProject);
                    db.deleteTableReq(id);
                    for (Requirement r: reqs) {
                        r.setProjId(id);
                        db.insertReq(r);
                    }
                    goBack();
                }
            }
        });

        // On AddReq Button clicked
        btnAddReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ActEditProj.this);
                dialog.setContentView(R.layout.dialog_add_req);
                dialog.setCancelable(true);

                Button btnChooseStudy = (Button)dialog.findViewById(R.id.btnChooseStudyARD);
                final EditText edtReq = (EditText)dialog.findViewById(R.id.edtReqARD);
                Button btnAddReq = (Button)dialog.findViewById(R.id.btnAddReqARD);
                Button btnCancel = (Button)dialog.findViewById(R.id.btnCancelARD);
                btnChooseStudy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Study> studies = db.selectAllStudies();
                        if (studies.size() == 0) {
                            Toast.makeText(ActEditProj.this, "There are no studies registered", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActEditProj.this);
                            builder.setTitle(R.string.choose_study);
                            String[] items = new String[studies.size()];
                            for (int i = 0; i < studies.size(); i++) {
                                Study s = studies.get(i);
                                items[i] = s.getNameStu();
                            }
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Study selected = db.selectStudyByPosition(which);
                                    Requirement req = new Requirement(nReq, 0, selected.getIdStu());
                                    reqs.add(req);
                                    addReqRL(req, delReqListener);
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog2 = builder.create();
                            dialog2.setCancelable(true);
                            dialog.cancel();
                            dialog2.show();
                        }
                    }
                });
                btnAddReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtReq.getText().toString().trim().equals("")) {
                            Toast.makeText(ActEditProj.this, "Empty requirement", Toast.LENGTH_SHORT).show();
                        } else {
                            Requirement req = new Requirement(nReq, 0, edtReq.getText().toString().trim());
                            reqs.add(req);
                            addReqRL(req, delReqListener);
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

    private void addFuncEdt(TextWatcher textWatcher, String text) {
        nFunc++;
        EditText newFunc = new EditText(this);
        newFunc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newFunc.setHint(R.string.functionality);
        newFunc.setTag("func_" + nFunc);
        newFunc.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        newFunc.setSingleLine(true);
        newFunc.setText(text);
        newFunc.addTextChangedListener(textWatcher);
        func.addView(newFunc);
        funcs.add(newFunc);
    }

    private void addReqRL(Requirement requirement, View.OnClickListener delReqListener) {
        RelativeLayout reqRL = new RelativeLayout(this);
        reqRL.setTag("reqRL_" + nReq);
        reqRL.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        TextView tvReq = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        tvReq.setId(View.generateViewId());
        tvReq.setLayoutParams(params);
        tvReq.setTextColor(getResources().getColor(android.R.color.black));
        tvReq.setTextSize(18);
        ImageButton btnDelReq = new ImageButton(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.END_OF, tvReq.getId());
        params2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        params2.setMarginStart(3);
        btnDelReq.setLayoutParams(params2);
        btnDelReq.setTag("delReq_" + nReq);
        btnDelReq.setImageResource(android.R.drawable.ic_delete);
        btnDelReq.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        btnDelReq.setOnClickListener(delReqListener);
        if (requirement.getStudyId() == 0) {
            tvReq.setText(requirement.getReq());
        } else {
            Study study = db.selectStudy(requirement.getStudyId());
            tvReq.setText(study.getNameStu());
        }
        reqRL.addView(tvReq);
        reqRL.addView(btnDelReq);
        req.addView(reqRL);
        noReq.setVisibility(View.GONE);
        nReq++;
    }

    private void goToProjList() {
        Intent intent = new Intent(ActEditProj.this, ActProj.class);
        startActivity(intent);
        finish();
    }
    private void goBack() {
        Intent intent = new Intent(ActEditProj.this, ActShowProj.class);
        intent.putExtra("PROJID", id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
