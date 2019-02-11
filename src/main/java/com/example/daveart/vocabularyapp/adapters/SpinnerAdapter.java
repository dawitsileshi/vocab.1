package com.example.daveart.vocabularyapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;

import java.util.ArrayList;

/**
 * Created by DaveArt on 7/29/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<Object> {

    private ArrayList<Integer> images;

    public SpinnerAdapter(@NonNull Context context, ArrayList<Object> spinnerItemArray,
                          ArrayList<Integer> images) {
        super(context, 0, spinnerItemArray);
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (checkMethodToExecute() == 1) {

            return fontLayout(position, convertView, parent);

        } else if (checkMethodToExecute() == 2) {

            return intervalLayout(position, convertView, parent);

        } else {

            return sortLayout(position, convertView, parent);

        }
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (checkMethodToExecute() == 1) {

            return fontLayout(position, convertView, parent);

        } else if (checkMethodToExecute() == 2) {

            return intervalLayout(position, convertView, parent);

        } else {

            return sortLayout(position, convertView, parent);
        }
    }

    private View fontLayout(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.font_item, parent,
                    false);
        }

        String singleFont = (String) getItem(position);

        TextView font = convertView.findViewById(R.id.font);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + singleFont);

        font.setTypeface(typeface);

        return convertView;
    }


    private View intervalLayout(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.font_item, parent,
                    false);
        }

        int SingleInterval = (Integer) getItem(position);
        TextView interval = convertView.findViewById(R.id.font);
        interval.setText(String.valueOf(SingleInterval));
        interval.setTextSize(14);

        return convertView;
    }

    private View sortLayout(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sort_spinner_items, parent,
                    false);
        }

        ImageView sortIcon = convertView.findViewById(R.id.imageView_sort_spinner);
        TextView sortType = convertView.findViewById(R.id.textView_sort_spinner);
        String item = (String) getItem(position);
        sortType.setText(item);
        sortIcon.setImageResource(images.get(position));

        return convertView;

    }

    private int checkMethodToExecute() {

        if (getItem(0) instanceof String) {
            String item = (String) getItem(0);
            assert item != null;
            if (item.toLowerCase().contains(".ttf") || item.toLowerCase().contains(".otf")) {
                return 1;
//        }else if(getItem(0).toLowerCase().contains("min")){
//            return 2;
            }

        } else if (getItem(0) instanceof Integer) {
                return 2;

        }
        return 0;
    }
}
