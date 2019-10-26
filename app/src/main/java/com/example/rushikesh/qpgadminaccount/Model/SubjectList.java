package com.example.rushikesh.qpgadminaccount.Model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rushikesh.qpgadminaccount.R;

import java.util.List;

/**
 * Created by Rushikesh on 06/04/2018.
 */

public class SubjectList extends ArrayAdapter<Subject> {
    private Activity context;
    private List<Subject> subjectList;

    public SubjectList(Activity context, int list_layout, List<Subject> subjectList){
        super(context, R.layout.list_layout,subjectList);
        this.context=context;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return myCustomSpinner(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return myCustomSpinner(position, convertView, parent);
    }

    private View myCustomSpinner(int position, @Nullable View myView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listViewItem = inflater.inflate(R.layout.list_layout,parent,false);

        TextView textViewCourseName = listViewItem.findViewById(R.id.textViewCourseName);

        Subject subject = subjectList.get(position);
        textViewCourseName.setText(subject.getSubjectName());
        return listViewItem;
    }

}
