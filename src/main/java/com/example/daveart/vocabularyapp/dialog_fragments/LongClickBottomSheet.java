package com.example.daveart.vocabularyapp.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

//import com.daimajia.easing.linear.Linear;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;

public class LongClickBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private TextView textView_title;
    private LinearLayout linearLayout_delete, linearLayout_edit, linearLayout_relatedWords;

    public static final String TITLE = "title";

    public LongClickBottomSheet newInstance(String word) {
        LongClickBottomSheet longClickBottomSheet = new LongClickBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, word);
        longClickBottomSheet.setArguments(bundle);
        return longClickBottomSheet;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearLayout_bottomSheet_delete:
//                Toast.makeText(getActivity(), "Delete Clicked", Toast.LENGTH_SHORT).show();
                onButtonClickedListener.onDeleteClicked();
                break;
            case R.id.linearLayout_bottomSheet_edit:
//                Toast.makeText(getActivity(), "Edit Clicked", Toast.LENGTH_SHORT).show();
                onButtonClickedListener.onEditClicked();
                break;
            case R.id.linearLayout_bottomSheet_relatedWords:
//                Toast.makeText(getActivity(), "Related Words Clicked", Toast.LENGTH_SHORT).show();
                onButtonClickedListener.onRelatedWordsClicked();
                break;
        }
        dismiss();
    }

    public interface OnButtonClickedListener{
        void onDeleteClicked();
        void onEditClicked();
        void onRelatedWordsClicked();
    }

    public OnButtonClickedListener onButtonClickedListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.long_click_bottom_sheet_dialog, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        linearLayout_delete = view.findViewById(R.id.linearLayout_bottomSheet_delete);
        linearLayout_edit = view.findViewById(R.id.linearLayout_bottomSheet_edit);
        linearLayout_relatedWords = view.findViewById(R.id.linearLayout_bottomSheet_relatedWords);

        textView_title = view.findViewById(R.id.textView_bottomSheet_title);

        if(getArguments() != null) {
            textView_title.setText(getArguments().getString(TITLE));
        }

        linearLayout_delete.setOnClickListener(this);
        linearLayout_edit.setOnClickListener(this);
        linearLayout_relatedWords.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            onButtonClickedListener = (OnButtonClickedListener) getTargetFragment();
        }catch (ClassCastException c){
            throw new ClassCastException(context.toString() + " must implement this " +
                    "OnButtonClickedListener interface");
        }
    }
}
