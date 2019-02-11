package com.example.daveart.vocabularyapp.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.activities.DetailActivity;
import com.example.daveart.vocabularyapp.activities.QuizStartPage;
import com.example.daveart.vocabularyapp.services.FloatingWidgetReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.app.NotificationCompat.CATEGORY_REMINDER;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.daveart.vocabularyapp.notification.NotificationChannelsClass.CHANNEL_ID;

public class NotificationScheduler {

    public void startNotification(int hour, int minute, Context context){
        Calendar calendar = Calendar.getInstance();
        Calendar newCalendar = Calendar.getInstance();

        newCalendar.set(Calendar.HOUR_OF_DAY, hour);
        newCalendar.set(Calendar.MINUTE, minute);
        newCalendar.set(Calendar.SECOND, 0);

        if(newCalendar.before(calendar)){
            newCalendar.add(Calendar.DATE, 1);
        }

        stopNotification(context);
        ComponentName componentName = new ComponentName(context, AlarmNotificationReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(componentName, PackageManager
                .COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, AlarmNotificationReceiver.class);
        intent.putExtra("tag", 2);
//        intent.setAction(Intent.ACTION_BOOT_COMPLETED);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, newCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

//        showNotification(context);

    }

    public void showNotification(Context context){

        Intent intent = new Intent(context, DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("tag", "noti");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                .setBigContentTitle("Reminder")
                .bigText("Take a Quiz");

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_auto_renew)
                .setContentTitle("Hey!")
                .setContentText("Did you memorize any words today?")
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setCategory(CATEGORY_REMINDER)
                .addAction(R.drawable.checkmark_48px_light_grey, "Yes", pendingIntent)
                .addAction(R.drawable.delete_48px_light_grey, "No", pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLights(Color.CYAN, 3000, 3000)
                .build();

        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);

        notificationManager1.notify(1, notification);

    }

    public void stopNotification(Context context){

        ComponentName receiver = new ComponentName(context, AlarmNotificationReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, AlarmNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert am != null;
        am.cancel(pendingIntent);
        pendingIntent.cancel();

    }

    public void checkDateOfWords(Context context) {

        ComponentName componentName = new ComponentName(context, FloatingWidgetReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(componentName, PackageManager
                .COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, FloatingWidgetReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        assert alarmManager != null;


//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();
//
//        calendar.set(Calendar.HOUR_OF_DAY, 6);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        if(calendar.before(calendar1)){
//            calendar.add(Calendar.DATE, 1);
//        }
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                10000, pendingIntent);
    }

}
