package com.example.daveart.vocabularyapp.fragments;

import android.app.SearchManager;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.dialog_fragments.DeleteConfirmation;
import com.example.daveart.vocabularyapp.dialog_fragments.ShowWordDialogFragment;
import com.example.daveart.vocabularyapp.interfaces.RecyclerViewItemsClickLongClickListener;
import com.example.daveart.vocabularyapp.model.WordsViewType;
import com.example.daveart.vocabularyapp.utils.RecyclerItemAnimator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.dialog_fragments.LongClickBottomSheet;
import com.example.daveart.vocabularyapp.dialog_fragments.MeaningParsedDialogFragment;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.utils.AnimationsUtil;
import com.example.daveart.vocabularyapp.utils.ExceptionUtil;
import com.example.daveart.vocabularyapp.utils.OtherUtils;
import com.example.daveart.vocabularyapp.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class WordsFragment extends Fragment implements LongClickBottomSheet
        .OnButtonClickedListener, View.OnClickListener, View.OnTouchListener, TextToSpeech
        .OnInitListener, ShowWordDialogFragment.OnShareClicked, DeleteConfirmation
        .HandlingConfirmationClicks {

    private TextToSpeech textToSpeech;
    private boolean ttsInitialized;


    private RecyclerView wordFragment_recycler_id;
    private TextView textView_noWord;
    private CoordinatorLayout coordinatorLayout_word_fragment;
    private LinearLayout linearLayout_word_fragment_recycler;
    private FloatingActionButton fab_setting, fab_add, fab_edit, fab_delete;
    private View rootView;
    int clickedFab = 0;

    private static String word;

    private LinearLayoutManager layoutManager;
    public RecyclerViewAdapter recyclerViewAdapter;
//    private VocabularyLoader vocabularyLoader;
    private DataSource dataSource;
    private ExceptionUtil exceptionUtil;
    private OtherUtils otherUtils;
    private AnimationsUtil animationsUtil;
    private PreferenceUtil preferenceUtil;

    private ArrayList<Object> savedWords;
    private List<Long> itemsToDelete;
    private ArrayList<String> words;
    private int sortSelectedPosition, clickedItemPosition;
    public static float sAnimatorScale = 1;
    private long clickedItemId;
    private boolean restored = false;

//    private CustomDialogFragment customDialogFragment;
    private MeaningParsedDialogFragment meaningParsedDialogFragment;
    private DeleteConfirmation deleteConfirmation;
    private LongClickBottomSheet longClickBottomSheet;
    private SavedWord longClickedObject;
    private Snackbar snackbar;

    public WordsFragment(){
        recyclerViewAdapter = new RecyclerViewAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.words_fragment, container, false);
        setHasOptionsMenu(true);

        creatingObjects();
        inflatingViews(rootView);

//        longClickBottomSheet.setTargetFragment(this, 3);

        sortSelectedPosition = preferenceUtil.retrieveInteger(preferenceUtil
                .PREFERENCE_SORT_POSITION, -1);
        if(sortSelectedPosition == 1) {
            savedWords = dataSource.savedWords(ItemTables.TABLE_NAME, sortSelectedPosition);
        }else {
            savedWords = dataSource.wordsViewTypes();
        }

//        fab_setting.setOnClickListener(this);
        fab_setting.setOnTouchListener(this);
        fab_add.setOnClickListener(this);
//
//
//                meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance(null, 0, words, false, -1);
//                assert getFragmentManager() != null;
//                meaningParsedDialogFragment.show(getFragmentManager(), "word_fragment");
//                switchFAB(false);

        checkIfDataExists();

        addingRecyclerView();

        wordFragment_recycler_id.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(snackbar != null) {
                    snackbar.dismiss();
                    dataSource.removeItemById(clickedItemId, ItemTables.TABLE_NAME, ItemTables.COLUMN_ID);
                }

            }
        });


        recyclerViewAdapter.setRecyclerViewItemsClickLongClickListener(new RecyclerViewItemsClickLongClickListener() {
            @Override
            public void onItemClicked(int position, final View view, Object object) {
                showDialog(position, view, object);
            }

            @Override
            public void onItemLongClicked(int position, long id, Object object) {
                clickedItemId = id;
                clickedItemPosition = position;
                longClickedObject = (SavedWord) object;
                LongClickBottomSheet longClickBottomSheet = new LongClickBottomSheet()
                        .newInstance(longClickedObject.getWord());
                longClickBottomSheet.setTargetFragment(
                        WordsFragment.this, 3);
                assert getFragmentManager() != null;
                longClickBottomSheet.show(getFragmentManager(), "Long Clicked Bottom Sheet");
//                performDeletion();
            }
        });
