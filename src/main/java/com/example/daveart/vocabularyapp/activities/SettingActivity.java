package com.example.daveart.vocabularyapp.activities;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.transition.TransitionManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.SpinnerAdapter;
import com.example.daveart.vocabularyapp.dialog_fragments.TimePickerDialogFragment;
import com.example.daveart.vocabularyapp.notification.NotificationScheduler;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class SettingActivity extends AppCompatActivity implements AdapterView
        .OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        TimePickerDialog.OnTimeSetListener {

    ConstraintLayout nightModeConstraint, notificationConstraint, intervalConstraint, fontConstraint, floatingWidgetConstraint,
            sortConstraint, wholeConstraint;
    ConstraintSet constraintSetOld = new ConstraintSet();
    ConstraintSet constraintSetNew = new ConstraintSet();
    boolean altLayout;

    SwitchCompat notificationSwitch, nightModeSwitch, floatingWidgetSwitch;
    AppCompatSpinner fontSpinner, sortSpinner, daysSpinner;
    ImageView notificationImage, nightModeImage;
    TextView intervalTextView;
    Toolbar toolbar;

    boolean notificationState, nightModeState, floatingWidgetState;
    String itemSelected, fontMain, sortMethod;
    int selectedFontPosition, selectedDayPosition, selectedDay;

    ArrayList<Object> spinnerItemArray;

    SpinnerAdapter spinnerAdapter;
    PreferenceUtil preferenceUtil;

    NotificationScheduler notificationScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferenceUtil = new PreferenceUtil(this);
        nightModeState = preferenceUtil.retrieveBooleanValue(preferenceUtil
                .PREFERENCE_NIGHTMODE_STATE);

        if(nightModeState){
            setTheme(R.style.darkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        notificationScheduler = new NotificationScheduler();

        initializingEverything();
        assigningSpinnerItems();
        retrieveValues();

        constraintSetOld.clone(wholeConstraint);
        constraintSetNew.clone(this, R.layout.activity_settings_alt);

        fontSpinner.setSelection(0, true);
        fontSpinner.setSelected(false);
        fontSpinner.setOnItemSelectedListener(this);
//        intervalSpinner.setOnItemSelectedListener(this);
        sortSpinner.setOnItemSelectedListener(this);

        daysSpinner.setSelection(0, true);
        daysSpinner.setSelected(false);
        daysSpinner.setOnItemSelectedListener(this);

        retrieveSelected();

        // handling event of switches
        notificationSwitch.setOnCheckedChangeListener(this);
        nightModeSwitch.setOnCheckedChangeListener(this);
        floatingWidgetSwitch.setOnCheckedChangeListener(this);

        if(!notificationSwitch.isChecked()){

            intervalConstraint.animate().alpha(0);
            intervalConstraint.setVisibility(View.GONE);
        }

    }

    public void initializingEverything(){

        // switches
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        floatingWidgetSwitch = findViewById(R.id.floatingWidgetSwitch);

        wholeConstraint = findViewById(R.id.settingFragment_container);

        // spinners
//        intervalSpinner = findViewById(R.id.intervalSpinner);
        fontSpinner = findViewById(R.id.fontSpinner);
        sortSpinner = findViewById(R.id.sortSpinner);
        daysSpinner = findViewById(R.id.selfDeletion_spinner);

        // images
        nightModeImage = findViewById(R.id.nightMode_image);
        notificationImage = findViewById(R.id.notification_image);

        // cardViews
        nightModeConstraint = findViewById(R.id.nightModeConstraint);
        notificationConstraint = findViewById(R.id.notificationConstraint);
        intervalConstraint = findViewById(R.id.intervalConstraint);
        fontConstraint = findViewById(R.id.fontConstraint);
        floatingWidgetConstraint = findViewById(R.id.floatingWidgetConstraint);
        sortConstraint = findViewById(R.id.sortConstraint);

        // textViews
        intervalTextView = findViewById(R.id.interval);

        toolbar = findViewById(R.id.toolBarID);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        intervalConstraint.setOnClickListener(this);

    }

    private void assigningSpinnerItems() {

        String[] array = new String[]{"a_sensible_armadillo.ttf",
                                    "Abyssinica.ttf",
                                    "animeace.ttf",
                                    "attack_of_the_cucumbers.ttf",
                                    "CATHSGBR.TTF",
                                    "Pecita.otf",
                                    "baveuse_3d.ttf",
                                    "rm_typerighter.ttf"};
        spinnerItemArray = new ArrayList<Object>(Arrays.asList(array));

        ArrayList<Object> sortSpinners = new ArrayList<>();
        sortSpinners.add( "Alphabetically");
        sortSpinners.add("Recently Added");

        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_sort_by_alpha_black_24dp);
        images.add(R.drawable.ic_date_range_black_24dp);

        ArrayList<Object> daysList = new ArrayList<>();
        daysList.add(5);
        daysList.add(6);
        daysList.add(7);
        daysList.add(8);
        daysList.add(9);
        daysList.add(10);
//        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, spinnerItemArray, null);
//        fontSpinner.setAdapter(spinnerAdapter);

//        SpinnerAdapter spinnerAdapter1 = new SpinnerAdapter(this, )
//        settingAdapter(intervalItems, intervalSpinner, null);
        settingAdapter(spinnerItemArray, fontSpinner, null);
        settingAdapter(sortSpinners, sortSpinner, images);
        settingAdapter(daysList, daysSpinner, null);

    }

    public void settingAdapter(ArrayList<Object> arrayList, AppCompatSpinner view,
                               ArrayList<Integer> images){
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, arrayList, images);
        view.setAdapter(spinnerAdapter);
    }

    public void retrieveValues(){

        notificationState = preferenceUtil.retrieveBooleanValue
                (preferenceUtil.PREFERENCE_NOTIFICATION_STATE);
        floatingWidgetState = preferenceUtil.retrieveBooleanValue(preferenceUtil
                .PREFERENCE_FLOATINGWIDGET_STATE);

        if(notificationState){
            notificationSwitch.setChecked(true);
            notificationImage.setImageResource(R.drawable.ic_notifications_active);

        }else{
            notificationSwitch.setChecked(false);
            notificationImage.setImageResource(R.drawable.ic_notifications);
        }
        if(nightModeState){
            nightModeSwitch.setChecked(true);
        }else{
            nightModeSwitch.setChecked(false);
        }

        if(floatingWidgetState){
            floatingWidgetSwitch.setChecked(true);
        }else{
            floatingWidgetSwitch.setChecked(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
//            case R.id.intervalSpinner:
//
//                int selectedIntervalPosition = adapterView.getSelectedItemPosition();
//                itemSelected = (String) adapterView.getItemAtPosition(i);
//                preferenceUtil.saveSpinnerValues(itemSelected, selectedIntervalPosition,
//                        preferenceUtil.PREFERENCE_INTERVAL_POSITION, preferenceUtil
//                                .PREFERENCE_INTERVAL);
//
//                break;

            case R.id.fontSpinner:

                fontMain = (String) adapterView.getSelectedItem();
                selectedFontPosition = adapterView.getSelectedItemPosition();
                preferenceUtil.saveSpinnerValues(fontMain, selectedFontPosition, preferenceUtil
                                .PREFERENCE_FONT_POSITION, preferenceUtil.PREFERENCE_FONT);

                break;

            case R.id.sortSpinner:

                sortMethod = (String) adapterView.getSelectedItem();
                int selectedSortPosition = adapterView.getSelectedItemPosition();
                preferenceUtil.saveSpinnerValues(sortMethod, selectedSortPosition, preferenceUtil
                        .PREFERENCE_SORT_POSITION, preferenceUtil.PREFERENCE_SORT);

                break;
            case R.id.selfDeletion_spinner:
                selectedDay = (Integer) adapterView.getSelectedItem();
                selectedDayPosition = adapterView.getSelectedItemPosition();
                preferenceUtil.saveSpinnerValues(selectedDay, selectedDayPosition, preferenceUtil
                        .PREFERENCE_DAY_POSITION, preferenceUtil.PREFERENCE_DAY);
                notificationScheduler.checkDateOfWords(this);
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        switch (adapterView.getId()){

//            case R.id.intervalSpinner:
//
//                itemSelected = (String) adapterView.getSelectedItem();
//                preferenceUtil.saveSpinnerValues((String) adapterView.getSelectedItem(), adapterView
//                    .getSelectedItemPosition(), preferenceUtil.PREFERENCE_INTERVAL_POSITION,
//                        preferenceUtil.PREFERENCE_INTERVAL);
//                break;

            case R.id.fontSpinner:

                fontMain = (String) adapterView.getSelectedItem();
                preferenceUtil.saveSpinnerValues(fontMain, adapterView.getSelectedItemPosition(),
                        preferenceUtil.PREFERENCE_FONT_POSITION, preferenceUtil.PREFERENCE_FONT);
                break;

            case R.id.sortSpinner:

                sortMethod = (String) adapterView.getSelectedItem();
                preferenceUtil.saveSpinnerValues(sortMethod, adapterView.getSelectedItemPosition
                        (), preferenceUtil.PREFERENCE_SORT_POSITION, preferenceUtil.PREFERENCE_SORT);
                break;

            case R.id.selfDeletion_spinner:

                selectedDay = (Integer) adapterView.getSelectedItem();
                selectedDayPosition = adapterView.getSelectedItemPosition();
                preferenceUtil.saveSpinnerValues(selectedDay, selectedDayPosition, preferenceUtil
                        .PREFERENCE_DAY_POSITION, preferenceUtil.PREFERENCE_DAY);
                break;
        }

    }

    public void retrieveSelected(){

        int retrievedFontPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_FONT_POSITION, -1);
        int retrievedIntervalPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_INTERVAL_POSITION, -1);
        int retrievedSortPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_SORT_POSITION, -1);
        int retrievedDayPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_DAY_POSITION, -1);

        if(retrievedFontPosition != -1){
            fontSpinner.setSelection(retrievedFontPosition);
        }
