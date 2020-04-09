package com.rafaribeiro.projectsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActAddProj extends AppCompatActivity {

    Button btnBackAddProj, btnAddAddProj;
    EditText edtName, edtAbst, edtDesc, func_0;
    LinearLayout func, req;
    RadioGroup radState;
    ImageButton btnAddReq;
    TextView noReq;

    ArrayList<EditText> funcs;
    int nFunc = 0;
    ArrayList<Requirement> reqs;
    int nReq = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_add_proj);

        // Views
        btnBackAddProj = findViewById(R.id.btnBackAddProj);
        btnAddAddProj = findViewById(R.id.btnAddAddProj);
        edtName = findViewById(R.id.edtNameAddProj);
        edtAbst = findViewById(R.id.edtAbstAddProj);
        edtDesc = findViewById(R.id.edtDescAddProj);
        func = findViewById(R.id.funcAddProj);
        func_0 = findViewById(R.id.func_0);
        func_0.setTag("func_0");
        radState = findViewById(R.id.radState);
        req = findViewById(R.id.reqAddProj);
        btnAddReq = findViewById(R.id.btnAddReqAddProj);
        noReq = findViewById(R.id.noReqAddProj);

        // Funcionalities ArrayList inicialization
        funcs = new ArrayList<>();
        funcs.add(func_0);
        // Requirements ArrayList inicialization
        reqs = new ArrayList<>();

        // On Back button clicked
        btnBackAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActAddProj.super.onBackPressed();
            }
        });

        // On Add button clicked
        btnAddAddProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Verification and Add code
                 */
            }
        });

        TextWatcher funcTextChanged = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 0 && start == 0 && s.toString().length() > 0) addFuncEdt(this);
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
        for (EditText edt: funcs) {
            edt.addTextChangedListener(funcTextChanged);
        }

        // On AddReq Button clicked
        btnAddReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ActAddProj.this);
                dialog.setContentView(R.layout.dialog_add_req);
                dialog.setCancelable(true);

                Button btnChooseStudy = (Button)dialog.findViewById(R.id.btnChooseStudyARD);
                final EditText edtReq = (EditText)dialog.findViewById(R.id.edtReqARD);
                Button btnAddReq = (Button)dialog.findViewById(R.id.btnAddReqARD);
                Button btnCancel = (Button)dialog.findViewById(R.id.btnCancelARD);
                btnChooseStudy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                btnAddReq.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtReq.getText().toString().trim().equals("")) {
                            Toast.makeText(ActAddProj.this, "Empty requirement", Toast.LENGTH_SHORT).show();
                        } else {
                            Requirement req = new Requirement(edtReq.getText().toString().trim());
                            reqs.add(req);
                            addReqRL(req);
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

    private void addFuncEdt(TextWatcher textWatcher) {
        nFunc++;
        EditText newFunc = new EditText(this);
        newFunc.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        newFunc.setHint(R.string.functionality);
        newFunc.setTag("func_" + nFunc);
        newFunc.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        newFunc.setSingleLine(true);
        newFunc.addTextChangedListener(textWatcher);
        func.addView(newFunc);
        funcs.add(newFunc);
    }

    private void addReqRL(Requirement requirement) {
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
        if (requirement.getStudyId() == 0) {
            tvReq.setText(requirement.getReq());
        } else {
            /*
            Study selected
             */
        }
        reqRL.addView(tvReq);
        reqRL.addView(btnDelReq);
        req.addView(reqRL);
        noReq.setVisibility(View.GONE);
        nReq++;
    }

}