//        recyclerViewAdapter.setDeleteListener(new RecyclerViewAdapter.deleteListener() {
//            @Override
//            public void onItemClicked(int adapterPosition, View view, Object){
//
//                showDialog(adapterPosition, view);
//            }
//            @Override
//            public void onItemLongClicked(int adapterPosition, long id, SavedWord savedWord){
//                clickedItemId = id;
//                clickedItemPosition = adapterPosition;
//                longClickedObject = savedWord;
//                assert getFragmentManager() != null;
//                longClickBottomSheet.show(getFragmentManager(), "Long Clicked Bottom Sheet");
//            }
//        });

        return rootView;

    }

    private void showFabChangeImage(boolean show, int imageRs) {

        if(show) {
            fab_edit.show();
            fab_delete.show();
            fab_add.show();

            OvershootInterpolator overshootInterpolator = new OvershootInterpolator(3f);
            fab_edit.animate().setInterpolator(overshootInterpolator).scaleY(1f).scaleX(1f);
            fab_add.animate().setInterpolator(overshootInterpolator).scaleY(1f).scaleX(1f);
            fab_delete.animate().setInterpolator(overshootInterpolator).scaleY(1f).scaleX(1f);
        }else {

            fab_edit.hide();
            fab_delete.hide();
            fab_add.hide();

        }
//        layoutParams.dimAmount = 0.5f;
        fab_setting.setImageResource(imageRs);
    }

    private void inflatingViews(View rootView) {
        wordFragment_recycler_id = rootView.findViewById(R.id.wordFragment_recycler);
        textView_noWord = rootView.findViewById(R.id.textView_noWords_word_fragment_recycler);
        coordinatorLayout_word_fragment = rootView.findViewById(R.id.coordinator_word_fragment);
        linearLayout_word_fragment_recycler = rootView.findViewById(R.id
                .linearLayout_word_fragment_recycler);
        fab_setting = rootView.findViewById(R.id.fab_options);
        fab_add = rootView.findViewById(R.id.fab_add);
        fab_delete = rootView.findViewById(R.id.fab_delete);
        fab_edit = rootView.findViewById(R.id.fab_edit);

    }

    private void creatingObjects() {

        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(), this);
        dataSource = new DataSource(getContext());
//        vocabularyLoader = new VocabularyLoader(getContext());
        exceptionUtil = new ExceptionUtil(getContext());
        otherUtils = new OtherUtils(getContext());
        animationsUtil = new AnimationsUtil(getContext());
        preferenceUtil = new PreferenceUtil(getContext());
//        longClickBottomSheet = new LongClickBottomSheet();
        deleteConfirmation = new DeleteConfirmation();
        deleteConfirmation.setTargetFragment(this, 5);

