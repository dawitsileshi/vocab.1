package com.example.daveart.vocabularyapp.view_holders;

import android.view.View;

import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {


    public abstract void setData(RecyclerViewAdapter recyclerViewAdapter, Object object, int position, int lastCheckedPosition);

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public View getRootView(){
        return this.itemView;
    }


}
