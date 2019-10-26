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

public class LevelList extends ArrayAdapter<Level> {
    private Activity context;
    private List<Level> levelList;

    public LevelList(Activity context, int list_layout, List<Level> levelList){
        super(context, R.layout.list_layout,levelList);
        this.context=context;
        this.levelList = levelList;
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

        Level level = levelList.get(position);
        textViewCourseName.setText(level.getLevelName());
        return listViewItem;
    }

}
