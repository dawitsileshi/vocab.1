package com.example.daveart.vocabularyapp.view_holders;

import androidx.appcompat.widget.AppCompatCheckBox;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.model.MemorizedWords;

import java.util.ArrayList;

public class MemorizedWordsViewHolder extends BaseViewHolder implements View.OnClickListener {

    private LinearLayout linearLayout_itemMemorized;
    private TextView textView_itemMemorized;
    private AppCompatCheckBox appCompatCheckBox_itemMemorized;
    MemorizedWords memorizedWords;
    RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Integer> checkedPositions;

    public interface NotifyAddRemove{
        void add(int position);
        void remove(int position);
    }

    private NotifyAddRemove notifyAddRemove;

    @Override
    public void setData(RecyclerViewAdapter recyclerViewAdapter, Object object, int position, int lastCheckedPosition) {

        this.recyclerViewAdapter = recyclerViewAdapter;

        MemorizedWords memorizedWords = (MemorizedWords) object;
        this.memorizedWords = memorizedWords;

        getTextView_itemMemorized().setText(memorizedWords.getWord());

        if(memorizedWords.isSelected()) {
            getAppCompatCheckBox_itemMemorized().setChecked(true);
//            recyclerViewAdapter.notifyDataSetChanged();
        }else {
            getAppCompatCheckBox_itemMemorized().setChecked(false);
        }

    }

    public MemorizedWordsViewHolder(View itemView) {
        super(itemView);

        linearLayout_itemMemorized = itemView.findViewById(R.id.linearLayout_itemMemorized);
        textView_itemMemorized = itemView.findViewById(R.id.textView_itemMemorized);
        appCompatCheckBox_itemMemorized = itemView.findViewById(R.id.appCompatCheckBox_itemMemorized);

        checkedPositions = new ArrayList<>();

        itemView.setOnClickListener(this);
//        getRootView().setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(memorizedWords.isSelected()) {
            memorizedWords.setSelected(false);
            notifyAddRemove.remove(getAdapterPosition());
//            int value = checkedPositions.indexOf(getAdapterPosition());
//            checkedPositions.remove(value);
//            itemView.setHasTransientState(false);
        }else {
            memorizedWords.setSelected(true);
//            checkedPositions.add(getAdapterPosition());
            notifyAddRemove.add(getAdapterPosition());
            Toast.makeText(itemView.getContext(), String.valueOf(memorizedWords.getId()), Toast
                    .LENGTH_SHORT).show();
//            itemView.setHasTransientState(true);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public AppCompatCheckBox getAppCompatCheckBox_itemMemorized() {
        return appCompatCheckBox_itemMemorized;
    }

    public LinearLayout getLinearLayout_itemMemorized() {
        return linearLayout_itemMemorized;
    }

    public TextView getTextView_itemMemorized() {
        return textView_itemMemorized;
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    public void setNotifyAddRemove(NotifyAddRemove notifyAddRemove) {
        this.notifyAddRemove = notifyAddRemove;
    }
}

