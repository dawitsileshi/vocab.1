package com.example.daveart.vocabularyapp.view_holders;

import android.content.Context;
import android.view.ViewGroup;

import com.example.daveart.vocabularyapp.model.SavedWord;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class RecyclerViewAdapter extends com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter {

    private Context mContext;

    public List<Object> objectList;
    private List<SavedWord> mDataItemsFull;

    private int sortType;

    private int tag = -1;

    public int getTag() {
        return tag;
    }

    public RecyclerViewAdapter(ArrayList<Object> objectList) {
        super(objectList);
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

//    public RecyclerViewAdapter(Context context, List<Object> objects, int sortType) {
//        mContext = context;
//        objectList = objects;
//        mDataItemsFull = new ArrayList<>(objectList);
//        this.deletelistener = null;
//        this.sortType = sortType;
//    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new NormalWordsViewHolder(parent);
            case 1:
                return new NormalWordsViewHolder(parent);
            case 2:
                return new HeaderViewHolder(parent);
            case 3:
                return new CachedWordsViewHolder(parent);
            case 4:
                return new MeaningParsedViewHolder(parent);
            default:
                throw new NullPointerException("No viewHolder found");
        }
    }
}