//        if(retrievedIntervalPosition != -1){
//            intervalSpinner.setSelection(retrievedIntervalPosition);
//        }
        if(retrievedSortPosition != -1){
            sortSpinner.setSelection(retrievedSortPosition);
        }

        if(retrievedDayPosition != -1) {
            daysSpinner.setSelection(retrievedDayPosition);
        }

        int hour = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_HOUR, 12);
        int minute = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_MINUTE, 00);

        intervalTextView.setText(getFormattedTime(hour, minute));

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()){

            case R.id.notificationSwitch:

                if(isChecked){

                    notificationState = true;
                    TransitionManager.beginDelayedTransition(wholeConstraint);

                    constraintSetOld.applyTo(wholeConstraint);
                    altLayout = false;
//                    if(!altLayout) {
//                    } else {
//                    }
//                    itemSelected = (String) intervalSpinner.getSelectedItem();
//                    Log.i("Selected interval", itemSelected);
//                    notificationSwitch(View.VISIBLE, true,
//                            R.drawable.ic_notifications_active, 1000, 1);

                }else{

                    constraintSetNew.applyTo(wholeConstraint);
                    altLayout = true;
                    notificationState = false;
                    itemSelected = null;
//                    notificationSwitch(View.GONE, false, R.drawable.ic_notifications, 0, 0);

                }

                break;

            case R.id.nightModeSwitch:

                if(isChecked){

                    nightModeState = true;
                    preferenceUtil.saveBooleanValue(true, preferenceUtil.PREFERENCE_NIGHTMODE_STATE);

                }else{

                    nightModeState = false;
                    preferenceUtil.saveBooleanValue(false, preferenceUtil.PREFERENCE_NIGHTMODE_STATE);

                }
                recreate();


                break;

            case R.id.floatingWidgetSwitch:
                if(isChecked){
                    Snackbar.make(findViewById(R.id.settingFragment_container), "The Floating " +
                            "Widget will appear once you close the app.", Snackbar.LENGTH_LONG).show();
                    preferenceUtil.saveBooleanValue(true, preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE);
                }else{
                    preferenceUtil.saveBooleanValue(false, preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE);
                }
                break;

        }

    }

    public void notificationSwitch(int visibility, boolean checked, int image, int duration, int alphaValue){

        intervalConstraint.animate().alpha(alphaValue).setDuration(duration);
        intervalConstraint.setVisibility(visibility);
        changeImage(notificationImage, image);
        preferenceUtil.saveBooleanValue(checked, preferenceUtil.PREFERENCE_NOTIFICATION_STATE);

    }

    public void changeImage(ImageView imageView, int resID){

        imageView.setImageResource(resID);

    }

    public void animateTransition(ImageView imageView, int rotation, int alpha){

        imageView.animate().rotation(rotation).setDuration(6000).alpha(alpha).setDuration
                (6000);

    }

    public void sendingValue(){

//        settingData = new SettingData(notificationState, itemSelected, fontMain);
        Log.i("Destination1", "reached here");
        getIntent().putExtra("setting_data", notificationState);
        setResult(RESULT_OK, getIntent());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){

//            sendingValue();
            startActivity(new Intent(this, MainActivity.class));
//            onBackPressed();
            finish();

        }
        return true;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(this, MainActivity.class));
////        sendingValue();
////        super.onBackPressed();
        finish();
//
    }

    @Override
    public void onClick(View view) {
        DialogFragment dialogFragment = new TimePickerDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        preferenceUtil.saveInteger(hour, preferenceUtil.PREFERENCE_HOUR);
        preferenceUtil.saveInteger(minute, preferenceUtil.PREFERENCE_MINUTE);

        notificationScheduler.startNotification(hour, minute, this);

        intervalTextView.setText(getFormattedTime(hour, minute));

    }

    public String getFormattedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

}

