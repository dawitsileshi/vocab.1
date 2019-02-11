package com.example.daveart.vocabularyapp.activities;

import android.app.Dialog;

import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;

//import com.example.daveart.vocabularyapp.adapters.MeaningParsedRecycler;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;

import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;

import com.example.daveart.vocabularyapp.dialog_fragments.MeaningParsedDialogFragment;

import com.example.daveart.vocabularyapp.interfaces.MeaningParsedListener;
import com.example.daveart.vocabularyapp.model.MeaningParsed;

import com.example.daveart.vocabularyapp.utils.AnimationsUtil;
import com.example.daveart.vocabularyapp.utils.ExceptionUtil;

import java.util.List;

public class AnotherActivity extends AppCompatActivity implements MeaningParsedListener {

    EditText editText_alertLayout_word;
    Button button_alertLayout_save, imageView_alertLayout_cache;
    RecyclerView recyclerView_alertLayout;
    TextView textView_alertLayout_meaning;

    RecyclerViewAdapter recyclerViewAdapter;

    ImageView imageView_alertLayout_search, imageView_alertLayout_exit;

//    MeaningParsedRecycler meaningParsedRecycler;
    DataSource dataSource;

    ExceptionUtil exceptionUtil;
    AnimationsUtil animationsUtil;

    MeaningParsedListener meaningParsedListener;
    List<MeaningParsed> meaningParsedList;
    MeaningParsedDialogFragment meaningParsedDialogFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_layout);
        setFinishOnTouchOutside(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.dimAmount = 0.5f;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager
                .LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        getWindow().setAttributes(layoutParams);
//        exceptionUtil = new ExceptionUtil(this);
//        animationsUtil = new AnimationsUtil(this);
//        dataSource = new DataSource(this);
//        recyclerViewAdapter = new RecyclerViewAdapter(this, dataSource.savedWords(ItemTables
//                .TABLE_NAME, 0), 0);
//
//        meaningParsedList = new ArrayList<>();

//        meaningParsedDialogFragment = new MeaningParsedDialogFragment();
//        meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance(null, -1,
//                null);
//        meaningParsedDialogFragment.show(getSupportFragmentManager(), "alert_layout");

//        findingViewById();
//        settingUpRecyclerView();
//
//        imageView_alertLayout_exit.setOnClickListener(this);
//        imageView_alertLayout_search.setOnClickListener(this);
//        button_alertLayout_save.setOnClickListener(this);
//        imageView_alertLayout_cache.setOnClickListener(this);
//
//        imageView_alertLayout_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                parseJson(editText_alertLayout_word.getText().toString().toLowerCase());
//            }
//        });
    }

    @Override
    public void onSaveClicked(String cachedWord, boolean isCheckBoxChecked, int wordPostion, Dialog dialog, Object object, String word, boolean isEdited) {

    }

    @Override
    public void onCacheClicked(String word) {

    }

    @Override
    public void onClosingDialog() {

    }

//    private void findingViewById() {
//
//        textView_alertLayout_meaning = findViewById(R.id.textView_alertLayout_noData);
//
//        editText_alertLayout_word = findViewById(R.id.editText_alertLayout_word);
//        button_alertLayout_save = findViewById(R.id.button_alertLayout_save);
//        recyclerView_alertLayout = findViewById(R.id.recyclerView_alertLayout);
//
//        imageView_alertLayout_exit = findViewById(R.id.imageView_alertLayout_exit);
//        imageView_alertLayout_search = findViewById(R.id.imageView_alertLayout_search);
//        imageView_alertLayout_cache = findViewById(R.id.button_alertLayout_cache);
//
//    }

//    private void parseJson(String s) {
//
//        if (exceptionUtil.checkConnection()) {
//
////            clearTheList();
//            button_alertLayout_save.setEnabled(false);
//            imageView_alertLayout_search.startAnimation(animationsUtil.customAnimation(R
//                    .anim.swing));
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            String url = "https://owlbot.info/api/v2/dictionary/" + s;
//
//            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//                    try {
//
//                        if (response.length() == 0) {
//
////                            changingVisibility(View.VISIBLE, true, "No Data found");
//                            imageView_alertLayout_search.clearAnimation();
//
//                        } else {
//
//                            for (int i = 0; i < response.length(); i++) {
//
//                                JSONObject object = response.getJSONObject(i);
//
////                                changingVisibility(View.GONE, false, null);
//
////                                displayingInRecycler(object);
//
//                                button_alertLayout_save.setEnabled(true);
//
//                            }
//                        }
//                    } catch (JSONException e) {
//
//                        changingVisibility(View.VISIBLE, true, "Something happened, please try " +
//                                "again");
//                        imageView_alertLayout_search.clearAnimation();
//                        Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json", e);
//
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    int statusCode = error.networkResponse.statusCode;
//                    changingVisibility(View.VISIBLE, true, "Connection Problem, Please try " +
//                            "again.");
//                    textView_alertLayout_meaning.setText(R.string.connection_problem);
//                    imageView_alertLayout_search.clearAnimation();
//                    Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json1" +
//                            error.toString());
//
//                }
//            });
//
//
//            requestQueue.add(jsonArrayRequest);
//
//        }
//    }

