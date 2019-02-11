package com.example.daveart.vocabularyapp.utils;

import android.graphics.Color;

import com.example.daveart.vocabularyapp.view_holders.NormalWordsViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemAnimator extends DefaultItemAnimator {


    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        final NormalWordsViewHolder viewHolder = (NormalWordsViewHolder) holder;

        CardView cardView = viewHolder.getCardView();

        cardView.setHasTransientState(true);
        cardView.setCardBackgroundColor(Color.RED);

        return super.animateRemove(holder);
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return super.animateAdd(holder);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the payload list is not empty, DefaultItemAnimator returns <code>true</code>.
     * When this is the case:
     * <ul>
     * <li>If you override {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}, both
     * ViewHolder arguments will be the same instance.
     * </li>
     * <li>
     * If you are not overriding {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)},
     * then DefaultItemAnimator will call {@link #animateMove(RecyclerView.ViewHolder, int, int, int, int)} and
     * run a move animation instead.
     * </li>
     * </ul>
     *
     * @param viewHolder
     * @param payloads
     */
    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull List<Object> payloads) {
        return super.canReuseUpdatedViewHolder(viewHolder, payloads);
    }
}
