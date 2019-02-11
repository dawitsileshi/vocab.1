package com.example.daveart.vocabularyapp.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;

public class HomeScreenWidget extends AppWidgetProvider {

    DataSource dataSource;
    PreferenceUtil preferenceUtil;
    ArrayList<String> arrayList;
    SavedWord savedWord;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        initializeObjects(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {


        for(int appWidgetId : appWidgetIds){
            initializeObjects(context);

            if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_IS_SELECTED_WIDGET_CONFIG_STACKED)){

                prepareWidget(context, appWidgetId, R.layout.widget_layout_empty, true, appWidgetManager);

            }else{

                prepareWidget(context, appWidgetId, R.layout.widget_layout_single_view, false,
                        appWidgetManager);

            }
        }
    }

    public void prepareWidget(Context context, int appWidgetId, int layoutId, boolean isStacked,
                              AppWidgetManager appWidgetManager) {
        RemoteViews remoteViews;
        if (isStacked) {
            remoteViews = new RemoteViews(context.getPackageName(), layoutId);
            Intent intent = new Intent(context, StackedWidgetView.class);
            remoteViews.setRemoteAdapter(R.id.stackView_widget_layout, intent);
            remoteViews.setEmptyView(R.id.stackView_widget_layout, R.id.textView_widget_layout);
        } else {
            remoteViews = new RemoteViews(context.getPackageName(), layoutId);
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_word, savedWord.getWord());
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_meaning, savedWord
                    .getMeaning());
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_wordType, savedWord
                    .getWordType());

            Intent intent = new Intent(context, getClass());
            intent.setAction("REFRESH");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id
                    .imageView_widget_layout_single_view_autoRenew, pendingIntent);
        }
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        initializeObjects(context);

        assert intent.getAction() != null;

        if(intent.getAction().equals("REFRESH")){

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_single_view);
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_word, savedWord.getWord());
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_meaning, savedWord.getMeaning());
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_wordType, savedWord.getWordType());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, getClass());
            appWidgetManager.updateAppWidget(componentName, remoteViews);

        }

    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        preferenceUtil = null;
        dataSource = null;
        arrayList = null;

    }

    public void initializeObjects(Context context){
        dataSource = new DataSource(context);
        arrayList = dataSource.randomWord();
        preferenceUtil = new PreferenceUtil(context);
        savedWord = new SavedWord();
        settingData();

    }

    public void settingData(){
        if(arrayList.size() > 0) {
            savedWord.setWord(arrayList.get(0));
            savedWord.setMeaning(arrayList.get(1));
            savedWord.setWordType(arrayList.get(2));
        }else{
            savedWord.setWord("");
            savedWord.setMeaning("No Data found");
            savedWord.setWordType("");
        }
    }
}
