package com.example.daveart.vocabularyapp.view_holders;

import android.view.View;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.model.WordsViewType;

import androidx.annotation.NonNull;

public class HeaderViewHolder extends BaseViewHolder{

    private TextView header_textView;

    @Override
    public void setData(RecyclerViewAdapter recyclerViewAdapter, Object object, int position, int lastCheckedPosition) {

        WordsViewType wordsViewType = (WordsViewType) object;

        getHeader_textView().setText(wordsViewType.getFirstLetter());
    }

    public HeaderViewHolder(@NonNull View itemView) {

        super(itemView);

        header_textView = itemView.findViewById(R.id.textView_itemHeader);

    }

    public TextView getHeader_textView() {
        return header_textView;
    }
}

