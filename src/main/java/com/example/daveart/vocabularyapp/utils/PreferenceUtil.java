package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceUtil {

    private final String PREFERENCE_NAME = "Setting";
    public final String PREFERENCE_NIGHTMODE_STATE = "NightModeState";
    public final String PREFERENCE_NOTIFICATION_STATE = "NotificationState";
    public final String PREFERENCE_FLOATINGWIDGET_STATE = "FloatingWidgetState";
    public final String PREFERENCE_PREVIOUS_NOTIFICATION_STATE = "PreviousNotificationState";
    public final String PREFERENCE_INTERVAL = "Interval";
    public final String PREFERENCE_INTERVAL_POSITION = "SelectedIntervalPosition";
    public final String PREFERENCE_PREVIOUS_INTERVAL_POSITION = "PreviousSelectedIntervalPosition";
    public final String PREFERENCE_FONT = "Font";
    public final String PREFERENCE_FONT_POSITION = "SelectedFontPosition";
    public final String PREFERENCE_SORT = "Sort";
    public final String PREFERENCE_SORT_POSITION = "SelectedSortPosition";
    public final String PREFERENCE_IS_SELECTED_WIDGET_CONFIG_STACKED = "SelectedWidgetConfig";
    public final String PREFERENCE_MINUTE = "SavedMinute";
    public final String PREFERENCE_HOUR = "SavedHour";
    public final String PREFERENCE_DAY = "SelectedDay";
    public final String PREFERENCE_DAY_POSITION = "SelectedDayPosition";
    public final String PREFERENCE_DAYS_LEFT = "DaysLeft";

    private Context context;

    public PreferenceUtil(Context context){
        this.context = context;
    }

    private SharedPreferences.Editor editor(){
        return context.getSharedPreferences(PREFERENCE_NAME, 0).edit();
    }

    private SharedPreferences preferences(int mode){ // this is for retrieving info
        // from sharedPreference
        return context.getSharedPreferences(PREFERENCE_NAME, mode);
    }

    public void saveBooleanValue(boolean b, String booleanName) {

        SharedPreferences.Editor editor = editor();
        editor.putBoolean(booleanName, b);
        editor.apply();

    }

    public void saveSpinnerValues(Object itemSelected, int itemSelectedPosition, String
            preferenceItemPosition, String preferenceItemName){

        SharedPreferences.Editor editor = editor();
        editor.putInt(preferenceItemPosition, itemSelectedPosition);
        if(itemSelected instanceof String) {
            editor.putString(preferenceItemName, (String) itemSelected);
        } else {
            editor.putInt(preferenceItemName, (Integer) itemSelected);
        }
        editor.apply();

    }

    public void saveInteger(int integer, String nameOfInteger){
        SharedPreferences.Editor editor = editor();
        editor.putInt(nameOfInteger, integer);
        editor.apply();
    }

    public void saveInterval(String interval){
        int extractedInteger = Integer.parseInt(interval.replaceAll("[^0-9]", ""));
        SharedPreferences.Editor editor = editor();
        editor.putString(PREFERENCE_PREVIOUS_INTERVAL_POSITION, String.valueOf(extractedInteger));
        editor.apply();
    }

    public boolean retrieveBooleanValue(String name){
        return preferences(MODE_PRIVATE).getBoolean(name, false);
    }

    public int retrieveInteger(String name, int defaultValue){
        return preferences(MODE_PRIVATE).getInt(name, defaultValue);
    }



    public String retrieveItemsSelected(String name, String backUp){
        return preferences(MODE_PRIVATE).getString(name, backUp);
    }

    public void delete(){
        SharedPreferences.Editor editor = editor();
        editor.remove(PREFERENCE_PREVIOUS_INTERVAL_POSITION);
        editor.commit();
    }


}
