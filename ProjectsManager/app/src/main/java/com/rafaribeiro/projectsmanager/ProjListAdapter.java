package com.rafaribeiro.projectsmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjListAdapter extends ArrayAdapter<Project> {

    private ArrayList<Project> projects;
    private Context mContext;

    public ProjListAdapter(Context context, ArrayList<Project> projects) {
        super(context, R.layout.adapter_proj_list, projects);
        this.projects = projects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.adapter_proj_list, null);
        }

        Project project = getItem(position);

        if (project != null) {
            TextView projListName = (TextView)v.findViewById(R.id.projListName);
            ImageView projListState = (ImageView)v.findViewById(R.id.projListState);
            TextView projListAbstract = (TextView)v.findViewById(R.id.projListAbstract);
            TextView projListID = (TextView)v.findViewById(R.id.projListID);

            if (projListName != null) {
                projListName.setText(project.getNameProj());
            }
            if (projListState != null) {
                switch (project.getState()) {
                    case Project.TO_DO:
                        projListState.setImageResource(R.drawable.idea);
                        break;
                    case Project.DOING:
                        projListState.setImageResource(R.drawable.doing);
                        break;
                    case Project.DONE:
                        projListState.setImageResource(R.drawable.done);
                        break;
                }
            }
            if (projListAbstract != null) {
                projListAbstract.setText(project.getAbstractProj());
            }
            if (projListID != null) {
                projListID.setText(Integer.toString(project.getIdProj()));
            }
        }

        return v;
    }
}
