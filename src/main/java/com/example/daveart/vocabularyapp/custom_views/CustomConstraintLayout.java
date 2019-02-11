package com.example.daveart.vocabularyapp.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CustomConstraintLayout extends ConstraintLayout {
    public CustomConstraintLayout(Context context) {
        super(context);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize, height = heightSize;

        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = getResources().getDisplayMetrics().widthPixels - 20;
            height = heightMeasureSpec;
        }

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();

        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

//        widthMeasureSpec = getResources().getDisplayMetrics().widthPixels - 10;
        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension
                (desiredHeight, heightMeasureSpec));
//        super.onMeasure(450, 250);
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);

        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {

            result = specSize;

        } else {

            result = desiredSize;

            if (specMode == MeasureSpec.AT_MOST) {

                result = Math.min(result, specSize);

            }

        }



        if (result < desiredSize){

            Log.e("ChartView", "The view is too small, the content might get cut");

        }

        return result;
    }

}
