package com.example.daveart.vocabularyapp.view_holders;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.MeaningParsed;

import java.util.ArrayList;

public class MeaningParsedViewHolder extends BaseViewHolder implements View.OnClickListener {

    private TextView textView_meaningParsed_wordType, textView_meaningParsed_meaning,
            textView_meaningParsed_example;

    private ConstraintLayout constraintLayout_meaningParsed;
    private AppCompatRadioButton radioButton_meaningParsed;

    private DataSource dataSource;
    ArrayList<Object> arrayList;
    Context context;
    private int lastCheckedPosition;
    private RecyclerViewAdapter recyclerViewAdapter;


    @Override
    public void setData(final RecyclerViewAdapter recyclerViewAdapter, Object object, final int
            position, final int lastCheckedPosition) {

        this.lastCheckedPosition = lastCheckedPosition;
        final MeaningParsed currentMeaningParsed = (MeaningParsed) object;
        this.recyclerViewAdapter = recyclerViewAdapter;

        String example = "Example: " + currentMeaningParsed.getExample();
        getTextView_meaningParsed_wordType().setText(currentMeaningParsed.getWord_type());
        getTextView_meaningParsed_meaning().setText(currentMeaningParsed.getMeaning());
//        Log.i("New Tag ", String.valueOf(getTag()));
        getTextView_meaningParsed_example().setText(example);

        getRadioButton_meaningParsed().setChecked(position == lastCheckedPosition);
        Log.i("Position", String.valueOf(lastCheckedPosition));
//        Log.i("Position " + String.valueOf(position), String.valueOf
//                (getRadioButton_meaningParsed().isChecked()));

        getConstraintLayout_meaningParsed().setOnClickListener(this);
//        getConstraintLayout_meaningParsed().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                recyclerViewAdapter.notifyDataSetChanged();
//            }
//        });
    }

    public MeaningParsedViewHolder(View itemView) {
        super(itemView);

        textView_meaningParsed_example = itemView.findViewById(R.id
                .textView_meaningParsed_example);
        textView_meaningParsed_meaning = itemView.findViewById(R.id
                .textView_meaningParsed_meaning);
        textView_meaningParsed_wordType = itemView.findViewById(R.id
                .textView_meaningParsed_wordType);
        constraintLayout_meaningParsed = itemView.findViewById(R.id.constraint_meaning_parsed);

        radioButton_meaningParsed = itemView.findViewById(R.id.radioButton_meaningParsed);
//        lastCheckedPosition = -1;

//        dataSource = new DataSource(this.getRootView().getContext());
//        arrayList = dataSource.


    }

    public ConstraintLayout getConstraintLayout_meaningParsed() {
        return constraintLayout_meaningParsed;
    }

    public AppCompatRadioButton getRadioButton_meaningParsed() {
        return radioButton_meaningParsed;
    }

    public TextView getTextView_meaningParsed_example() {
        return textView_meaningParsed_example;
    }

    public TextView getTextView_meaningParsed_meaning() {
        return textView_meaningParsed_meaning;
    }

    public TextView getTextView_meaningParsed_wordType() {
        return textView_meaningParsed_wordType;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        getRadioButton_meaningParsed().setChecked(true);

        lastCheckedPosition = getAdapterPosition();
        recyclerViewAdapter.notifyItemChanged(getAdapterPosition());
    }
}
