package com.example.daveart.vocabularyapp.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.SpinnerAdapter;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener {

    PreferenceUtil preferenceUtil;
    CardView nightModeCard, notificationCard, intervalCard, fontCard, floatingWidgetCard;
    SwitchCompat notificationSwitch, nightModeSwitch, floatingWidgetSwitch;
    AppCompatSpinner intervalSpinner, fontSpinner;
    ImageView notificationImage, nightModeImage;
    Toolbar toolbar;

    View rootView;

    boolean notificationState, nightModeState, floatingWidgetState;
    String itemSelected, fontMain;
    int userChoice;

    ArrayList<String> spinnerItemArray;

    SpinnerAdapter spinnerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        preferenceUtil = new PreferenceUtil(getContext());

        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
            getContext().setTheme(R.style.darkTheme);
        }else{
            getContext().setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_settings, container, false);
        setHasOptionsMenu(false);

        initializingEverything();
        assigningSpinnerItems();
        retrieveValues();

        fontSpinner.setSelection(0, false);
        fontSpinner.setSelected(false);
        fontSpinner.setOnItemSelectedListener(this);
        intervalSpinner.setOnItemSelectedListener(this);

        retrieveSelected();

        // handling event of switches
        notificationSwitch.setOnCheckedChangeListener(this);
        nightModeSwitch.setOnCheckedChangeListener(this);
        floatingWidgetSwitch.setOnCheckedChangeListener(this);

        if(!notificationSwitch.isChecked()){

            intervalCard.animate().alpha(0);
            intervalCard.setVisibility(View.GONE);
        }

        return rootView;
    }

    public void initializingEverything(){

        // switches
        nightModeSwitch = rootView.findViewById(R.id.nightModeSwitch);
        notificationSwitch = rootView.findViewById(R.id.notificationSwitch);
        floatingWidgetSwitch = rootView.findViewById(R.id.floatingWidgetSwitch);

        // spinners
//        intervalSpinner = rootView.findViewById(R.id.intervalSpinner);
        fontSpinner = rootView.findViewById(R.id.fontSpinner);

        // images
        nightModeImage = rootView.findViewById(R.id.nightMode_image);
        notificationImage = rootView.findViewById(R.id.notification_image);

        // cardViews
        nightModeCard = rootView.findViewById(R.id.nightModeConstraint);
        notificationCard = rootView.findViewById(R.id.notificationConstraint);
        intervalCard = rootView.findViewById(R.id.intervalConstraint);
        fontCard = rootView.findViewById(R.id.fontConstraint);
        floatingWidgetCard = rootView.findViewById(R.id.floatingWidgetConstraint);

        toolbar = rootView.findViewById(R.id.toolBarID);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

    }

    private void assigningSpinnerItems() {

        String[] array1 = new String[]{"1 min", "2 min", "3 min", "5 min", "10 min", "20 min",
                "25 min", "30 min", "40 min", "60 min"};

        ArrayList<String> intervalItems = new ArrayList<>(Arrays.asList(array1));
//        spinnerAdapter = new SpinnerAdapter(getContext(), intervalItems);
//        intervalSpinner.setAdapter(spinnerAdapter);

        String[] array = new String[]{ "AugustaShadow.ttf",
                "Augusta.ttf",
                "Abyssinica.ttf",
                "BLKCHCRY.TTF",
                "CreatorCampotype.ttf",
                "jiret_01.ttf",
                "DARK11.ttf",
                "EnglishTowne.ttf",
                "evanescent.ttf"};
        spinnerItemArray = new ArrayList<>(Arrays.asList(array));

//        spinnerAdapter = new SpinnerAdapter(getContext(), spinnerItemArray);
        fontSpinner.setAdapter(spinnerAdapter);

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
//                int position = adapterView.getSelectedItemPosition();
//                itemSelected = (String) adapterView.getItemAtPosition(i);
//                preferenceUtil.saveSpinnerValues(itemSelected, position,
//                        preferenceUtil.PREFERENCE_INTERVAL_POSITION, preferenceUtil
//                                .PREFERENCE_INTERVAL);
//
//                break;

            case R.id.fontSpinner:

                fontMain = (String) adapterView.getSelectedItem();
                userChoice = adapterView.getSelectedItemPosition();
                preferenceUtil.saveSpinnerValues(fontMain, userChoice, preferenceUtil
                        .PREFERENCE_FONT_POSITION, preferenceUtil.PREFERENCE_FONT);

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
//                                .getSelectedItemPosition(),
//                        preferenceUtil.PREFERENCE_INTERVAL_POSITION, preferenceUtil.PREFERENCE_INTERVAL);
//                break;

            case R.id.fontSpinner:

                fontMain = (String) adapterView.getSelectedItem();
                preferenceUtil.saveSpinnerValues(fontMain, adapterView.getSelectedItemPosition(),
                        preferenceUtil.PREFERENCE_FONT_POSITION, preferenceUtil.PREFERENCE_FONT);
                break;
        }

    }



    public void retrieveSelected(){

        int retrievedFontPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_FONT_POSITION, -1);
        int retrievedIntervalPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_INTERVAL_POSITION, -1);

        if(retrievedFontPosition != -1){
            fontSpinner.setSelection(retrievedFontPosition);
        }
        if(retrievedIntervalPosition != -1){
            intervalSpinner.setSelection(retrievedIntervalPosition);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        switch (compoundButton.getId()){

            case R.id.notificationSwitch:

                if(isChecked){

                    notificationState = true;

                    itemSelected = (String) intervalSpinner.getSelectedItem();
                    Log.i("Selected interval", itemSelected);
                    notificationSwitch(View.VISIBLE, true,
                            R.drawable.ic_notifications_active, 1000, 1);

                }else{

                    notificationState = false;
                    itemSelected = null;
                    notificationSwitch(View.GONE, false, R.drawable.ic_notifications, 0, 0);

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
//                getActivity().recreate();
//                getActivity().recreate();


                break;

            case R.id.floatingWidgetSwitch:
                if(isChecked){
                    preferenceUtil.saveBooleanValue(true, preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE);
                }else{
                    preferenceUtil.saveBooleanValue(false, preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE);
                }
                break;

        }

    }

    public void notificationSwitch(int visibility, boolean checked, int image, int duration, int alphaValue){

        intervalCard.animate().alpha(alphaValue).setDuration(duration);
        intervalCard.setVisibility(visibility);
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


//    public void sendingValue(){
//
////        settingData = new SettingData(notificationState, itemSelected, fontMain);
//        Log.i("Destination1", "reached here");
//        getIntent().putExtra("setting_data", notificationState);
//        setResult(RESULT_OK, getIntent());
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){

//            sendingValue();
            getFragmentManager().beginTransaction().add(new WordsFragment(), "word_fragment").commit();
//            startActivity(new Intent(getContext(), MainActivity.class));
//            Toast.makeText(getContext(), "Pressed", Toast.LENGTH_SHORT).show();
//            onBackPressed();
//            finish();

        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//
//        startActivity(new Intent(getContext(), MainActivity.class));
//////        sendingValue();
//////        super.onBackPressed();
////        finish();
////
//    }
}
