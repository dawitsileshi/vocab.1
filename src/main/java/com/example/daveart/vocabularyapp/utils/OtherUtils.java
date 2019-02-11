package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.example.daveart.vocabularyapp.R;

public class OtherUtils {

    Context context;

    public OtherUtils(Context contxt){
        this.context = contxt;
    }

    public int accessingStyleableColor(int id){
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.ds);
        int backUpColor = context.getResources().getColor(R.color.purple);
        int color = typedArray.getColor(id, backUpColor);
        typedArray.recycle();
        return color;

    }

}
