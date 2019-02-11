package com.example.daveart.vocabularyapp.activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.services.HomeScreenWidget;
import com.example.daveart.vocabularyapp.services.StackedWidgetView;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;

public class WidgetConfigActivity extends AppCompatActivity {

    Button button;
    RadioGroup radioGroup;
    RadioButton radioButton_stackView, radioButton_singleView;

    PreferenceUtil preferenceUtil;
    DataSource dataSource;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        preferenceUtil = new PreferenceUtil(this);

        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
            setTheme(R.style.darkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras != null){
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if(appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        dataSource = new DataSource(this);

        button = findViewById(R.id.button_widget_config);
        radioButton_singleView = findViewById(R.id.radioButton_widget_layout_singleView);
        radioButton_stackView = findViewById(R.id.radioButton_widget_layout_stackView);
        radioGroup = findViewById(R.id.radioGroup_widget_layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioButton_singleView.isChecked() || radioButton_stackView.isChecked()){
                    RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
                    saveSelectedRadio(radioGroup.indexOfChild(rbSelected));
                    displayWidgetBasedOnChoice(radioGroup.indexOfChild(rbSelected));
                    finish();

                }else{
                    Snackbar.make(findViewById(R.id.constraintLayout_widget_config), "No " +
                            "Selection is made", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveSelectedRadio(int selectedRadio) {

        boolean isStacked = false;

        if(selectedRadio == 1){
            isStacked = true;
        }

        preferenceUtil.saveBooleanValue(isStacked, preferenceUtil.PREFERENCE_IS_SELECTED_WIDGET_CONFIG_STACKED);
    }

    private void displayWidgetBasedOnChoice(int selectedRadio) {

        if(selectedRadio == 0){
            prepareWidget(R.layout.widget_layout_empty, selectedRadio);
        }else {
            prepareWidget(R.layout.widget_layout_single_view, selectedRadio);
        }

    }

    public void prepareWidget(int layoutId, int selectedRadio){
        RemoteViews remoteViews = new RemoteViews(getPackageName(), layoutId);
        if(selectedRadio == 0){
            Intent intent = new Intent(this, StackedWidgetView.class);
            remoteViews.setRemoteAdapter(R.id.stackView_widget_layout, intent);
            remoteViews.setEmptyView(R.id.stackView_widget_layout, R.id.textView_widget_layout);
        }else{
            ArrayList<String> arrayList = dataSource.randomWord();
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_word, arrayList.get(0));
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_meaning, arrayList.get
                    (1));
            remoteViews.setTextViewText(R.id.textView_widget_layout_single_wordType, arrayList.get
                    (2));
            Intent intent = new Intent(this, HomeScreenWidget.class);
            intent.setAction("REFRESH");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            remoteViews.setOnClickPendingIntent(R.id
                    .imageView_widget_layout_single_view_autoRenew, pendingIntent);
        }
        ComponentName thisWidget = new ComponentName(this, HomeScreenWidget.class);

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(appWidgetId, remoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
    }
}