//        customDialogFragment = new CustomDialogFragment();

        itemsToDelete = new ArrayList<>();
        words = dataSource.getAllWords();

    }

    private void performDeletion(){
//        recyclerViewAdapter.notifyItemChanged(clickedItemPosition);
        recyclerViewAdapter.notifyItemRemoved(clickedItemPosition);
        recyclerViewAdapter.notifyItemRangeChanged(clickedItemPosition, 2);
        savedWords.remove(clickedItemPosition);
        // TODO: deleted word is removed from the list, but only when the dialog is initialized
        // from this fragment, if its from cached words fragment, it still exists
//        words.remove(longClickedObject.getWord());
//        itemsToDelete.add(clickedItemId);
        checkIfDataExists();

        customizedSnackBar("Deleted", Snackbar.LENGTH_INDEFINITE).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performUndo();
            }

        }).show();

        }

    private void performUndo(){

        restored = true;
        recyclerViewAdapter.objectList.add(clickedItemPosition, longClickedObject);
        recyclerViewAdapter.notifyItemInserted(clickedItemPosition);
        recyclerViewAdapter.notifyItemRangeChanged(clickedItemPosition, 2);
//        itemsToDelete.remove(itemsToDelete.size()-1);
//        words.add(clickedItemPosition, longClickedObject.getWord());
        Snackbar.make(coordinatorLayout_word_fragment, "RESTORED", Snackbar.LENGTH_LONG).show();

    }

    private void checkIfDataExists(){
        if(exceptionUtil.checkIfDataExists(ItemTables.TABLE_NAME, ItemTables.COLUMN_TIMESTAMP)){
            textView_noWord.setVisibility(View.GONE);
            fab_setting.startAnimation(animationsUtil.customAnimation(R.anim.slide_in_right));

        }else {
            fab_setting.startAnimation(animationsUtil.customAnimation(R.anim.repeated_scale));
            textView_noWord.setVisibility(View.VISIBLE);
        }

    }

    private void showDialog(int adapterPosition, View view, Object object){

        SavedWord savedWord;
        if(object instanceof SavedWord) {
            savedWord = (SavedWord) savedWords.get(adapterPosition);
        }else {
            WordsViewType wordsViewType = (WordsViewType) object;
            savedWord = wordsViewType.getSavedWord();
        }

//        Date date = new Date();
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar1 = Calendar.getInstance();
//
//        String format = "yyyy-MM-dd hh:mm:ss";
//
//        Date date1 = null;
//        try {
//            date1 = new SimpleDateFormat(format, getCurrentLocale()).parse(savedWord.getTimestamp());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if(date1 != null) {
//            calendar.setTime(date);
//            calendar1.setTime(date1);
//
//            if(calendar1.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
//                calendar.add(Calendar.DATE, -calendar1.get(Calendar.DATE));
//                Toast.makeText(getContext(), calendar.get(Calendar.DATE) + " days.", Toast
//                        .LENGTH_SHORT).show();
//            } else {
////                calendar.add(Calendar.DATE, -calendar1.get(Calendar.DATE));
////                Toast.makeText(getContext(), calendar.get(Calendar.DATE) + " days.", Toast
////                        .LENGTH_SHORT).show();
//                calendar.add(Calendar.DAY_OF_YEAR, -calendar1.get(Calendar.DAY_OF_YEAR));
//                Toast.makeText(getContext(), calendar.get(Calendar.DAY_OF_YEAR) + " days.", Toast.LENGTH_SHORT)
//                        .show();
//
//            }
////            calendar.add(Calendar.DAY_OF_YEAR, -calendar1.get(Calendar.DAY_OF_YEAR));
////            int diff = calendar.get(Calendar.) - calendar1.get(Calendar.DATE);
//
////            Toast.makeText(getContext(), calendar.get(Calendar.DAY_OF_YEAR) + " " + calendar1.get
////                            (Calendar.DAY_OF_YEAR), Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(getContext(), "couldn't parse", Toast.LENGTH_SHORT).show();
//        }


//        pronunceTheWord(savedWord.getWord());
        ShowWordDialogFragment showWordDialogFragment = new ShowWordDialogFragment().newInstance(savedWord);
        showWordDialogFragment.setTargetFragment(this, 2);
        showWordDialogFragment.show(getFragmentManager(), "");

//        assert getContext() != null;
//        Dialog dialog = new Dialog(getContext());
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.show_word);
//
//        Toast.makeText(getActivity(), "Reached", Toast.LENGTH_SHORT).show();
//
//
//        String word = savedWord.getWord();
//        String meaning = savedWord.getMeaning();
//        String wordType = savedWord.getWordType();
//        int orientation = getResources().getConfiguration().orientation;
//
//        int[] screenLocation = new int[2];
//        view.getLocationOnScreen(screenLocation);
//
//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//
//        intent.putExtra("word", word);
//        intent.putExtra("meaning", meaning);
//        intent.putExtra("wordType", wordType);
//        intent.putExtra("viewWidth", view.getWidth());
//        intent.putExtra("viewHeight", view.getHeight());
//        intent.putExtra("orientation", orientation);
//        intent.putExtra("left", screenLocation[0]);
//        intent.putExtra("top", screenLocation[1]);
//
//        startActivity(intent);

//        dialog.getWindow().setWindowAnimations(R.style.popUpScaleAnimation);
//
//        TextView wordTextView = dialog.findViewById(R.id.textView_showWord_word);
//        TextView meaningTextView = dialog.findViewById(R.id.textView_showWord_meaning);
//        TextView wordTypeTextView = dialog.findViewById(R.id.textView_showWord_wordType);
//
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.visibility_animation);

