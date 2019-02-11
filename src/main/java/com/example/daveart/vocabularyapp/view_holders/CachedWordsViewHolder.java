package com.example.daveart.vocabularyapp.view_holders;

import android.view.View;
import android.widget.TextView;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.interfaces.RecyclerViewItemsClickLongClickListener;
import com.example.daveart.vocabularyapp.model.CachedWord;

public class CachedWordsViewHolder extends BaseViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView textView_cached_words_item_layout;
    public RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener;
    private Object object;
    private int clickedPosition;
    private long id;

    @Override
    public void setData(RecyclerViewAdapter recyclerViewAdapter, Object object, int position, int lastCheckedPosition) {

        CachedWord cachedWord = (CachedWord) object;
        clickedPosition = position;
        this.object = object;

        id = cachedWord.getId();
        getTextView_cached_words_item_layout().setText(cachedWord.getWord());

    }

    public CachedWordsViewHolder(View itemView) {
        super(itemView);

        textView_cached_words_item_layout = itemView.findViewById(R.id
                .textView_cached_words_itemLayout);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    private TextView getTextView_cached_words_item_layout() {
        return textView_cached_words_item_layout;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        recyclerViewItemsClickLongClickListener.onItemClicked(clickedPosition, v, object);
//        Toast.makeText(itemView.getContext(), "Touched", Toast.LENGTH_SHORT).show();
    }

    public void setRecyclerViewItemsClickLongClickListener(RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener) {
        this.recyclerViewItemsClickLongClickListener = recyclerViewItemsClickLongClickListener;
    }

    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        recyclerViewItemsClickLongClickListener.onItemLongClicked(clickedPosition, id, object);
        return true;
    }
}
