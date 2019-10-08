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

/**
 * Created by Rushikesh on 06/04/2018.
 */

public class QuestionList extends ArrayAdapter<Question> {
    private Activity context;
    private List<Question> questionList;

    public QuestionList(Activity context, int list_layout, List<Question> questionList){
        super(context, R.layout.list_layout,questionList);
        this.context=context;
        this.questionList = questionList;
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

        Question question = questionList.get(position);
        textViewCourseName.setText(question.getQuestionText());
        return listViewItem;
    }

}
