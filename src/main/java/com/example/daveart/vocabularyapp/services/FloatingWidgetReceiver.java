package com.example.daveart.vocabularyapp.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.activities.MainActivity;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.app.NotificationCompat.CATEGORY_REMINDER;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.daveart.vocabularyapp.notification.NotificationChannelsClass.CHANNEL_ID;

public class FloatingWidgetReceiver extends BroadcastReceiver {

    String format = "yyyy-MM-dd hh:mm:ss";
    int counter = 0;
    private Context context;
    private long[] ids;
    public static final String TAG = "IDs";
    PreferenceUtil preferenceUtil;
    int numberOfDays, totalDays;
    private boolean showNotification = false;
    @Override
    public void onReceive(Context context, Intent intent) {
//        intent = new Intent(context, FloatingViewService.class);
//        context.startService(intent);

        this.context = context;
        preferenceUtil = new PreferenceUtil(context);
        numberOfDays = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_DAY, -1);
        totalDays = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_DAYS_LEFT, -1);



//        if (intent.getAction() != null) {
        if ("CalculatingDays".equals(intent.getAction())) {
            Toast.makeText(context, "Here", Toast.LENGTH_SHORT).show();
            DataSource dataSource = new DataSource(context);
                ArrayList<Object> objects = dataSource.savedWords(ItemTables.TABLE_NAME, 1);
                ids = new long[objects.size()];

            if(totalDays == -1) {
                int maxDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + numberOfDays;
                preferenceUtil.saveInteger(maxDay, preferenceUtil.PREFERENCE_DAYS_LEFT);
            }
                for (int i = objects.size() - 1 ; i >= 0 ; i--) {

                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();
                    SavedWord savedWord = (SavedWord) objects.get(i);
                    String timestamp = savedWord.getTimestamp();

                    Date date;
                    try {
                        date = new SimpleDateFormat(format, getCurrentLocale()).parse(timestamp);
                        calendar1.setTime(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(calendar1.before(calendar)) {
                        calendar.add(Calendar.DAY_OF_YEAR, -calendar1.get(Calendar
                                .DAY_OF_YEAR));

                        int day = calendar.get(Calendar.DAY_OF_YEAR);
                        int days = calendar1.get(Calendar.DAY_OF_YEAR);
//                        calendar.add(day, days);
                        Log.i("Days", String.valueOf(days) + " and " + String.valueOf(day));
                        Log.i("Day", String.valueOf(calendar.get(Calendar.DAY_OF_YEAR)));

                        int dayLeft = totalDays - day;
                        if(dayLeft <= 2) {
                            showNotification = true;
                            ids[counter] = savedWord.getId();
                            counter++;
                        }
                    } else {
                        Log.i("days", String.valueOf(calendar1.get(Calendar.DAY_OF_YEAR)));
                    }

                }


            }else {
                Toast.makeText(context, "intent not found", Toast.LENGTH_SHORT).show();
            }
//        }else {
//            Toast.makeText(context, "intent null", Toast.LENGTH_SHORT).show();
//
//        }

        if(ids.length != 0) {
            showNotification();
        }
    }

    private void showNotification() {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction("DeleteTheWords");
        intent.putExtra(TAG, ids);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent
                .FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle().bigText
                ("There are " + String.valueOf(counter) + "words that have been saved " +
                        String.valueOf(numberOfDays) + " days ago, delete the words?");
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_auto_renew)
                .setContentTitle("Hey!")
                .setContentText(String.valueOf(ids.length) + " words to be deleted.")
                .setStyle(bigTextStyle)
                .setPriority(PRIORITY_HIGH)
                .setCategory(CATEGORY_REMINDER)
                .addAction(R.mipmap.ic_launcher_auto_renew, "Yes", pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLights(Color.CYAN, 3000, 3000)
                .build();

        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(context);

        notificationManager1.notify(1, notification);

    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }
}
