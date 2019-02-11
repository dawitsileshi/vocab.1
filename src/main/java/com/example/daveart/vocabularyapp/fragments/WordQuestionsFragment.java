package com.example.daveart.vocabularyapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.activities.MainActivity;
import com.example.daveart.vocabularyapp.activities.QuizStartPage;
import com.example.daveart.vocabularyapp.model.Quiz;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.data_handlers.InternalFile;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.dialog_fragments.ExitDialogFragment;
import com.example.daveart.vocabularyapp.dialog_fragments.ResultDialog;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.utils.AnimationsUtil;
import com.example.daveart.vocabularyapp.utils.OtherUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WordQuestionsFragment extends Fragment implements View.OnClickListener, ResultDialog
        .actionListener, ExitDialogFragment.ExitInterface {

    private DataSource dataSource;
    private Quiz quiz;

    // Dialogs
    private ResultDialog resultDialog;
    private ExitDialogFragment exitDialogFragment;

    private CountDownTimer countDownTimer;

    // Classes from Utils package
    private OtherUtils otherUtils;
    private AnimationsUtil animationsUtil;

    private InternalFile internalFile;

    // Variables
    private static final String QUESTION_TYPE = "QuestionType";
    public static final String MEMORIZED_WORDS = "MemorizedWords";
    private String question, questionType, choiceType, singleChoice;
    private int questionNumber = 0, correctAnswerTag, correctAnswer, score = 0, noOfQuestions;
    private boolean answered = false, finished = false;
    private long maxMilliSeconds = 25000, milliSecondsLeft = maxMilliSeconds;
    private long totalMill = 0, currentMilli = 0;

    // Views
    private TextView questionNumber_textViewId, question_textViewId;
    private Button nextOrStartOrFinish;
    private ImageButton imageButton_pause;
    private RadioButton choiceA_radioButtonId, choiceB_radioButtonId, choiceC_radioButtonId;
    private RadioButton[] radioButtons;
    private Toolbar toolbar;
    private CardView cardView_choiceA, cardView_choiceB, cardView_choiceC;
    private ProgressBar timer_progressBar;
//    RadioGroup radioGroup;

    // Data Structure
    private List<Object> savedWordList;
    private ArrayList<Integer> questionArrayList;
    private ArrayList<Integer> choiceArrayList;
//    private

//    private MissedQuestion missedQuestion;

    private View rootView;

    public WordQuestionsFragment newInstance(String questionType, ArrayList<Integer>
            memorizedWords){
        WordQuestionsFragment wordQuestionsFragment = new WordQuestionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION_TYPE, questionType);
        bundle.putIntegerArrayList(MEMORIZED_WORDS, memorizedWords);
        wordQuestionsFragment.setArguments(bundle);
        return wordQuestionsFragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_meaning_questions, container, false);

        findingViewsById();

        if(getArguments() != null) {
            questionType = getArguments().getString(QUESTION_TYPE);
        }else{
            throw new NullPointerException(QUESTION_TYPE + " was not passed.");
        }
        otherUtils = new OtherUtils(getContext());
//        missedQuestion = new MissedQuestion();
        internalFile = new InternalFile(getContext());
        animationsUtil = new AnimationsUtil(getContext());

        exitDialogFragment = new ExitDialogFragment();
        exitDialogFragment.setTargetFragment(this, 1);

        dataSource = new DataSource(getContext());
        quiz = new Quiz();
