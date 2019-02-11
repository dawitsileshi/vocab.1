package com.example.daveart.vocabularyapp.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.daveart.vocabularyapp.activities.AnotherActivity;
import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.dialog_fragments.MeaningParsedDialogFragment;

public class FloatingViewService extends Service {

    private WindowManager mWindowManager;
    private View mFloatingView;
    MeaningParsedDialogFragment meaningParsedDialogFragment;
    PreferenceUtil preferenceUtil;
    Context context;
    int value;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceUtil = new PreferenceUtil(getApplicationContext());
        meaningParsedDialogFragment = new MeaningParsedDialogFragment();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget1, null);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.CENTER | Gravity.END;
        params.x = 0;
        params.y = 100;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        ImageView closeButtonCollapsed = mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
                preferenceUtil.saveBooleanValue(false, preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE);
            }
        });



        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                MeaningParsedDialogFragment meaningParsedDialogFragment = new
                                        MeaningParsedDialogFragment().newInstance(null, -1, null, false, -1);
                                startActivity(new Intent(getApplicationContext(), AnotherActivity
                                        .class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                // TODO: when this activity is cleared from recent, it also
                                // clears previous activity.
//                                mFloatingView.findViewById(R.id.collapse_view).setVisibility
//                                        (View.GONE);
                                stopSelf();

//                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX - (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });

    }

//    @Override
//    public void onRebind(Intent intent) {
//        super.onRebind(intent);
//        if(getValue() == 1){
//
//        }
//    }

    public boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view)
                .getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }
}
