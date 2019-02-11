package com.example.daveart.vocabularyapp.activities;

import android.app.Dialog;

import android.content.Intent;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.services.FloatingWidgetReceiver;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.adapters.ViewPagerAdapter;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.fragments.HardWordsFragment;
import com.example.daveart.vocabularyapp.fragments.WordsFragment;
import com.example.daveart.vocabularyapp.interfaces.MeaningParsedListener;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.utils.FloatingViewService;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;
import com.example.daveart.vocabularyapp.utils.TypeFaceUtil;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MeaningParsedListener {

    public static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    public String word;

    public DataSource mDataSource;

    ViewPager viewPager;
    TabLayout tabLayout;

    ViewPagerAdapter viewPagerAdapter;

    NavigationView navigationView;

    RecyclerViewAdapter recyclerViewAdapter;
    CoordinatorLayout coordinatorLayout;

    PreferenceUtil preferenceUtil;
    WordsFragment wordsFragment;
    HardWordsFragment hardWordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferenceUtil = new PreferenceUtil(this);

        String fontSelected = preferenceUtil.retrieveItemsSelected(preferenceUtil
                .PREFERENCE_FONT, "Augusta.ttf");

        TypeFaceUtil.overrideFont(this, "SERIF", "fonts/" + fontSelected);

        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
            setTheme(R.style.darkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wordsFragment = new WordsFragment();
        hardWordsFragment = new HardWordsFragment();

        mDataSource = new DataSource(this);
        mDataSource.open();

//        sendBroadcast();

        // if the user comes to this screen when he/she is shown that it is the final day to
        // delete some words
        deleteDeadLine();

//        int day = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_DAY_OF_YEAR, -1);

        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

//        if(day != -1) {
//            calendar1.set(Calendar.DAY_OF_YEAR, day);
//            if(!calendar.before(calendar1)) {
//                Toast.makeText(this, "next day", Toast.LENGTH_SHORT).show();
//            }
//            Toast.makeText(this, "not next day", Toast.LENGTH_SHORT).show();
//            preferenceUtil.saveInteger(calendar.get(Calendar.DAY_OF_YEAR), preferenceUtil.PREFERENCE_DAY_OF_YEAR);
//        }else {
//            preferenceUtil.saveInteger(calendar.get(Calendar.DAY_OF_YEAR), preferenceUtil.PREFERENCE_DAY_OF_YEAR);
//        }



//        recyclerViewAdapter = new RecyclerViewAdapter(this);

        stopService(new Intent(this, FloatingViewService.class));

        coordinatorLayout = findViewById(R.id.coordinator);
        viewPager = findViewById(R.id.viewPagerID);
        tabLayout = findViewById(R.id.tabLayoutID);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(wordsFragment, "Saved Words");
        viewPagerAdapter.addFragment(hardWordsFragment, "Cached Words");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        startAlarm();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView =  findViewById(R.id.nav_view);

        settingTextToNavHeaderItems(navigationView);

    }

    private void deleteDeadLine() {
        Intent intent = getIntent();
        if("DeleteTheWords".equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(FloatingWidgetReceiver.TAG);
            for(long id : ids) {
                mDataSource.removeItemById(id, ItemTables.TABLE_NAME, ItemTables.COLUMN_ID);
                wordsFragment.recyclerViewAdapter.notifyDataSetChanged();
            }
            Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
        }else {
//            sendBroadcast();
            Toast.makeText(this, "Not Received", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableFloatingWidget() {
        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_FLOATINGWIDGET_STATE)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
                Intent intent  = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }else{
                initializeView();
            }
        }else{
            stopService(new Intent(this, FloatingViewService.class));
            FloatingViewService floatingViewService = new FloatingViewService();
            floatingViewService.stopSelf();

        }
    }

    public void initializeView(){
        startService(new Intent(this, FloatingViewService.class));
    }

    public void settingTextToNavHeaderItems(NavigationView navigationView) {

//        View navHeader = navigationView.getHeaderView(0);
        View nav = navigationView.getRootView();

        ConstraintLayout home = nav.findViewById(R.id.constraintLayout_navView_home_container);
        ConstraintLayout quiz = nav.findViewById(R.id.constraintLayout_navView_quiz_container);
        ConstraintLayout settings = nav.findViewById(R.id
                .constraintLayout_navView_setting_container);
        ConstraintLayout rating= nav.findViewById(R.id.constraintLayout_navView_rating_container);
        ConstraintLayout exit = nav.findViewById(R.id.constraintLayout_navView_exit_container);

        home.setOnClickListener(this);
        quiz.setOnClickListener(this);
        settings.setOnClickListener(this);
        rating.setOnClickListener(this);
        exit.setOnClickListener(this);

//        TextView textView_hardWords = navHeader.findViewById(R.id.textView_hardWords_navHeader);
//        TextView textView_number_of_words = navHeader.findViewById(R.id
//                .textView_number_of_words_navHeader);
//        TextView textView_score = navHeader.findViewById(R.id.textView_score_navHeader);
//
//        String totalWords = String.valueOf(mDataSource.getAllElements().getCount());
//
//        Log.i("total words", totalWords);
//
//        textView_number_of_words.setText(totalWords);

    }

    public void sendBroadcast(){
        Intent intent = new Intent(this, FloatingWidgetReceiver.class);
        intent.setAction("CalculatingDays");
        sendBroadcast(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION){
            if(resultCode == RESULT_OK){
                initializeView();
            }else{
                Toast.makeText(this, "Draw over other app permission not available.", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();
    }
//        Log.i("Destination", "reached here");
////        settingData = data.getParcelableExtra("setting_data");
//        boolean notification_state;
//        if(resultCode == RESULT_OK) {
//            notification_state = data.getBooleanExtra("setting_data", false);
//            Toast.makeText(this, String.valueOf(notification_state), Toast.LENGTH_SHORT).show();
////            recreate();
//
//        }

//        startAlarm(notification_state);
//        startAlarm(settingData.getmInterval(), settingData.ismNofificationState());

//    }

//    public void startAlarm(){
//
//        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent myIntent;
//        PendingIntent pendingIntent;
//        myIntent = new Intent(MainActivity.this, AlarmNotificationReceiver.class);
//
//        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        boolean currentState = preferenceUtil.retrieveBooleanValue(preferenceUtil
//                .PREFERENCE_NOTIFICATION_STATE);
//        boolean previousState = preferenceUtil.retrieveBooleanValue(preferenceUtil
//                .PREFERENCE_PREVIOUS_NOTIFICATION_STATE);
//        String interval="";
//        if(currentState){
//
//            interval = preferenceUtil.retrieveItemsSelected(preferenceUtil
//                    .PREFERENCE_INTERVAL, "0");
//            int intervalInMillis = convertToMillis(interval);
//
//            if(!String.valueOf(intervalInMillis).equals(preferenceUtil.retrieveItemsSelected
//                    (preferenceUtil.PREFERENCE_PREVIOUS_INTERVAL_POSITION, "0"))){
//                Log.i("Interval tag", String.valueOf(intervalInMillis));
//                Log.i("Interval tag1", preferenceUtil.retrieveItemsSelected(preferenceUtil
//                    .PREFERENCE_PREVIOUS_INTERVAL_POSITION, "0"));
//                assert manager != null;
//                manager.cancel(pendingIntent);
//                clearNotification();
//                manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000,
//                        intervalInMillis * 60000, pendingIntent);
//                preferenceUtil.saveInterval(interval);
//
//            }
//        }
//
//        if(!previousState && currentState){

//            int intervalInMillis = convertToMillis(interval);
//            int hour = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_HOUR, 12);
//            int minute = preferenceUtil.retrieveInteger(preferenceUtil.PREFERENCE_MINUTE, 0);
//            int second = 0;
//
//            Calendar calendar = Calendar.getInstance();
//            Calendar newCalendar = Calendar.getInstance();
//
//            newCalendar.set(Calendar.HOUR_OF_DAY, hour);
//            newCalendar.set(Calendar.MINUTE, minute);
//            newCalendar.set(Calendar.SECOND, second);
//
//            if(newCalendar.before(calendar)){
//                newCalendar.add(Calendar.DATE, 1);
//            }
//
//            assert manager != null;
//
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, newCalendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
//
//            preferenceUtil.saveBooleanValue(true, preferenceUtil.PREFERENCE_PREVIOUS_NOTIFICATION_STATE);
//        } else if(previousState && !currentState) {

//            assert manager != null;
//            manager.cancel(pendingIntent);
//            preferenceUtil.saveBooleanValue(false, preferenceUtil
//                    .PREFERENCE_PREVIOUS_NOTIFICATION_STATE);
//            clearNotification();
//            }
//        }

//    public void clearNotification(){
//        NotificationManager notificationManagerCompat = (NotificationManager) this
//                .getSystemService(NOTIFICATION_SERVICE);
//        assert notificationManagerCompat != null;
//        notificationManagerCompat.cancelAll();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isFinishing()){
            enableFloatingWidget();
        }
//        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open();
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch(view.getId()){
            case R.id.constraintLayout_navView_quiz_container:
                intent = new Intent(this, QuizStartPage.class);
                startActivity(intent);
                break;
            case R.id.constraintLayout_navView_setting_container:
//                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new
//                        SettingsFragment()).commit();
                intent = new Intent(this, SettingActivity.class);
                startActivityForResult(intent, 100);
//                finish();
                break;
            case R.id.constraintLayout_navView_rating_container:
                Snackbar.make(coordinatorLayout, "Feature not available", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.constraintLayout_navView_exit_container:
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

//        return true;
    }

    @Override
    public void onSaveClicked(String cachedWord, boolean isCheckBoxChecked, int wordPosition,
                              Dialog dialog, Object object, String word, boolean isEdited) {

//        String wordType = object.getWord_type();
//        String meaning = object.getMeaning();
//        String example = object.getExample();

//        SavedWord savedWord = new SavedWord(wordType, word, meaning, 0);

        SavedWord savedWord = (SavedWord) object;
        int positionOfTheNewWord = 0;
        int sortPreference = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_SORT_POSITION, -1);

        // sortPreference = 0 is for alphabetically sort and 1 is for recently added sort.
        if(sortPreference == 0){

//            I commented out the below line, cuz the algorithm there is not working at all
//            positionOfTheNewWord = new DataSource(this).findNewWordPosition(word);
            Toast.makeText(this, "position is " + String.valueOf(positionOfTheNewWord), Toast.LENGTH_SHORT).show();

        }

        if(wordsFragment.checkConditions(savedWord, positionOfTheNewWord, false, cachedWord,
                isCheckBoxChecked, isEdited, sortPreference)){

            if(cachedWord != null && isCheckBoxChecked){

                    hardWordsFragment.checkIfDataExists(wordPosition);

            }

        }

        dialog.dismiss();

    }

    @Override
    public void onCacheClicked(String word) {
        if(mDataSource.doesExist(word)) {

            Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show();

        } else {

            long id = new DataSource(this).insertSingleWord(word);

            if(id > 0){
                Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();

                hardWordsFragment.notifying(id);

            }else {
                Toast.makeText(this, "Problem with data insertion", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onClosingDialog() {
        // TODO: causing crash when screen is rotated
        wordsFragment.switchFAB(true);
    }

}
