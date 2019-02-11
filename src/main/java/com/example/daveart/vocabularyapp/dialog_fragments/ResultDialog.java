package com.example.daveart.vocabularyapp.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;

/**
 * Created by DaveArt on 8/5/2018.
 */

public class ResultDialog extends DialogFragment implements View.OnClickListener{

    public TextView score_textView, noOfSeconds_textView, highScoreTitle_textView;
    public ImageButton exit_imageButton, restart_imageButton, save_imageButton;

    public static final String SCORE = "score";
    public static final String SECONDS = "seconds";

    private actionListener actionlistener;

    public interface actionListener{
        void onSaveClicked();
        void onRestartClicked(ResultDialog resultDialog);
        void onExitClicked(ResultDialog resultDialog);
    }

    public ResultDialog newInstance(String score, String noOfSeconds){

        ResultDialog resultDialog = new ResultDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SCORE, score);
        bundle.putString(SECONDS, noOfSeconds);
        resultDialog.setArguments(bundle);
        return resultDialog;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View result_view = inflater.inflate(R.layout.show_quiz_result, null);

        score_textView = result_view.findViewById(R.id.high_score);
        noOfSeconds_textView = result_view.findViewById(R.id.no_of_seconds);
        highScoreTitle_textView = result_view.findViewById(R.id.high_score_title);

        setCancelable(false);

        if(getArguments() != null){
            if(getArguments().getString(SECONDS) != null) {
                noOfSeconds_textView.append(getArguments().getString(SECONDS));
            }
            if(getArguments().getString(SCORE) != null) {
                score_textView.setText(getArguments().getString(SCORE));
            }
        }else{
            highScoreTitle_textView.setVisibility(View.GONE);
            noOfSeconds_textView.setVisibility(View.GONE);
            score_textView.setText("Paused");
        }

        exit_imageButton = result_view.findViewById(R.id.exit_icon);
        restart_imageButton = result_view.findViewById(R.id.restart_icon);
        save_imageButton = result_view.findViewById(R.id.done_icon);

        exit_imageButton.setOnClickListener(this);
        restart_imageButton.setOnClickListener(this);
        save_imageButton.setOnClickListener(this);

        this.getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getDialog().getWindow().setWindowAnimations(R.style.popUpScaleAnimation);

        return result_view;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View result_view = inflater.inflate(R.layout.show_quiz_result, null);
//
//        score_textView = result_view.findViewById(R.id.high_score);
//        noOfSeconds_textView = result_view.findViewById(R.id.no_of_seconds);
//
//        actionlistener.settingText(ResultDialog.this);
//
//        exit_imageButton = result_view.findViewById(R.id.exit_icon);
//        restart_imageButton = result_view.findViewById(R.id.restart_icon);
//        save_imageButton = result_view.findViewById(R.id.done_icon);
//
//        builder.setView(result_view);
//
//        setCancelable(false);
//
//        exit_imageButton.setOnClickListener(this);
//        restart_imageButton.setOnClickListener(this);
//        save_imageButton.setOnClickListener(this);
//
//        return builder.create();
//
//    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.exit_icon:
                actionlistener.onExitClicked(this);
                break;
            case R.id.restart_icon:
                actionlistener.onRestartClicked(this);
                break;
            case R.id.done_icon:
                actionlistener.onSaveClicked();
//                Toast.makeText(getActivity(), "Save is clicked", Toast.LENGTH_SHORT).show();
//        }


        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
//            actionlistener = (actionListener) context;
            actionlistener = (actionListener) getTargetFragment();
        }catch(ClassCastException e){
            throw new ClassCastException(getTargetFragment().toString() + " must implement this " +
                    "interface");
        }
    }
}
