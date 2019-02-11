package com.example.daveart.vocabularyapp.notification;//package com.example.daveart.fourthround;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.daveart.vocabularyapp.utils.PreferenceUtil;
import com.example.daveart.vocabularyapp.utils.TypeFaceUtil;

/**
 * Created by DaveArt on 7/19/2018.
 */

public class NotificationChannelsClass extends Application {

    public static final String CHANNEL_ID = "channel";
    private String font;
    PreferenceUtil preferenceUtil;

    @Override
    public void onCreate() {

//        preferenceUtil = new PreferenceUtil(this);
//
//        String fontSelected = preferenceUtil.retrieveItemsSelected(preferenceUtil
//                .PREFERENCE_FONT, "Augusta.ttf");
//
//        TypeFaceUtil.overrideFont(this, "SERIF", "fonts/" + fontSelected);
//
//        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
//            setTheme(R.style.darkTheme);
//        }else{
//            setTheme(R.style.AppTheme);
//        }

        super.onCreate();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        TypeFaceUtil.overrideFont(this, "SERIF", "fonts/" + getFont());

        createChannel();

    }

    private void createChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "channel", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("This is the notification");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }
}