//        wordTextView.startAnimation(animation);
//        meaningTextView.startAnimation(animation);
//
//        wordTextView.setText(word);
//        meaningTextView.setText(meaning);
//        wordTypeTextView.setText(wordType);
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//
    }

    private Snackbar customizedSnackBar(String msg, int length){

        snackbar = Snackbar.make(coordinatorLayout_word_fragment, msg, length);
        snackbar.setActionTextColor(otherUtils.accessingStyleableColor(R.styleable.ds_iconColor));
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(otherUtils.accessingStyleableColor(R.styleable.ds_textColor));
        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(otherUtils.accessingStyleableColor(R.styleable.ds_cardBackground));
        return snackbar;

    }

    private void addingRecyclerView(){

        wordFragment_recycler_id.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
//        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), savedWords,
//                sortSelectedPosition);
        recyclerViewAdapter = new RecyclerViewAdapter(savedWords);
        wordFragment_recycler_id.setItemAnimator(new RecyclerItemAnimator());
//        wordFragment_recycler_id.setLayoutManager(new StaggeredGridLayoutManager(2,
//                StaggeredGridLayoutManager.VERTICAL));
        wordFragment_recycler_id.setLayoutManager(new LinearLayoutManager(getActivity()));
        wordFragment_recycler_id.setAdapter(recyclerViewAdapter);

//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(wordFragment_recycler_id);

        runAnimation(wordFragment_recycler_id);

    }

    public void switchFAB(boolean enabled){
        fab_setting.setEnabled(enabled);
    }

    private void runAnimation(RecyclerView wordFragment_recycler_id) {

        Context context = wordFragment_recycler_id.getContext();
        LayoutAnimationController layoutAnimationController;

        layoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim
                .layout_fall_down);
        wordFragment_recycler_id.setLayoutAnimation(layoutAnimationController);
        wordFragment_recycler_id.getAdapter().notifyDataSetChanged();
        wordFragment_recycler_id.scheduleLayoutAnimation();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        ArrayList<Object> savedWords = dataSource.savedWords(ItemTables.TABLE_NAME, 1);