//        dataSource.removeAllFromTable(ItemTables.RESULT_TABLE);
        savedWordList = dataSource.savedWords(ItemTables.TABLE_NAME, 1); // assigning the arraylist
        // of the database files to the arraylist called 'savedWordList'

        setProgressValue(maxMilliSeconds);

        radioButtons = new RadioButton[]{choiceA_radioButtonId, choiceB_radioButtonId,
                choiceC_radioButtonId}; // storing the three radioButtons in an array to assign
        // them a value in iteration

        checkData();

        startEverything();
        imageButton_pause.setOnClickListener(this);
        choiceA_radioButtonId.setOnClickListener(this);
        choiceB_radioButtonId.setOnClickListener(this);
        choiceC_radioButtonId.setOnClickListener(this);

        ((QuizStartPage)getActivity()).setiOnBackPressed(new QuizStartPage.IOnBackPressed() {
            @Override
            public boolean onBackPressed() {
                exitDialogFragment.show(getActivity().getSupportFragmentManager(), "exit_fragment");
                countDownTimer.cancel();
                return true;
            }
        });
        return rootView;
    }

    private void startEverything(){
        nextOrStartOrFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!answered){
                    if(radioButtons[0].isChecked() || radioButtons[1].isChecked() ||
                            radioButtons[2].isChecked()){

                        checkAnswer(correctAnswer);
                    }else {

                        shakeAnimation(cardView_choiceA);
                        shakeAnimation(cardView_choiceB);
                        shakeAnimation(cardView_choiceC);
                        Toast.makeText(getContext(), "Please answer the question", Toast
                                .LENGTH_SHORT).show();
                    }


                }else{

                    startQuestion();

                }

            }
        });
    }

    private void findingViewsById(){

        toolbar = rootView.findViewById(R.id.toolBar_quiz);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Word Quiz");
        int color = this.getResources().getColor(android.R.color.white);
        toolbar.setTitleTextColor(color);

        questionNumber_textViewId = rootView.findViewById(R.id.textView_quiz_questionNumber);
        question_textViewId = rootView.findViewById(R.id.textView_quiz_question);

        imageButton_pause = rootView.findViewById(R.id.imageButton_quiz_pause);
        choiceA_radioButtonId = rootView.findViewById(R.id.radioButton_quiz_choiceA);
        choiceB_radioButtonId = rootView.findViewById(R.id.radioButton_quiz_choiceB);
        choiceC_radioButtonId = rootView.findViewById(R.id.radioButton_quiz_choiceC);
//        radioGroup = findViewById(R.id.choiceGroup);

        nextOrStartOrFinish = rootView.findViewById(R.id.button_quiz_next_done);

        timer_progressBar = rootView.findViewById(R.id.progressBar_quiz_timer);

        cardView_choiceA = rootView.findViewById(R.id.cardView_quiz_choiceAContainer);
        cardView_choiceB = rootView.findViewById(R.id.cardView_quiz_choiceBContainer);
        cardView_choiceC = rootView.findViewById(R.id.cardView_quiz_choiceCContainer);

    }

    private void shakeAnimation(CardView cardView){
        YoYo.with(Techniques.Shake).playOn(cardView);
    }

    private void setProgressValue(long l) {

        timer_progressBar.setProgress((int) l / 1000);
        timer_progressBar.setMax((int) l / 1000);

    }

    private void checkData() {

        if (zeroCursor()) { // calling the function that checks whether there is a data in the db

            noQuestion(); // if so this function is called

        } else {

            resetEverything();

            startQuestion(); // if not this method is called

        }
    }

    private boolean zeroCursor() {
        return savedWordList.size() < 3; // returns true if the size
        // of the arrayList is zero
    }

    private void noQuestion() {

//        radioGroup.setVisibility(View.GONE);
        visibilityChange(View.GONE);
        nextOrStartOrFinish.setText(R.string.home);
        Log.i("Checking4 method", "called from line 245");
        showResult();
        nextOrStartOrFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

    }

    private void visibilityChange(int visibilityValue){

        cardView_choiceA.setVisibility(visibilityValue);
        cardView_choiceB.setVisibility(visibilityValue);
        cardView_choiceC.setVisibility(visibilityValue);

    }

    private void animatingCards(){

        cardView_choiceB.startAnimation(animationsUtil.customAnimation(R.anim.slide_in_right));
        cardView_choiceA.startAnimation(animationsUtil.customAnimation(R.anim.slide_in_bottom));
        cardView_choiceC.startAnimation(animationsUtil.customAnimation(R.anim.slide_in_top));

    }

    private int chooseSize() {
        if(checkIfMemorized() != null) {
            return checkIfMemorized().size();
        }else {
            return 5;
        }
    }

    private void startQuestion() {

//        if(finished){
//            resetEverything();
//        }
        checkUncheckRadioButtons(false, false, false);

        int color = otherUtils.accessingStyleableColor(R.styleable.ds_textColor);
        settingPropertiesToRadio(color, color, color, android.R.drawable.btn_radio,
                android.R.drawable.btn_radio, android.R.drawable.btn_radio);

//        if(savedWordList.size() < 5){
//            noOfQuestions = savedWordList.size();
        if(chooseSize() != 5) {
            quiz.setNumberOfQuestion(chooseSize());
            noOfQuestions = chooseSize();
        }else{
            quiz.setNumberOfQuestion(5);
            noOfQuestions = 5;
        }
        if(noOfQuestions == questionNumber){ // comparison is with equal sign because questionNumber starts
            // with 0
            quiz.setAnswered(false);
            answered = false;
            settingText("Done!", "Result", "Finished");
            dataSource.checkFrequency(ItemTables.COLUMN_WRONGLY_ANSWERED);
            dataSource.checkFrequency(ItemTables.COLUMN_CORRECTLY_ANSWERED);
            Log.i("Checking3 method", "called from line 290");
            showResult();
            visibilityChange(View.GONE);

        } else {

            milliSecondsLeft = maxMilliSeconds;
            startTimer();

            int index = questionArrayList.get(questionNumber); // assigns a value of
            // questionArrayList with an index of questionNumber
            Toast.makeText(getContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
            correctAnswerTag = randomNumberGenerator(3); // this is where the right answer has
            // to be placed in the choice

            if(questionType.equals(ItemTables.COLUMN_WORD)){
                SavedWord savedWord = (SavedWord) savedWordList.get(index);
                quiz.setQuestion(savedWord.getWord());
                question = savedWord.getWord(); // a question from the savedWordList
                // using an index of 'index' variable
                choiceType = ItemTables.COLUMN_MEANING;
            }else{
                SavedWord savedWord = (SavedWord) savedWordList.get(index);
                quiz.setQuestion(savedWord.getMeaning());
                question = savedWord.getMeaning(); // a question from the savedWordList
                // using an index of 'index' variable
                choiceType = ItemTables.COLUMN_WORD;
            }


            generateChoices(radioButtons, index, correctAnswerTag); // else we generate the choices
            settingText("Q. " + question, "Confirm", String.valueOf(questionNumber + 1));
            animatingCards();
            quiz.setAnswered(false);
            answered = false;
            quiz.setQuestionNumber(quiz.getQuestionNumber()+1);
            questionNumber++; // increments as the user taps the button
        }
    }

    private void resetEverything(){
        quiz.setQuestionNumber(0);
        questionNumber = 0;
        maxMilliSeconds = 25000;
        milliSecondsLeft = maxMilliSeconds;
        totalMill = 0;
        currentMilli = 0;
        visibilityChange(View.VISIBLE);
        quiz.setAnswered(false);
        quiz.setFinished(false);
        finished = false;
        answered = false;
        quiz.setScore(0);
        score = 0;
//        countDownTimer.start();
//        startTimer();
//        questionArrayList = questionArrayList(); // assigning questionArrayList to the arraylist
        // returned from the function questionArrayList()
        questionArrayList = questionArrayList();
        choiceArrayList = choiceArrayList();
    }

    private void checkUncheckRadioButtons(boolean radio1, boolean radio2, boolean radio3){

        choiceA_radioButtonId.setChecked(radio1);
        choiceB_radioButtonId.setChecked(radio2);
        choiceC_radioButtonId.setChecked(radio3);

    }

    private void showResult() {

//        Toast.makeText(this, String.valueOf(internalFile.readFromFile()), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "The size is " + String.valueOf(dataSource.toArrayList
                (ItemTables.COLUMN_HARD_WORDS)
                        .size()), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < dataSource.toArrayList(ItemTables.COLUMN_HARD_WORDS).size(); i++) {

            Toast.makeText(getContext(), String.valueOf(dataSource.toArrayList(ItemTables.COLUMN_HARD_WORDS).get(i)),
                    Toast.LENGTH_SHORT).show();

        }
