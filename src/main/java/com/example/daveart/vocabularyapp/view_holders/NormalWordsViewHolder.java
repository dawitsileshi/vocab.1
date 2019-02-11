package com.example.daveart.vocabularyapp.view_holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.interfaces.RecyclerViewItemsClickLongClickListener;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.model.WordsViewType;

import androidx.cardview.widget.CardView;

public class NormalWordsViewHolder extends BaseViewHolder implements View.OnClickListener, View
        .OnLongClickListener {

    private TextView word_item_text_view, meaning_item_text_view, firstLetter_item_text_view;
    private ImageButton edit_button, delete_button;
    private CardView cardView;
    private MaterialRippleLayout rippleLayout;
    private LinearLayout image_button_container;
    private RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener;
    private Object object;

    @Override
    public void setData(RecyclerViewAdapter recyclerViewAdapter, Object object, int position, int lastCheckedPosition) {

        this.object = object;

        SavedWord savedWord;

        if(object instanceof WordsViewType) {

            WordsViewType wordsViewType = (WordsViewType) object;
            savedWord = wordsViewType.getSavedWord();

        }else {

            savedWord = (SavedWord) object;

        }

        final String word = savedWord.getWord();
        final String wordType = savedWord.getWordType();
        final String example = savedWord.getExample();
        final String meaning = "(" + wordType + ") " + savedWord.getMeaning();

        char[] wordToArray = word.toCharArray();

//            getSort_item_text_view().setText(String.valueOf(wordToArray[0]));
        getFirstLetter_item_text_view().setText(String.valueOf(wordToArray[0]));
        getWord_item_text_view().setText(word);
//        getWord_item_text_view().setText(savedWord.getTimestamp());
        getMeaning_item_text_view().setText(meaning);
        itemView.setTag(position);

        getCardView().setOnClickListener(this);
        getCardView().setOnLongClickListener(this);

    }

    public NormalWordsViewHolder(View itemView) {
        super(itemView);

        word_item_text_view = itemView.findViewById(R.id.wordItem);
        meaning_item_text_view = itemView.findViewById(R.id.meaningItem);
        firstLetter_item_text_view = itemView.findViewById(R.id.firstLetterItem);

        cardView = itemView.findViewById(R.id.constraintLayout_showWord_container);
        image_button_container = itemView.findViewById(R.id.image_button_container);

        edit_button = itemView.findViewById(R.id.edit_button);
        delete_button = itemView.findViewById(R.id.delete_button);

        rippleLayout = itemView.findViewById(R.id.ripple);

//        sort_item_text_view = itemView.findViewById(R.id.textView_item_contact_sorting);
//        sorting_container = itemView.findViewById(R.id
//                .linearLayout_item_contact_sorting_container);

//        getRootView().setOnClickListener(this);
//        getRootView().setOnLongClickListener(this);


    }

    public void setRecyclerViewItemsClickLongClickListener(RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener) {
        this.recyclerViewItemsClickLongClickListener = recyclerViewItemsClickLongClickListener;
    }

    private TextView getWord_item_text_view() {
        return word_item_text_view;
    }

    private TextView getMeaning_item_text_view() {
        return meaning_item_text_view;
    }

    private TextView getFirstLetter_item_text_view() {
        return firstLetter_item_text_view;
    }

    public ImageButton getEdit_button() {
        return edit_button;
    }

    public ImageButton getDelete_button() {
        return delete_button;
    }

    public CardView getCardView() {
        return cardView;
    }

    public MaterialRippleLayout getRippleLayout() {
        return rippleLayout;
    }

    public LinearLayout getImage_button_container() {
        return image_button_container;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        recyclerViewItemsClickLongClickListener.onItemClicked(getAdapterPosition(), v, object);
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        SavedWord savedWord;
        if(object instanceof SavedWord) {
            savedWord = (SavedWord) object;
        }else {
            WordsViewType wordsViewType = (WordsViewType) object;
            savedWord = wordsViewType.getSavedWord();
        }
        recyclerViewItemsClickLongClickListener.onItemLongClicked(getAdapterPosition(), savedWord.getId(),
                savedWord);
        return true;
    }
}
