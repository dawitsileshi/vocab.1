package com.example.daveart.vocabularyapp.dialog_fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.daveart.vocabularyapp.R;
//import com.example.daveart.vocabularyapp.adapters.MeaningParsedRecycler;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.custom_views.DualProgressView;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.interfaces.MeaningParsedListener;
import com.example.daveart.vocabularyapp.interfaces.NetworkCheckInterface;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.model.MeaningParsed;
import com.example.daveart.vocabularyapp.utils.ExceptionUtil;
import com.example.daveart.vocabularyapp.utils.NetworkUtils;
import com.example.daveart.vocabularyapp.utils.RecyclerDividerUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.util.Log;


public class MeaningParsedDialogFragment extends DialogFragment implements View.OnClickListener,
        NetworkCheckInterface {

    private ArrayList<Object> meaningParsedList;
    private ArrayList<String> suggestionWords;
    private static final String WORD_SENT = "word_sent"; /* sent to be displayed on the editText
    by the cachedwords fragment, pass null if you don't wanna do anything with it */
    private static final String POSITION_SENT = "position_sent"; /* is the position the cached
    word in the adapter, we use it to remove it from the adapter, pass -1 if you don't wanna do
    anything with it*/
    private static final String ARRAYLIST_SENT = "arrayList_sent"; /* this is the identifier for
    the collection of strings sent from the words fragment to be used as suggestion, pass null if
     you don't wanna do anything with it */

    private static final String CONNECTION_FLAG_SENT = "connection_flag_sent"; /* checks if the
    dialog is started for word edition purpose, if passed true, the dialog will automatically
    start connecting to the internet to fetch the meanings */

    private static final String ID_SENT = "id_sent"; /* this is used for updating(editing) the
    word in the db */

    private ImageView imageButton_search, imageButton_exit;
    private Button button_save, button_cache;
    private AutoCompleteTextView editText_word;
    private RecyclerView recyclerView_meaningsParsed;
    private TextView textView_noData;
    private AppCompatCheckBox checkBox_delete;
    private DualProgressView dualProgressView;
    private LinearLayout linearLayout_search_container;

    private RequestQueue requestQueue;

    private ConstraintLayout constraintLayout_alertLayout;

    private boolean isErrorConnecting = false, isSearching = false, isEditing = false;
    private String temp = null;

//    MeaningParsedRecycler meaningParsedRecycler;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DataSource dataSource;
    private ExceptionUtil exceptionUtil;
    private NetworkUtils networkUtils;

    private MeaningParsedListener meaningParsedListener;

    public MeaningParsedDialogFragment newInstance(String word, int adapterPosition,
                                                   ArrayList<String> strings, boolean startConnecting, long id) {
        MeaningParsedDialogFragment meaningParsedDialogFragment = new MeaningParsedDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WORD_SENT, word);
        bundle.putInt(POSITION_SENT, adapterPosition);
        bundle.putStringArrayList(ARRAYLIST_SENT, strings);
        bundle.putBoolean(CONNECTION_FLAG_SENT, startConnecting);
        bundle.putLong(ID_SENT, id);
        meaningParsedDialogFragment.setArguments(bundle);
        return meaningParsedDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.alert_layout, null);

        findingViewById(rootView);

        creatingObjects();

        settingPropertiesOfDialog();

        settingUpRecyclerView();

        readingPassedValues();

        handlingClicks();

        return rootView;

    }

    private void findingViewById(View rootView) {

        imageButton_exit = rootView.findViewById(R.id.imageView_alertLayout_exit);
        imageButton_search = rootView.findViewById(R.id.imageView_alertLayout_search);
        button_cache = rootView.findViewById(R.id.button_alertLayout_cache);
        editText_word = rootView.findViewById(R.id.appCompatTextView_alertLayout_word);
        button_save = rootView.findViewById(R.id.button_alertLayout_save);
        recyclerView_meaningsParsed = rootView.findViewById(R.id.recyclerView_alertLayout);
        textView_noData = rootView.findViewById(R.id.textView_alertLayout_noData);
        constraintLayout_alertLayout = rootView.findViewById(R.id.constraintLayout_alertLayout);
        checkBox_delete = rootView.findViewById(R.id.checkBox_alertLayout_delete);
        dualProgressView = rootView.findViewById(R.id.progressBar_alertLayout);
        linearLayout_search_container = rootView.findViewById(R.id.linearLayout_alertLayout_search_container);

    }

    private void creatingObjects() {

        meaningParsedList = new ArrayList<>();
        dataSource = new DataSource(getActivity());
        exceptionUtil = new ExceptionUtil(getContext());
        networkUtils = new NetworkUtils(this);

    }

    private void settingPropertiesOfDialog() {

        setCancelable(false);
        editText_word.clearFocus();

        this.getDialog().getWindow().setWindowAnimations(R.style.popUpAnimation);
        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

    }

    private void settingUpRecyclerView() {

        recyclerView_meaningsParsed.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), meaningParsedList, 0);
        recyclerViewAdapter = new RecyclerViewAdapter(meaningParsedList);
