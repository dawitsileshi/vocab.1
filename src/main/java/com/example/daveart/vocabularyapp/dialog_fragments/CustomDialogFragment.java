package com.example.daveart.vocabularyapp.dialog_fragments;
//
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.daveart.vocabularyapp.R;

///**
// * Created by DaveArt on 7/25/2018.
// */
//
public class CustomDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener{
//
    public EditText word, meaning;
    public Button save, cancel;
    public MaterialRippleLayout fetch;
    ProgressBar progressBar;
    AppCompatSpinner wordTypeSpinner;
//
    String itemSelected;
//
    public interface CustomDialogListener{


        void onSaveButtonClicked(String wordType, String word, String meaning);

        void onCancelButtonClicked();
        void onFetchButtonClicked(String wordType, String word);
    }

    private CustomDialogListener customDialogListener;
//
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View alert_layout_view = layoutInflater.inflate(R.layout.alert_layout, null);
//
////        builder.setTitle("Insert a word");
//
//        word = alert_layout_view.findViewById(R.id.wordEditText);
//        meaning = alert_layout_view.findViewById(R.id.meaningEditText);
//        save = alert_layout_view.findViewById(R.id.save_button);
//        cancel = alert_layout_view.findViewById(R.id.cancel_button);
//        fetch = alert_layout_view.findViewById(R.id.materialRippleLayout);
//        progressBar = alert_layout_view.findViewById(R.id.progressBarID);
//        wordTypeSpinner = alert_layout_view.findViewById(R.id.wordTypeSpinner);
//
//        preparingSpinner();
//
//        builder.setView(alert_layout_view);
//
        wordTypeSpinner.setOnItemSelectedListener(this);
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                customDialogListener.onSaveButtonClicked(itemSelected, word.getText().toString(),
//                        meaning.getText().toString());
//
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                customDialogListener.onCancelButtonClicked();
//
//            }
//        });
//
//        fetch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                customDialogListener.onFetchButtonClicked(itemSelected, word.getText().toString());
//                wordTypeSpinner.setEnabled(false);
//
//            }
//        });
//
//        fetch.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Toast.makeText(getContext(), "Fetch meaning", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//
////        showText();
//
        return builder.create();
//    }
//
//    private void preparingSpinner() {
//        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array
//                .word_type, android.R.layout.simple_spinner_item);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        wordTypeSpinner.setAdapter(arrayAdapter);
    }
//
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        itemSelected = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        itemSelected = adapterView.getItemAtPosition(0).toString();
    }
//
////    private void showText() {
////    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        customDialogListener = (CustomDialogListener) getTargetFragment();
//    }
//
}
