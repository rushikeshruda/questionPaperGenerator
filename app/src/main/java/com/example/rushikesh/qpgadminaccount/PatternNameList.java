package com.example.rushikesh.qpgadminaccount;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Rushikesh on 06/04/2018.
 */

public class PatternNameList extends ArrayAdapter<PatternName> {
    private Activity context;
    private List<PatternName> patternNameList;

    public PatternNameList(Activity context, int list_layout, List<PatternName> patternNameList){
        super(context, R.layout.list_layout,patternNameList);
        this.context=context;
        this.patternNameList = patternNameList;
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

        PatternName patternName = patternNameList.get(position);
        textViewCourseName.setText(patternName.getPatternNameText());
        return listViewItem;
    }

}