//        missedQuestions = internalFile.toArrayList();
        Log.i("Checking method", "reached here");
        finished = true;
        final String scoreString = String.valueOf(score) + "/" + String.valueOf(questionNumber);
        final String seconds = String.valueOf(Math.round(totalMill / 1000) + " sec" );
//        resultDialog.noOfSeconds_textView.append(String.valueOf(Math.round(totalMill / 1000)) + "" +
//                " sec");
        nextOrStartOrFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Checking method2", "reached here");
                resultDialog = new ResultDialog().newInstance(scoreString, seconds);
                resultDialog.setTargetFragment(WordQuestionsFragment.this, 0);
                resultDialog.show(getActivity().getSupportFragmentManager(), "Result Fragment");
            }
        });

    }

    private void checkAnswer(int correctAnswer) {

        answered = true;
        if(radioButtons[correctAnswer].isChecked()){

            score++;

            dataSource.insertToResultDb(true, questionArrayList.get(questionNumber - 1));

            setTextAndAnimate(R.string.correct);

//            showPopUp("Correct", R.drawable.ic_check_larger);

        }else{

            dataSource.insertToResultDb(false, questionArrayList.get(questionNumber - 1));

//            internalFile.createAndStore(String.valueOf(questionArrayList.get(questionNumber - 1)));

            setTextAndAnimate(R.string.incorrect);
//            showPopUp("Incorrect", R.drawable.ic_close_larger);

        }
        showSolution();
    }

    private void setTextAndAnimate(int msg){

        questionNumber_textViewId.setText(msg);

        questionNumber_textViewId.startAnimation(animationsUtil.customAnimation(R.anim.bounce));

    }

    private void showSolution() {

        int color = otherUtils.accessingStyleableColor(R.styleable.ds_textColor);

        if(correctAnswer == 0){

            settingPropertiesToRadio(color, R.color.red, R.color.red,
                    R.drawable.correct_radio_checked, R.drawable.incorrect_radio_checked,
                    R.drawable.incorrect_radio_checked);

        }else if(correctAnswer == 1) {

            settingPropertiesToRadio(R.color.red, color, R.color.red,
                    R.drawable.incorrect_radio_checked, R.drawable.correct_radio_checked,
                    R.drawable.incorrect_radio_checked);

        }else{

            settingPropertiesToRadio(R.color.red, R.color.red,
                    color, R.drawable.incorrect_radio_checked, R.drawable.incorrect_radio_checked,
                    R.drawable.correct_radio_checked);

        }

        totalMill = totalMill + currentMilli;
        countDownTimer.cancel();
        nextOrStartOrFinish.setText(R.string.next);

    }

    private void settingPropertiesToRadio(int color1, int color2, int color3, int
            correctButtonDrawable, int incorrectButtonDrawable, int incorrectButtonDrawable1) {

        radioButtons[0].setTextColor(color1);
        radioButtons[1].setTextColor(color2);
        radioButtons[2].setTextColor(color3);

        radioButtons[0].setButtonDrawable(correctButtonDrawable);
        radioButtons[1].setButtonDrawable(incorrectButtonDrawable);
        radioButtons[2].setButtonDrawable(incorrectButtonDrawable1);

    }


    private void generateChoices(RadioButton[] radioButtons, int index, int correctAnswerTag) {

        // put in the choice
//        ArrayList<Integer> arrayList = choiceArrayList(); // assigning the value of the arraylist
        // returned from the choiceArrayList() method

        if(!choiceArrayList.contains(index)){ // checks if the index value is, (which is the where the
            // question is found in savedWordList) contained in the arrayList 'arrayList'

            choiceArrayList.set(correctAnswerTag, index); // if not, then we replace the value of the
            // arrayList found in the correctAnswerTag index with the index value, so that the
            // answer will be in the choice
        }


        correctAnswerTag = choiceArrayList.indexOf(index);
        correctAnswer = correctAnswerTag;

        for (int j = 0; j < 3; j++) {
            SavedWord savedWord = (SavedWord) savedWordList.get(choiceArrayList.get(j));
            String wordType = savedWord.getWordType();
            if(choiceType.equals(ItemTables.COLUMN_WORD)){
                singleChoice = savedWord.getWord() + "(" + wordType + ")";
            }else{
                singleChoice = savedWord.getMeaning() + "(" + wordType + ")";
            }
            radioButtons[j].setText(singleChoice); // then we
            // setText on the radioButtons using the value of the arrayList

        }

    }

    private void settingText(String question, String buttonText, String questionNumber){

        nextOrStartOrFinish.setText(buttonText);

        questionNumber_textViewId.setText(questionNumber);
        question_textViewId.setText("");
        question_textViewId.append(question);

    }

    private Integer randomNumberGenerator(int bound){

        return new Random().nextInt(bound);

    }

    private ArrayList<Integer> checkIfMemorized() {
        if(getArguments().getIntegerArrayList(MEMORIZED_WORDS) != null) {
            return getArguments().getIntegerArrayList(MEMORIZED_WORDS);
        }
        return null;
    }

    private ArrayList<Integer> questionArrayList(){ // this function returns an arrayList that
        // contains shuffled order of the data

        ArrayList<Integer> arrayList = new ArrayList<>();
        // TODO: the next loop has to be removed if a crash happens in the code
        if(checkIfMemorized() != null) {
            arrayList.addAll(checkIfMemorized());
        }else {
            for (int i = 0; i < savedWordList.size(); i++) {
                arrayList.add(i); // adds the value of i into the arrayList, which in this case
                // is just ordered numbers
                Log.i("Size", String.valueOf(savedWordList.size()));
            }
        Collections.shuffle(arrayList); // then we shuffle the numbers, this is alternative to
            // random generation of numbers but with no duplication.
            }


        return arrayList;

    }

    private ArrayList<Integer> choiceArrayList(){ // also returns an arrayList that contains three
        // numbers to use on choices

        ArrayList<Integer> choiceArrayList = new ArrayList<>();
//        ArrayList<Integer> questionArrayList = questionArrayList();
        ArrayList<Integer> questionArrayList = questionArrayList();

        int randomNumber;
        if(checkIfMemorized() != null) {
            randomNumber = randomNumberGenerator(checkIfMemorized().size() - 2);
        }else {
            randomNumber = randomNumberGenerator(savedWordList.size() - 2); // generating rand num,
        }
        // 2 is subtracted from it so that it wont cause IndexOutOfBoundsException when we
        // increment it
//        if(checkIfMemorized() == null) {
            for (int i = 0; i < 3; i++) {

                choiceArrayList.add(questionArrayList.get(randomNumber)); // value of the
                // questionArrayList is added to the choiceArrayList starting from index of the value
                // of randomNumber

                randomNumber++;

            }
            // TODO: the next loop also has to be removed if a crash happens in the code
//        }else {
//            for (int i = 0; i < checkIfMemorized().size(); i++) {
//                choiceArrayList.add(questionArrayList.get(i));
//            }
//        }

        return choiceArrayList;
    }

    // the following overrides are for the result dialog fragment

    @Override
    public void onSaveClicked() {
        if(finished){
            resultDialog.dismiss();
        }else {
            resultDialog.dismiss();
            startTimer();

        }
    }

    @Override
    public void onRestartClicked(ResultDialog resultDialog) {
        resultDialog.dismiss();
        resetEverything();
        startEverything();
        startQuestion();

        // TODO: problem occurs after the quiz restarts

    }

    @Override
    public void onExitClicked(ResultDialog resultDialog) {
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();

    }

    private void startTimer(){

        countDownTimer = new CountDownTimer(milliSecondsLeft + 100, 1000) {
            @Override
            public void onTick(long l) {
                milliSecondsLeft = l;
                timer_progressBar.setProgress((int) l / 1000);
                currentMilli = maxMilliSeconds - l;
            }

            @Override
            public void onFinish() {
                currentMilli = maxMilliSeconds;

                setTextAndAnimate(R.string.time_up);
                dataSource.insertToResultDb(false, questionArrayList.get(questionNumber - 1));
//                Toast.makeText(getContext(), "Timer has finished", Toast.LENGTH_SHORT)
//                        .show();
                showSolution();
                answered = true;
                timer_progressBar.setProgress(0);
            }
        }.start();
    }

    // the following two overrides are for exit dialog fragment

    @Override
    public void onYesClicked() {
        startActivity(new Intent(getActivity(), QuizStartPage.class));
        getActivity().finish();
        countDownTimer.cancel();
    }

    @Override
    public void onNoClicked() {
        exitDialogFragment.dismiss();
        startTimer();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.radioButton_quiz_choiceA:
                checkUncheckRadioButtons(true, false, false);
                break;
            case R.id.radioButton_quiz_choiceB:
                checkUncheckRadioButtons(false, true, false);
                break;
            case R.id.radioButton_quiz_choiceC:
                checkUncheckRadioButtons(false, false, true);
                break;
            case R.id.imageButton_quiz_pause:
                countDownTimer.cancel();
                Log.i("Checking method1", "reached here");
                ResultDialog resultDialog = new ResultDialog().newInstance(null, null);
                resultDialog.setTargetFragment(WordQuestionsFragment.this, 0);
                resultDialog.show(getFragmentManager(), "result_dialog");
        }
    }

//    private void showPopUp(String message, int image_id){
//
//        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
//                .LAYOUT_INFLATER_SERVICE);
//        assert inflater != null;
//        final View popupView = inflater.inflate(R.layout.check_answer, null);
//        TextView textView_message = popupView.findViewById(R.id.textVie_checkAnswer);
//        ImageView imageView_image = popupView.findViewById(R.id.imageView_checkAnswer);
//        textView_message.setText(message);
//        imageView_image.setImageResource(image_id);
//        int width = LinearLayout.LayoutParams.MATCH_PARENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
//        popupWindow.setAnimationStyle(R.style.checkAnswerAnimation);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
//
//        final Handler handler = new Handler();
//        final Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                if(popupWindow.isShowing()){
//                    popupWindow.dismiss();
//                }
//            }
//        };
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                handler.removeCallbacks(runnable);
//            }
//        });
//
//        handler.postDelayed(runnable, 2000);
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(isRemoving()){
            exitDialogFragment.show(getFragmentManager(), "exit_dialog");
        }
    }

}
