package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationsUtil {

    Context context;

    public AnimationsUtil(Context context){
        this.context = context;
    }

    public Animation customAnimation(int resource){
        return AnimationUtils.loadAnimation(context, resource);
    }
}
