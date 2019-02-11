package com.example.daveart.vocabularyapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.fragments.WordQuestionsFragment;
import com.example.daveart.vocabularyapp.utils.OtherUtils;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;

public class QuizStartPage extends AppCompatActivity {

    Button quiz_type_buttonID;
    Toolbar toolbar;
    PreferenceUtil preferenceUtil;
    OtherUtils otherUtils;
    DataSource dataSource;
    PopupWindow popupWindow;
    ArrayList<Integer> memorizedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceUtil = new PreferenceUtil(this);

        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
            setTheme(R.style.darkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        otherUtils = new OtherUtils(this);
        dataSource = new DataSource(this);

        findingViewsById();

        Intent intent = getIntent();

        if(intent.getExtras() == null){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        }else {
            memorizedWords = intent.getIntegerArrayListExtra("checkedPosition");
            Toast.makeText(this, intent.getAction(), Toast.LENGTH_SHORT).show();
        }

        // the first parameter is for removing the old and the second is for adding the new one
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);

//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id
//                .quizFragment_container);
//        if(!(fragment instanceof IOnBackPressed) || !((IOnBackPressed)fragment).onBackPressed()){
//            super.onBackPressed();
//        }

        quiz_type_buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataSource.savedWords(ItemTables.TABLE_NAME, 0).size() < 3){
                    Toast.makeText(QuizStartPage.this, "Sorry, no enough data to start the quiz.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    promptToChoose();
                }
            }
        });

    }

    public interface IOnBackPressed{
        boolean onBackPressed();
    }

    public IOnBackPressed iOnBackPressed;

    public void setiOnBackPressed(IOnBackPressed iOnBackPressed) {
        this.iOnBackPressed = iOnBackPressed;
    }

    private void findingViewsById() {
        toolbar = findViewById(R.id.quizType_toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quiz_type_buttonID = findViewById(R.id.choose_quiz_type_buttonID);
        quiz_type_buttonID.setTextColor(otherUtils.accessingStyleableColor(R.styleable.ds_textColor));
        quiz_type_buttonID.setBackgroundColor(otherUtils.accessingStyleableColor(R.styleable
                .ds_iconColor));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
    private void promptToChoose() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View promptView = inflater.inflate(R.layout.quiz_type_layout, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        popupWindow = new PopupWindow(promptView, width, height, true);
        popupWindow.setAnimationStyle(R.style.popUpScaleAnimation);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(promptView, Gravity.CENTER, 0, 0);
//        Dialog dialog = new Dialog(this);
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.quiz_type_layout);
        CardView cardView_title = promptView.findViewById(R.id.cardView_quizType_quiz_type);
        CardView word_rippleID = promptView.findViewById(R.id.cardView_quizType_word);
        CardView meaning_rippleID = promptView.findViewById(R.id.cardView_quizType_meaning);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scaling_sliding_from_left);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.scaling_siding_from_right);
        word_rippleID.startAnimation(animation);
        meaning_rippleID.startAnimation(animation1);
//        cardView_title.startAnimation(animation);

//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();

        word_rippleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WordQuestionsFragment wordQuestionsFragment = new WordQuestionsFragment()
                        .newInstance(ItemTables.COLUMN_WORD, memorizedWords);
                goToAnotherFragment(wordQuestionsFragment, "word_quiz");
//                goingToAnotherActivity(WordQuestions.class);
            }
        });

        meaning_rippleID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                goingToAnotherActivity(MeaningQuestions.class);
                WordQuestionsFragment wordQuestionsFragment = new WordQuestionsFragment()
                        .newInstance(ItemTables.COLUMN_MEANING, memorizedWords);
                goToAnotherFragment(wordQuestionsFragment, "meaning_quiz");
            }
        });

    }

    public Fragment getCurrentFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0){
            String lastFragmentName = fragmentManager.getBackStackEntryAt(fragmentManager
                    .getBackStackEntryCount() - 1).getName();
            return fragmentManager.findFragmentByTag(lastFragmentName);
        }
        return null;
    }

    public void goToAnotherFragment(WordQuestionsFragment fragment, String fragmentTag){
        getSupportFragmentManager().beginTransaction().replace(R.id.quizFragment_container,
                fragment).addToBackStack(fragmentTag).commit();
        popupWindow.dismiss();
    }

    public void goingToAnotherActivity(Class activityName){
        Intent intent  = new Intent(this, activityName);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(iOnBackPressed != null && iOnBackPressed.onBackPressed()){
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();

    }

    //    @Override
//    public void onBackPressed() {
//        Fragment currentFragment = getCurrentFragment();
//        if(currentFragment != null && currentFragment instanceof Interfaces1){
//            if(((Interfaces1)currentFragment).onBackPressed()){
//                return;
//            }else{
//                super.onBackPressed();
//            }
//        }
//        super.onBackPressed();
//    }
}