//        recyclerViewAdapter = new RecyclerViewAdapter(savedWords);
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context
                .SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerViewAdapter.getFilter().filter(s);
                Log.i("FilterPattern1", s);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkConditions(SavedWord savedWord, int position, boolean isDeleted, String
            cachedWord, boolean isCheckBoxChecked, boolean isEdited, int sortPreference) {

        // TODO: the insertion doesn't consider the fact that I might be using the alphabetical
        // order, that's why it is not notifying the adapter to make a change whenever I add or
        // delete, or edit
        dataSource.open();
        long id = -1;

        if(!isEdited) {

            id = dataSource.insertData(dataSource.checkIfExists(savedWord), dataSource.checkEmpty
                            (savedWord), dataSource.checkNumber(savedWord), savedWord, position, isDeleted);

            if(id != -1){
                if(cachedWord != null){
                    if(isCheckBoxChecked){
                        dataSource.removeItemByName(cachedWord, ItemTables.TEMP_WORDS_TABLE, ItemTables.TEMP_COLUMN_WORD);
                    }

                }else {
                    if (isDeleted) {
                        Snackbar.make(coordinatorLayout_word_fragment, "RESTORED", Snackbar.LENGTH_LONG).show();
                    } else {
                        savedWord = (SavedWord) dataSource.getSingleData(id, ItemTables.TABLE_NAME);
//                        recyclerViewAdapter.notifyItemRangeChanged(position, 3);
//                        wordFragment_recycler_id.smoothScrollToPosition(0);
                        checkIfDataExists();
                        fab_setting.setEnabled(true);

                    }
                }

                // TODO: sometimes it doesn't show the Recently Added tag, but the first letter
                // of the word
                /* TODO: the words under the recently added header disperses when another
                 activity is opened, the solution might be
                    - to bundle the words and take them to the destination activity
                    - then when the user gets back to this class, the class will check of hte
                    words existence and displays them as they should be under the recently added
                    header
                */

                if(sortPreference == 0) {
                    if(savedWords.size() == 0) {
                        WordsViewType wordsViewType = new WordsViewType("Recently Added");
                        savedWords.add(0, wordsViewType);
//                        savedWords.add(1, wordsViewType1);
                    }else {
                        WordsViewType wordsViewType = (WordsViewType) savedWords.get(0);
                        if(!wordsViewType.getFirstLetter().equals("Recently Added")) {
                            WordsViewType wordsViewType1 = new WordsViewType("Recently Added");
                            savedWords.add(0, wordsViewType1);
                            recyclerViewAdapter.notifyItemInserted(0);

                        }
                    }
//                }else {
                    savedWords.add(1, savedWord);
                    recyclerViewAdapter.notifyItemInserted(1);
                } else {
                    savedWords.add(0, savedWord);
                    recyclerViewAdapter.notifyItemInserted(0);
                }
//                recyclerViewAdapter.notifyItemInserted(position);

            }else {

                Toast.makeText(getContext(), "Couldn't be saved.", Toast.LENGTH_SHORT).show();

            }

        } else {

            // TODO: unable to notify the adapter of the change happening when the words are
            // sorted alphabetically
            dataSource.updateItem(savedWord);
            savedWords.set(clickedItemPosition, savedWord);
//            recyclerViewAdapter.insertData(savedWords);
//            Toast.makeText(getContext(), "Editing", Toast.LENGTH_SHORT).show();
            recyclerViewAdapter.notifyDataSetChanged();
//            recyclerViewAdapter.notifyItemRemoved(clickedItemPosition);
//            recyclerViewAdapter.notifyItemInserted(clickedItemPosition);
//            recyclerViewAdapter.getObjectList().set(pos+ition, savedWord);
//            recyclerViewAdapter.notifyItemRangeChanged(position-1, 3);
//            recyclerViewAdapter.notifyItemChanged(position);
        }

        return id != -1;

    }

    public void dismissWhenScroll(){
        if(wordFragment_recycler_id.isInTouchMode()) {
            deleteConfirmation.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(itemsToDelete.size() > 0){
//            for (int i = 0; i < itemsToDelete.size(); i++) {
//                dataSource.removeItemById(itemsToDelete.get(i), ItemTables.TABLE_NAME, ItemTables.COLUMN_ID);
//            }
//        }
//        dataSource.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataSource.close();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDeleteClicked() {

        deleteConfirmation.show(getActivity().getSupportFragmentManager(), "delete confirmation " +
                "dialog");
//        performDeletion();
//        performUndo();
//        Toast.makeText(getActivity(), "Delete Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEditClicked() {
        MeaningParsedDialogFragment meaningParsedDialogFragment = new MeaningParsedDialogFragment
                ().newInstance(longClickedObject.getWord(), clickedItemPosition, null, true, clickedItemId);
        meaningParsedDialogFragment.show(getActivity().getSupportFragmentManager(),
                "word_fragment");
//        Toast.makeText(getActivity(), "Edit Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRelatedWordsClicked() {
        Toast.makeText(getActivity(), "Related Words Clicked", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab_options:
//                if(clickedFab == 0) {
//                    showFabChangeImage(true, R.drawable.delete_48px_light_grey);
//                    clickedFab = 1;
//                } else {
//                    showFabChangeImage(false, R.drawable.ic_settings_black_24dp);
//                    clickedFab = 0;
//                }
                break;
            case R.id.fab_add:
                meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance(null, 0, words, false, -1);
                assert getFragmentManager() != null;
                meaningParsedDialogFragment.show(getFragmentManager(), "word_fragment");
                switchFAB(false);
                showFabChangeImage(false, R.drawable.ic_settings_black_24dp);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            v.animate().setInterpolator(new AccelerateInterpolator())
                    .scaleX(.8f).scaleY(.8f);
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            getActivity().getWindow().setDimAmount(0.5f);
//            getActivity().setTheme(R.style.darkTheme);
            if(clickedFab == 0) {
                showFabChangeImage(true, R.drawable.delete_48px_light_grey);
                clickedFab = 1;
            } else {
                showFabChangeImage(false, R.drawable.ic_settings_black_24dp);
                clickedFab = 0;
            }
            v.animate().setInterpolator(new OvershootInterpolator(5f))
                    .scaleX(1f).scaleY(1f);
        }
        return true;
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech
                    .LANG_NOT_SUPPORTED) {
                Toast.makeText(getActivity(), "This language is not supported", Toast.LENGTH_SHORT).show();
            } else {
                ttsInitialized = true;
            }

        } else {
            Toast.makeText(getActivity(), "initialization failed", Toast.LENGTH_SHORT).show();
        }


    }

    private void pronunceTheWord(String word) {
        if(!ttsInitialized) {
            Toast.makeText(getActivity(), "wasn't initialized", Toast.LENGTH_SHORT).show();
            return;
        }

        textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    public void shareClicked(SpannableStringBuilder data) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, data);

        intent.setType("text/plain");
        startActivity(intent);

    }

    @Override
    public void pronunceClicked(String word) {
        pronunceTheWord(word);
    }

    @Override
    public void onYesClicked() {
        performDeletion();
        deleteConfirmation.dismiss();
    }

    @Override
    public void onNoClidked() {
        deleteConfirmation.dismiss();
    }
}
