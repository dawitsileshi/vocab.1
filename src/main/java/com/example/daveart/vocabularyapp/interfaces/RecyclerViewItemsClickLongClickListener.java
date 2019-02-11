package com.example.daveart.vocabularyapp.interfaces;

import android.view.View;

public interface RecyclerViewItemsClickLongClickListener {

    void onItemClicked(int position, View view, Object object);

    void onItemLongClicked(int position, long id, Object object);

}
