package com.rafaribeiro.projectsmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StuListAdapter extends ArrayAdapter<Study> {

    private ArrayList<Study> studies;
    private Context mContext;

    public StuListAdapter(Context context, ArrayList<Study> studies) {
        super(context, R.layout.adapter_stu_list, studies);
        this.studies = studies;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.adapter_stu_list, null);
        }

        Study study = getItem(position);

        if (study != null) {
            TextView stuListTitle = (TextView)v.findViewById(R.id.stuListTitle);
            ProgressBar stuListProgress = (ProgressBar)v.findViewById(R.id.stuListProgress);
            TextView stuListID = (TextView)v.findViewById(R.id.stuListID);

            if (stuListTitle != null) {
                stuListTitle.setText(study.getNameStu());
            }
            if (stuListProgress != null) {
                stuListProgress.setProgress(study.getProgress(mContext));
            }
            if (stuListID != null) {
                stuListID.setText(Integer.toString(study.getIdStu()));
            }
        }

        return v;
    }

}