//        meaningParsedRecycler = new MeaningParsedRecycler(this.getActivity(), meaningParsedList);
        recyclerView_meaningsParsed.setAdapter(recyclerViewAdapter);
        recyclerView_meaningsParsed.addItemDecoration(new RecyclerDividerUtil(getActivity(),
                LinearLayoutManager.VERTICAL, 16));

    }

    private void readingPassedValues() {

        if (getArguments().getString(WORD_SENT) != null) {

            editText_word.setText(getArguments().getString(WORD_SENT));
            checkBox_delete.setVisibility(View.VISIBLE);
            button_cache.setEnabled(false);
            button_cache.setAlpha(0.5f);

        }

        suggestionWords = dataSource.getAllWords();

//        if (getArguments().getStringArrayList(ARRAYLIST_SENT) != null) {

//            suggestionWords = getArguments().getStringArrayList(ARRAYLIST_SENT);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout
                    .simple_dropdown_item_1line, suggestionWords);
            editText_word.setAdapter(adapter);

//        }

        if(getArguments().getBoolean(CONNECTION_FLAG_SENT)) {

            button_save.setText(R.string.edit);
            isEditing = true;
            requestQueue = Volley.newRequestQueue(getContext());
            performTheSearchClickPosition(requestQueue); }
    }

    private void handlingClicks() {

//        imageButton_search.setOnClickListener(this);
        linearLayout_search_container.setOnClickListener(this);
        imageButton_exit.setOnClickListener(this);
        button_save.setOnClickListener(this);
        button_cache.setOnClickListener(this);

    }

    private void parseJson(final String word) {

        if (exceptionUtil.checkConnection()) {
            button_save.setEnabled(false);
            isSearching = false;
//
            changingProperties(R.drawable.ic_clear, View.VISIBLE);
//            dualProgressView.setVisibility(View.VISIBLE);
//            imageButton_search.setImageResource(R.drawable.ic_pause_whiite_24dp);

//            requestQueue = Volley.newRequestQueue(getActivity());
//            String url = "https://owlbot.info/api/v2/dictionary/" + word;
            networkUtils.searchingForMeaning(word, getActivity(), requestQueue);
//            requestQueue.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy
//                    .DEFAULT_TIMEOUT_MS, 0, 0));
//            setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
//            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
//                @Override
//                public void onResponse(JSONArray response) {
//                    try {
//
//                        Log.i("Json", String.valueOf(response));
//                        if(response.length() == 0){
//                            changingVisibility(View.VISIBLE, "No Data Found");
////                            imageButton_search.clearAnimation();
//                            dualProgressView.setVisibility(View.GONE);
//                        }
//
//                        for (int i = 0; i < response.length(); i++) {
//
//                            JSONObject object = response.getJSONObject(i);
//
//                            displayingInRecycler(object);
//                            button_save.setEnabled(true);
//
//                        }
//                    } catch (JSONException e) {
//
//                        changingVisibility(View.VISIBLE, "Error retrieving data");
//
////                        imageButton_search.clearAnimation();
//                        dualProgressView.setVisibility(View.GONE);
//                        Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json", e);
//
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
////                    int statusCode = error.networkResponse.statusCode;
//                    changingVisibility(View.VISIBLE, "Connection Problem\nPlease try again");
////                    imageButton_search.clearAnimation();
//                    dualProgressView.setVisibility(View.GONE);
//                    isErrorConnecting = true;
//                    Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json1" +
//                            error.toString());
//
//                }
//            });
//
//            requestQueue.add(jsonArrayRequest);
            temp = editText_word.getText().toString();
//
        } else {

            displayToastMessage("No Connection");
//
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        requestQueue = Volley.newRequestQueue(getContext());

        switch (id) {

            case R.id.linearLayout_alertLayout_search_container:

                performTheSearchClickPosition(requestQueue);

                break;

            case R.id.imageView_alertLayout_exit:

                if (requestQueue != null) {
                    requestQueue.stop();
                }
                getDialog().cancel();
                meaningParsedListener.onClosingDialog();
                editText_word.setText("");
//                    dismissAllowingStateLoss();
//                    dismiss();

                break;
            case R.id.button_alertLayout_save:

                if (checkEmptiness()) {
                    editText_word.setText(temp);

                } else if (checkNoData()) {

                    button_save.setEnabled(false);

                } else if (recyclerViewAdapter.getLastCheckedPosition() == -1) {
                    displayToastMessage("No selections made.");
                    YoYo.with(Techniques.Shake).playOn(constraintLayout_alertLayout);
                } else {
                    handlingSaveConditions();
                }
                break;

            case R.id.button_alertLayout_cache:

                if (!checkEmptiness()) {
                    meaningParsedListener.onCacheClicked(editText_word.getText().toString().trim());
                } else {
                    displayToastMessage("No word found");
                }
                break;

        }

    }

    private void performTheSearchClickPosition(RequestQueue requestQueue) {
        isErrorConnecting = false;
        // checks if the edit text is empty or not
        if (!checkEmptiness()) {
            clearTheList();
//                    recyclerViewAdapter.setTag(-1);
            changingVisibility(View.GONE, null);
            SavedWord savedWord = dataSource.checkIfExistsAndRetrieve(editText_word.getText
                    ().toString());
            if (savedWord != null && !getArguments().getBoolean(CONNECTION_FLAG_SENT)) {
                String meaning = "(" + savedWord.getWordType() + ") " + savedWord
                        .getMeaning();
                changingVisibility(View.VISIBLE, meaning);
            } else {
                if (isSearching) {
                    requestQueue.cancelAll(NetworkUtils.NETWORK_TAG);
                    requestQueue.stop();
                    changingProperties(R.drawable.ic_search, View.GONE);
                    Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
                    isSearching = false;
                } else {
                    isSearching = true;
                    parseJson(editText_word.getText().toString().toLowerCase());
                }
            }
        } else {
            displayToastMessage("No words found.");
        }
    }

    private void changingProperties(int drawable, int visibility) {


        animateTheImage(0, "scaleX", 0f, 300, new DecelerateInterpolator(), drawable);
        animateTheImage(0, "scaleY", 0f, 300, new DecelerateInterpolator(), drawable);
//        imageButton_search.setImageResource(drawable);
        animateTheImage(1000, "scaleX", 1f, 200, new AccelerateInterpolator(), drawable);
        animateTheImage(1000, "scaleY", 1f, 200, new AccelerateInterpolator(), drawable);

//        ObjectAnimator objectAnimanetor;

//        objectAnimator = ObjectAnimator.ofFloat(imageButton_search, "scaleX", 0f);
//        objectAnimator.setDuration(3000);
//        objectAnimator.start();
//        objectAnimator = ObjectAnimator.ofFloat(imageButton_search, "scaleY", 0f);
//        objectAnimator.setDuration(3000);
//        objectAnimator.start();
//
//
//        objectAnimator = ObjectAnimator.ofFloat(imageButton_search, "scaleX", 1f);
//        objectAnimator.setDuration(3000);
//        objectAnimator.setStartDelay(3000);
//        objectAnimator.start();
//        objectAnimator = ObjectAnimator.ofFloat(imageButton_search, "scaleY", 1f);
//        objectAnimator.setDuration(3000);
//        objectAnimator.setStartDelay(3000);
//        objectAnimator.start();

        dualProgressView.setVisibility(visibility);

    }

    private void animateTheImage(int startDelay, String animationType, float value, int duration,
                                 TimeInterpolator interpolator, final int drawable) {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageButton_search, animationType,
                value);
        objectAnimator.setDuration(duration);
        objectAnimator.setStartDelay(startDelay);
        objectAnimator.setInterpolator(interpolator);

        objectAnimator.addListener(new AnimatorListenerAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param animation
             */
            @Override
            public void onAnimationEnd(Animator animation) {
                imageButton_search.setImageResource(drawable);
            }
        });
        objectAnimator.start();
    }

    private boolean checkNoData() {
        return meaningParsedList.size() == 0;
    }

    private boolean checkEmptiness() {
        return editText_word.getText().toString().length() == 0;
    }

    private void handlingSaveConditions() {

        int currentIndex = recyclerViewAdapter.getLastCheckedPosition();

        MeaningParsed currentMeaningParsed = (MeaningParsed) meaningParsedList.get(currentIndex);

        assert getArguments() != null;
        long id = getArguments().getLong(ID_SENT);
        String word = temp;
        String wordType = currentMeaningParsed.getWord_type();
        String meaning = currentMeaningParsed.getMeaning();
        String example = currentMeaningParsed.getExample();

        Log.i("Example", example);
        SavedWord savedWord = new SavedWord(id, wordType, word, meaning, 0, example);
        String word_sent = getArguments().getString(WORD_SENT);
        boolean isCheckBoxChecked = checkBox_delete.isChecked();
        int positionOfTheWord = getArguments().getInt(POSITION_SENT);

        meaningParsedListener.onSaveClicked(word_sent, isCheckBoxChecked, positionOfTheWord,
                getDialog(), savedWord, word, isEditing);

    }

    private void displayingInRecycler(JSONObject object) throws JSONException {

        String wordType = object.getString("type");
        String meaning = object.getString("definition");
        String example = object.getString("example");

        meaningParsedList.add(new MeaningParsed(wordType, meaning, example));
        recyclerViewAdapter.notifyDataSetChanged();
        imageButton_search.clearAnimation();

    }

    private void clearTheList() {
        meaningParsedList.clear();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void changingVisibility(int visibility, String msg) {

        if (visibility == View.VISIBLE) {
            textView_noData.setText(msg);
        }
        textView_noData.setVisibility(visibility);
    }

    private void displayToastMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MeaningParsedListener) {
            meaningParsedListener = (MeaningParsedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement the interface");
        }

    }

    @Override
    public void onSuccess(JSONArray jsonArray) {
        isSearching = false;
        try {

            Log.i("Json", String.valueOf(jsonArray));
            if (jsonArray.length() == 0) {
                changingVisibility(View.VISIBLE, "No Data Found");
                changingProperties(R.drawable.ic_search, View.GONE);
//                imageButton_search.clearAnimation();
            } else {

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);

                    displayingInRecycler(object);
                    button_save.setEnabled(true);

                }

                changingProperties(R.drawable.ic_search, View.GONE);
//                dualProgressView.setVisibility(View.GONE);
//                imageButton_search.setImageResource(R.drawable.ic_search);

            }

        } catch (JSONException e) {

            changingVisibility(View.VISIBLE, "Error retrieving data");

//                        imageButton_search.clearAnimation();
            changingProperties(R.drawable.ic_search, View.GONE);
//            dualProgressView.setVisibility(View.GONE);
//            imageButton_search.setImageResource(R.drawable.ic_search);

            Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json", e);

        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        isSearching = false;
        changingVisibility(View.VISIBLE, "Connection Problem\nPlease try again");
//                    imageButton_search.clearAnimation();
        changingProperties(R.drawable.ic_search, View.GONE);
//        dualProgressView.setVisibility(View.GONE);
//        imageButton_search.setImageResource(R.drawable.ic_search);
        isErrorConnecting = true;
        Log.e(MeaningParsedDialogFragment.class.getName(), "Unable to parse json1" +
                volleyError.toString());

    }
}
