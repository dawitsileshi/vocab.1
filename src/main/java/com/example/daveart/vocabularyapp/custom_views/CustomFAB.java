package com.example.daveart.vocabularyapp.custom_views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.daveart.vocabularyapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CustomFAB extends FloatingActionButton {
    public CustomFAB(Context context) {
        super(context);
    }

    public CustomFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(int resId) {
//        this.animate().rotationBy(90f).setDuration(200);
//        if(resId == R.drawable.ic_settings_black_24dp) {
//            this.animate().alpha(0f).setDuration(300);
//        } else {
//            this.animate().alpha(1f).setDuration(300);
//        }
        super.setImageResource(resId);
    }
}
