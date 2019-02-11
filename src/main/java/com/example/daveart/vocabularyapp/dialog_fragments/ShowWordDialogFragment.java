package com.example.daveart.vocabularyapp.dialog_fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class ShowWordDialogFragment extends DialogFragment {

    TextView textView_word, textView_wordType, textView_meaning, textView_example;
    FloatingActionButton imageView_share, fab_showWord_pronunce;

    private String word = "", meaning, wordType, example;

    public static final String PASSEDPARCELABLEOBJECT = "SavedWordPassed";


    public interface OnShareClicked{
        void shareClicked(SpannableStringBuilder data);
        void pronunceClicked(String word);
    }

    public OnShareClicked onShareClicked;

    public ShowWordDialogFragment newInstance(SavedWord savedWord) {
        ShowWordDialogFragment showWordDialogFragment = new ShowWordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASSEDPARCELABLEOBJECT, savedWord);
        showWordDialogFragment.setArguments(bundle);
        return showWordDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_word, container, false);

        textView_word = rootView.findViewById(R.id.textView_showWord_word);
        textView_wordType = rootView.findViewById(R.id.textView_showWord_wordType);
        textView_meaning = rootView.findViewById(R.id.textView_showWord_meaning);
        textView_example = rootView.findViewById(R.id.textView_showWord_example);

        imageView_share = rootView.findViewById(R.id.fab_showWord_share);
        fab_showWord_pronunce = rootView.findViewById(R.id.fab_showWord_pronunce);

        if(getArguments() != null) {
            SavedWord savedWord = getArguments().getParcelable(PASSEDPARCELABLEOBJECT);

            word = savedWord.getWord();
            meaning = savedWord.getMeaning();
            wordType = savedWord.getWordType();
            example = savedWord.getExample();

//            Log.i("Example", savedWord.getExample());

        }
        textView_word.setText(word);
        textView_wordType.setText(wordType);
        textView_meaning.setText(meaning);
        textView_example.append(example);

        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        this.getDialog().getWindow().setAttributes(layoutParams);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imageView_share.animate().setDuration(200);



        fab_showWord_pronunce.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                fab_showWord_pronunce.animate().setInterpolator(new DecelerateInterpolator())
                        .scaleY(.7f).scaleX(.7f);
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                fab_showWord_pronunce.animate().setInterpolator(new OvershootInterpolator(5f)).scaleX
                        (1.1f).scaleY(1.1f);
                onShareClicked.pronunceClicked(word);
            }
            return true;
        });
//        fab_showWord_pronunce.setOnClickListener(this);

        imageView_share.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                imageView_share.animate().setInterpolator(new DecelerateInterpolator())
                        .scaleY(.7f).scaleX(.7f);
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                imageView_share.animate().setInterpolator(new OvershootInterpolator(5f)).scaleX
                        (1.1f).scaleY(1.1f);
                SpannableStringBuilder data =stylizeTheString(word, wordType, meaning, example);
                onShareClicked.shareClicked(data);
            }
            return true;
        }
        );
//        imageView_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageView_share.animate().setInterpolator(new OvershootInterpolator()).scaleX(1.2f)
//                        .scaleY(1.2f);
//            }
//        });
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onShareClicked = (OnShareClicked) getTargetFragment();
    }

    private SpannableStringBuilder stylizeTheString(String word, String wordType, String meaning, String
     example) {
        SpannableString spannableStringBuilder = new SpannableString(word);
        SpannableString spannableStringBuilder1 = new SpannableString(wordType);
        SpannableString spannableStringBuilder2 = new SpannableString("Eg");

        StyleSpan bold = new StyleSpan(Typeface.BOLD);
        StyleSpan italic = new StyleSpan(Typeface.ITALIC);
        UnderlineSpan underlineSpan = new UnderlineSpan();

        spannableStringBuilder.setSpan(bold, 0, word.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder1.setSpan(italic, 0, wordType.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder2.setSpan(underlineSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder();
        spannableStringBuilder3.append(spannableStringBuilder).append("\t\t").append
                (spannableStringBuilder1).append("\n").append(meaning).append("\n\t\t").append
                (spannableStringBuilder2).append(":").append(example);
        String data = String.valueOf(spannableStringBuilder) + " \t\t" + String.valueOf
                (spannableStringBuilder1) + "\n" + meaning + "\n\t\t" + String.valueOf
                (spannableStringBuilder2) + ": " + example;

        return spannableStringBuilder3;

    }
}
