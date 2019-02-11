package com.example.daveart.vocabularyapp.notification;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by DaveArt on 7/21/2018.
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {

    DataSource dataSource;
    public static final String RESULT_KEY = "result_key";
    String word;
    String meaning;
    String wordType;


    NotificationScheduler notificationScheduler;
    PreferenceUtil preferenceUtil;

    @Override
    public void onReceive(Context context, Intent intent) {

        preferenceUtil = new PreferenceUtil(context);
        notificationScheduler = new NotificationScheduler();

        int hour = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_HOUR, 12);
        int minute = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_MINUTE, 0);
//        int second = 0;
        Log.i("Alarm Notification", "Reached");
//        if(intent.getExtras() != null){
        notificationScheduler.showNotification(context);
//            Log.i("Alarm Notification", "Not null");
//        }
//            notificationScheduler.startNotification(hour, minute, context);
//        if(intent.getAction() != null && intent.getAction().equalsIgnoreCase(Intent
//                .ACTION_BOOT_COMPLETED)){
//            notificationScheduler.startNotification(hour, minute, context);
//            Log.i("Alarm Notification", intent.getAction());
//        }else {
//            Log.i("Alarm Notification Null", String.valueOf(intent.getAction()));
//        }
//        if (intent.getAction() != null && context != null) {
//            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
//                LocalData localData = new LocalData(context);
//                notificationScheduler.startNotification(hour, minute, context);
//                return;
//            }
//        }

//        notificationScheduler.showNotification(context);

//        Calendar calendar = Calendar.getInstance();
//        Calendar newCalendar = Calendar.getInstance();
//
//        newCalendar.set(Calendar.HOUR_OF_DAY, hour);
//        newCalendar.set(Calendar.MINUTE, minute);
//        newCalendar.set(Calendar.SECOND, second);
//
//        if(newCalendar.before(calendar)){
//            newCalendar.add(Calendar.DATE, 1);
//        }
//
        dataSource = new DataSource(context);
        if(dataSource.getAllWords().size() == 0){

            word = "";
            meaning = "No Data Found";
            wordType = "";

        }else {

            ArrayList<String> arrayList = dataSource.randomWord();
            word = arrayList.get(0);
            meaning = arrayList.get(1);
            wordType = arrayList.get(2);

        }
//
//        intent = new Intent(context, QuizStartPage.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.putExtra("tag", "noti");
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
//                .setBigContentTitle("Reminder")
//                .bigText("Take a Quiz");
//
//        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_auto_renew)
//                .setContentTitle("Hey!")
//                .setContentText("Did you memorize any words today?")
//                .setStyle(bigTextStyle)
//                .setPriority(PRIORITY_HIGH)
//                .setCategory(CATEGORY_REMINDER)
//                .addAction(R.mipmap.ic_launcher_auto_renew, "Yes", pendingIntent)
//                .addAction(R.mipmap.ic_launcher_auto_renew, "No", pendingIntent)
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setLights(Color.CYAN, 3000, 3000)
//                .build();
//
//        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);
//
//        notificationManager1.notify(1, notification);

//        updatingHomeScreenWidget(word, meaning, wordType, context);

    }

    private void updatingHomeScreenWidget(String word, String meaning, String wordType, Context
            context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_single_view);
        remoteViews.setTextViewText(R.id.textView_widget_layout_single_word, word);
        remoteViews.setTextViewText(R.id.textView_widget_layout_single_meaning, meaning);
        remoteViews.setTextViewText(R.id.textView_widget_layout_single_wordType, wordType);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, getClass());
        appWidgetManager.updateAppWidget(componentName, remoteViews);

    }
}

//
//        intent = new Intent(context, DetailActivity.class);
//
//        intent.putExtra("word", word);
//        intent.putExtra("meaning", meaning);
//        Log.i("Word in alarm", word + " " + meaning);

//        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle
//                ("Word");
//        messagingStyle.setConversationTitle("Search for a meaning");
//
//        Intent saveIntent = new Intent(context, MeaningParsedDialogFragment.class);
//        PendingIntent savePendingIntent = PendingIntent.getActivity(context, 0, saveIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//

//
//        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//                "tag");
//        wakeLock.acquire();

//        Intent intent1 = new Intent(context, StackedWidgetView.class);
//
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_empty);
//        remoteViews.setTextViewText(R.id.textView_widget_layout_item_word, word);
//        remoteViews.setTextViewText(R.id.textView_widget_layout_item_wordType, wordType);
//        remoteViews.setTextViewText(R.id.textView_widget_layout_item_meaning, meaning);
//        remoteViews.setRemoteAdapter(R.id.stackView_widget_layout, intent1);
//        remoteViews.setEmptyView(R.id.stackView_widget_layout, R.id.textView_widget_layout);
//
//        ComponentName thisWidget = new ComponentName(context, HomeScreenWidget.class);
//
//        AppWidgetManager manager = AppWidgetManager.getInstance(context);
//        manager.updateAppWidget(thisWidget, remoteViews);

//        wakeLock.release();