package com.example.daveart.vocabularyapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;
import com.example.daveart.vocabularyapp.utils.RecyclerDividerUtil;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

//    TextView word_detailActivity, meaning_detailActivity, wordType_detailActivity;
//    ImageView close_detailActivity;
//    CardView cardView;
    PreferenceUtil preferenceUtil;
    Button button_cancel, button_continue;
    ArrayList<Integer> checkedPosition;
//
//    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
//    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
//    private static final String PACKAGE_NAME = "com.example.daveart.vocabularyapp.activities";
//    private static final int ANIM_DURATION = 5000;
//    public int orientation;
//    int mLeftDelta;
//    int mTopDelta;
//
//    float mWidth;
//    float mHeight;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    DataSource dataSource;

    public static final String DATA_NAME = "checkedPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        preferenceUtil = new PreferenceUtil(this);
//
        if(preferenceUtil.retrieveBooleanValue(preferenceUtil.PREFERENCE_NIGHTMODE_STATE)){
            setTheme(R.style.darkTheme);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_memorized_words);
        setFinishOnTouchOutside(true);

        button_cancel = findViewById(R.id.button_activityDetail_cancel);
        button_continue = findViewById(R.id.button_activityDetail_continue);

        button_continue.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView_activityDetail);
        dataSource = new DataSource(this);
        ArrayList<Object> memorizedWords = dataSource.getWordsAndTimestamp();
        recyclerViewAdapter = new RecyclerViewAdapter(memorizedWords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerDividerUtil(this, LinearLayoutManager
                .VERTICAL, 8));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);


//        word_detailActivity = findViewById(R.id.textView_activityDetail_word);
//        meaning_detailActivity = findViewById(R.id.textView_activityDetail_meaning);
//        wordType_detailActivity = findViewById(R.id.textView_activityDetail_wordType);
//        cardView = findViewById(R.id.cardView_activityDetail_wordAndMeaning);
//        close_detailActivity = findViewById(R.id.imageView_activity_detail_close);

//        Intent intent = getIntent();
//
//        String word = intent.getStringExtra("word");
//        String meaning = intent.getStringExtra("meaning");
//        String wordType = intent.getStringExtra("wordType");
//        orientation = intent.getIntExtra("orientation", 0);
//        Log.i("Word in detail", word + " " + meaning);

//        final int width = intent.getIntExtra("viewWidth", 0);
//        final int height = intent.getIntExtra("viewHeight", 0);

//        word_detailActivity.setText(word);
//        meaning_detailActivity.setText(meaning);
//        wordType_detailActivity.setText(wordType);

//        if (savedInstanceState == null) {
//
//            ViewTreeObserver viewTreeObserver = cardView.getViewTreeObserver();
//            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    cardView.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                    mWidth = (float) width / cardView.getWidth();
//                    mHeight = (float) height / cardView.getHeight();
//
//                    runEnterAnimation();
//
//                    return true;
//                }
//            });
//
//        }
//        close_detailActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

    }

//    private void runEnterAnimation() {
//        final long duration = (long) (ANIM_DURATION * WordsFragment.sAnimatorScale);

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
//        cardView.setPivotX(0);
//        cardView.setPivotY(0);
//        cardView.setScaleX(mWidth);
//        cardView.setScaleY(mHeight);
//        cardView.setTranslationX(mLeftDelta);
//        cardView.setTranslationY(mTopDelta);
//
        // We'll fade the text in later
//        word_detailActivity.setAlpha(0);
//        wordType_detailActivity.setAlpha(0);
//        meaning_detailActivity.setAlpha(0);

        // Animate scale and translation to go from thumbnail to full size
//        cardView.animate().setDuration(duration).
//                scaleX(1).scaleY(1).
//                translationX(0).translationY(0).
//                setInterpolator(sDecelerator).
//                withEndAction(new Runnable() {
//                    public void run() {
                        // Animate the description in after the image animation
                        // is done. Slide and fade the text in from underneath
                        // the picture.
//                        word_detailActivity.setTranslationY(-word_detailActivity.getHeight());
//                        wordType_detailActivity.setTranslationY(-wordType_detailActivity.getHeight
//                                ());
//                        meaning_detailActivity.setTranslationY(-meaning_detailActivity.getHeight());
//                        word_detailActivity.animate().setDuration(duration/2).
//                                translationY(0).alpha(1).
//                                setInterpolator(sDecelerator);
//                        meaning_detailActivity.animate().setDuration(duration/2).
//                                translationY(0).alpha(1).
//                                setInterpolator(sDecelerator);
//                        wordType_detailActivity.animate().setDuration(duration/2).
//                                translationY(0).alpha(1).
//                                setInterpolator(sDecelerator);
//                    }
//                });

        // Fade in the black background
//        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
//        bgAnim.setDuration(duration);
//        bgAnim.start();
//
//        // Animate a color filter to take the image from grayscale to full color.
//        // This happens in parallel with the image scaling and moving into place.
//        ObjectAnimator colorizer = ObjectAnimator.ofFloat(PictureDetailsActivity.this,
//                "saturation", 0, 1);
//        colorizer.setDuration(duration);
//        colorizer.start();
//
//        // Animate a drop-shadow of the image
//        ObjectAnimator shadowAnim = ObjectAnimator.ofFloat(mShadowLayout, "shadowDepth", 0, 1);
//        shadowAnim.setDuration(duration);
//        shadowAnim.start();
//    }

    @Override
    protected void onDestroy() {
//        word_detailActivity.setText(null);
//        meaning_detailActivity.setText(null);
        super.onDestroy();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        checkedPosition = recyclerViewAdapter.getCheckedPositions();
        switch (v.getId()) {
            case R.id.button_activityDetail_continue:
                Intent intent = new Intent(this, QuizStartPage.class);
                intent.putExtra(DATA_NAME, checkedPosition);
                intent.setAction("From memorized words");
                startActivity(intent);
                Toast.makeText(this, checkedPosition.size() + " items.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_activityDetail_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
