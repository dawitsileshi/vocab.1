package com.example.daveart.vocabularyapp.dialog_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeleteConfirmation extends BottomSheetDialogFragment {

    private interface HandlingConfirmationClicks{

        void onYesClicked();
        void onNoClidked();

    }

    public HandlingConfirmationClicks handlingConfirmationClicks;

    private TextView textView_yes, textView_no;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_delete_confirmation, container, false);

        textView_no = rootView.findViewById(R.id.textView_layout_delete_confirmation_no);
        textView_yes = rootView.findViewById(R.id.textView_layout_delete_confirmation_yes);

        textView_no.setOnClickListener(v -> {



        });

        textView_yes.setOnClickListener(v -> {



        });

        return rootView;

    }
}