//    @Override
//    public void onSaveClicked(String word, String wordType, String meaning, String example, String cachedWord) {
//
//    }
//
//    @Override
//    public void onCacheClicked(String word) {
//
//    }
//
//    @Override
//    public void onClosingDialog(EditText editText) {
//
//    }

//        private void displayingInRecycler (JSONObject object) throws JSONException {
//
//            String wordType = object.getString("type");
//            String meaning = object.getString("definition");
//            String example = object.getString("example");
//
//            meaningParsedList.add(new MeaningParsed(wordType, meaning, example));
//            meaningParsedRecycler.notifyDataSetChanged();
//            imageView_alertLayout_search.clearAnimation();
//        }
//
//        private void settingUpRecyclerView () {
//
//            recyclerView_alertLayout.setLayoutManager(new LinearLayoutManager(this));
//            meaningParsedRecycler = new MeaningParsedRecycler(this, meaningParsedList);
//            recyclerView_alertLayout.setAdapter(meaningParsedRecycler);
//
//        }
//
//
//        @Override
//        public void onClick (View view){
//            switch (view.getId()) {
//                case R.id.imageView_alertLayout_search:
//
//                    if (!checkEmptiness()) {
//                        Log.i("EditTExt length", String.valueOf(editText_alertLayout_word.getText()
//                                .length()));
//
//                        meaningParsedRecycler.setTag(-1);
//                        changingVisibility(View.GONE, false, null);
//                        parseJson(editText_alertLayout_word.getText().toString().toLowerCase());
//
//                    } else {
//
//                        displayToastMessage("No words found.");
//
//                    }
//                    break;
//
//                case R.id.imageView_alertLayout_exit:
//
//                    editText_alertLayout_word.setText("");
//                    startTheService();
//                    break;
//
//                case R.id.button_alertLayout_save:
//
//                    if (checkEmptiness()) {
//
//                        displayToastMessage("Edit text is empty");
//                        YoYo.with(Techniques.Shake).playOn(editText_alertLayout_word);
//
//                    } else if (checkNoData()) {
//
//                        button_alertLayout_save.setEnabled(false);
//
//                    } else if (meaningParsedRecycler.getTag() == -1) {
//
//                        displayToastMessage("No Selections made");
//                        YoYo.with(Techniques.Shake).playOn(findViewById(R.id.alert_layout));
//
//                    } else {
//
//                        handlingSaveConditions();
//
//                    }
//
//                    break;
//
//                case R.id.button_alertLayout_cache:
//
//                    if (editText_alertLayout_word.getText().toString().length() != 0) {
////                        if (dataSource.insertSingleWord(editText_alertLayout_word.getText().toString()
////                        ) > 0) {
//                        meaningParsedListener.onCacheClicked(editText_alertLayout_word.getText().toString());
////                            displayToastMessage("Inserted");
//
//                    } else {
//                        displayToastMessage("No word found");
//                    }
//                    break;
//
//            }
//        }
//
//        private boolean checkNoData () {
//            return meaningParsedList.size() == 0;
//        }
//
//        public boolean checkEmptiness () {
//            return editText_alertLayout_word.getText().toString().length() == 0;
//        }
//
//        public void handlingSaveConditions () {
//
//            int currentIndex = meaningParsedRecycler.getTag();
//
//            MeaningParsed currentMeaningParsed = meaningParsedList.get(currentIndex);
//
//            String word = editText_alertLayout_word.getText().toString();
//            String wordType = currentMeaningParsed.getWord_type();
//            String meaning = currentMeaningParsed.getMeaning();
//
////            SavedWord dataItem = new SavedWord(wordType, word, meaning, 0);
//            meaningParsedListener.onSaveClicked(word, wordType, meaning, currentMeaningParsed
//                    .getExample(), null, false, 0, null);
////            new WordsFragment().insertData(dataSource.checkIfExists(dataItem), dataSource.checkEmpty
////                    (dataItem), dataSource.checkNumber(dataItem), dataItem, 0, false);
//            startTheService();
//
//        }
//
//        public void startTheService () {
//            editText_alertLayout_word.setText("");
//            startService(new Intent(this, FloatingViewService.class));
//            finish();
//        }
//
//        public void clearTheList () {
//            meaningParsedList.clear();
//            meaningParsedRecycler.notifyDataSetChanged();
//        }
//
//        public void changingVisibility ( int visibility, boolean isVisible, String msg){
//
//            if (isVisible) {
//                textView_alertLayout_meaning.setText(msg);
//            }
//            textView_alertLayout_meaning.setVisibility(visibility);
//        }
//
//        public void displayToastMessage (String msg){
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//        }


}
