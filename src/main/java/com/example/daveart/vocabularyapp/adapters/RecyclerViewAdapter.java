package com.example.daveart.vocabularyapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.dialog_fragments.MeaningParsedDialogFragment;
import com.example.daveart.vocabularyapp.interfaces.RecyclerViewItemsClickLongClickListener;
import com.example.daveart.vocabularyapp.model.CachedWord;
import com.example.daveart.vocabularyapp.model.MeaningParsed;
import com.example.daveart.vocabularyapp.model.MemorizedWords;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.model.WordsViewType;
import com.example.daveart.vocabularyapp.utils.RecyclerDiffUtil;
import com.example.daveart.vocabularyapp.view_holders.BaseViewHolder;
import com.example.daveart.vocabularyapp.view_holders.CachedWordsViewHolder;
import com.example.daveart.vocabularyapp.view_holders.HeaderViewHolder;
import com.example.daveart.vocabularyapp.view_holders.MeaningParsedViewHolder;
import com.example.daveart.vocabularyapp.view_holders.MemorizedWordsViewHolder;
import com.example.daveart.vocabularyapp.view_holders.NormalWordsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder>
        implements Filterable {

    public ArrayList<Object> objectList;
    private ArrayList<Object> objectListFull;
    private ArrayList<Integer> checkedPositions;
    private int lastCheckedPosition = -1;

    public ArrayList<Object> getObjectList() {
        return objectList;
    }

    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    private void setLastCheckedPosition(int lastCheckedPosition) {
        this.lastCheckedPosition = lastCheckedPosition;
    }

    public RecyclerViewAdapter(){}

    public RecyclerViewAdapter(ArrayList<Object> objects) {
        objectList = objects;
        objectListFull = new ArrayList<>(objectList);
        checkedPositions = new ArrayList<>();
    }

//    private deleteListener deletelistener;

    private RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener;

    public void setRecyclerViewItemsClickLongClickListener(RecyclerViewItemsClickLongClickListener recyclerViewItemsClickLongClickListener) {
        this.recyclerViewItemsClickLongClickListener = recyclerViewItemsClickLongClickListener;
    }

    @Override
    public Filter getFilter() {
        return wordFilter;
    }

    private Filter wordFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Object> filteredList = new ArrayList<>();
//
            if (charSequence == null || charSequence.length() == 0) {

                filteredList.addAll(objectListFull);

            } else {
//
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Object item : objectListFull) {

                    SavedWord savedWord;
                    if(item instanceof WordsViewType) {
                        WordsViewType wordsViewType = (WordsViewType) item;
                        savedWord = wordsViewType.getSavedWord();
                    }else {
                        savedWord = (SavedWord) item;
                    }

                    if (savedWord.getWord().toLowerCase().contains(filterPattern)) {

                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();

            results.values = filteredList;

            return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            objectList.clear();
//
//            Object list = (filterResults.values);
//            objectList.addAll((Collection<?>) list);
            objectList.addAll((List) filterResults.values);

            notifyDataSetChanged();
        }
    };

    @Override
    public int getItemViewType(int position) {

        if(objectList.get(position) instanceof SavedWord){

            if(((SavedWord) objectList.get(position)).getDifficulty() == 0) {
                return 5;
            } else {
                return 7;
            }

//            return 5;

        } else if (objectList.get(position) instanceof WordsViewType) {

            WordsViewType wordsViewType = (WordsViewType) objectList.get(position);

            if(wordsViewType.getFirstLetter() == null) {
                Log.i("First Letter", "null");
            }

            if (wordsViewType.getViewType() == 1) {

                int difficulty = wordsViewType.getSavedWord().getDifficulty();

                if (difficulty == 0) {

                    return 0;

                } else if (difficulty == 1) {

                    return 1;

                }

            } else if (wordsViewType.getViewType() == 2) {

                return 2;

            }
        } else if (objectList.get(position) instanceof CachedWord) {

            return 3;

        } else if (objectList.get(position) instanceof MeaningParsed) {

            return 4;

        }else if(objectList.get(position) instanceof MemorizedWords) {

            return 6;

        }

        return -1;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final BaseViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType){
            case 0:
            case 5:
                view = inflater.inflate(R.layout.item_words, parent, false);
                viewHolder = new NormalWordsViewHolder(view);

                ((NormalWordsViewHolder) viewHolder).setRecyclerViewItemsClickLongClickListener(new RecyclerViewItemsClickLongClickListener() {
                    @Override
                    public void onItemClicked(int position, View view, Object object) {
                        recyclerViewItemsClickLongClickListener.onItemClicked(position, view, object);
                    }

                    @Override
                    public void onItemLongClicked(int position, long id, Object object) {
                        recyclerViewItemsClickLongClickListener.onItemLongClicked(position, id, object);
                    }
                });
                break;
            case 1:
            case 7:
                view = inflater.inflate(R.layout.item_difficult, parent, false);
                viewHolder = new NormalWordsViewHolder(view);
                ((NormalWordsViewHolder) viewHolder).setRecyclerViewItemsClickLongClickListener
                        (new RecyclerViewItemsClickLongClickListener() {
                            @Override
                            public void onItemClicked(int position, View view, Object object) {
                                recyclerViewItemsClickLongClickListener.onItemClicked(position,
                                        view, object);
                            }

                            @Override
                            public void onItemLongClicked(int position, long id, Object object) {
                                recyclerViewItemsClickLongClickListener.onItemLongClicked
                                        (position, id, object);
                            }
                        });
                break;
            case 2:
                view = inflater.inflate(R.layout.item_header, parent, false);
                viewHolder = new HeaderViewHolder(view);
                break;
            case 3:
                view = inflater.inflate(R.layout.item_cached, parent, false);
                viewHolder = new CachedWordsViewHolder(view);
                ((CachedWordsViewHolder) viewHolder).setRecyclerViewItemsClickLongClickListener(new RecyclerViewItemsClickLongClickListener() {
                    @Override
                    public void onItemClicked(int position, View view, Object object) {
                        recyclerViewItemsClickLongClickListener.onItemClicked(position, view, object);
                    }

                    @Override
                    public void onItemLongClicked(int position, long id, Object object) {
                      recyclerViewItemsClickLongClickListener.onItemLongClicked(position, id, object);
                    }
                });

                break;
            case 4:
                view = inflater.inflate(R.layout.meanings_parsed, parent, false);
                viewHolder = new MeaningParsedViewHolder(view);
//                        Log.i("Position", String.valueOf(lastCheckedPosition));
//                ((MeaningParsedViewHolder) viewHolder).getRadioButton_meaningParsed().setChecked
//                        (lastCheckedPosition == viewHolder.getAdapterPosition());
//                ((MeaningParsedViewHolder) viewHolder).getConstraintLayout_meaningParsed()
//                        .setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                lastCheckedPosition = viewHolder.getAdapterPosition();
//                                ((MeaningParsedViewHolder) viewHolder)
//                                        .getRadioButton_meaningParsed().setChecked(true);
//                                notifyDataSetChanged();
//                            }
//                        });
                break;
            case 6:
                view = inflater.inflate(R.layout.item_memorized, parent, false);
                viewHolder = new MemorizedWordsViewHolder(view);
                ((MemorizedWordsViewHolder) viewHolder).setNotifyAddRemove(new MemorizedWordsViewHolder.NotifyAddRemove() {
                    @Override
                    public void add(int position) {
                        checkedPositions.add(position);
                    }

                    @Override
                    public void remove(int position) {
                        int value = checkedPositions.indexOf(position);
                        checkedPositions.remove(value);
                    }
                });
                break;
            default:
                Log.i("View type", String.valueOf(viewType));
                throw new NullPointerException("No view type found");
        }

//        int layout = viewType == 1 ? R.layout.item_difficult : R.layout.item_words;
//
//        View item_view;
//        item_view = LayoutInflater.from(mContext).inflate(layout, parent, false);

//        return new MyViewHolder(item_view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, int position) {
        holder.setData(this, getItem(position), position, -1);
        if(holder instanceof MeaningParsedViewHolder) {
            RadioButton radioButton = ((MeaningParsedViewHolder)
                    holder).getRadioButton_meaningParsed();
            ((MeaningParsedViewHolder) holder).getRadioButton_meaningParsed().setChecked
                    (getLastCheckedPosition() == position);

            if(radioButton.isChecked()){
                radioButton.setBackgroundColor(Color.MAGENTA);
            }else {
                radioButton.setBackgroundColor(Color.CYAN);
            }

            ((MeaningParsedViewHolder) holder)
                    .getConstraintLayout_meaningParsed().setOnClickListener(new View
                    .OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLastCheckedPosition(holder.getAdapterPosition());
//                    lastCheckedPosition = position;
                    ((MeaningParsedViewHolder) holder).getRadioButton_meaningParsed().setChecked(true);
                    notifyDataSetChanged();
                }
            });
        }
//        switch (holder.getItemViewType()) {
//            case 0:
//                NormalWordsViewHolder normalWordsViewHolder = (NormalWordsViewHolder) holder;
//                assigningValuesToNormalWordsViewHolder(normalWordsViewHolder);
//                break;
//            case 1:
////                HardWordsViewHolder hardWordsViewHolder = (HardWordsViewHolder) holder;
//                NormalWordsViewHolder normalWordsViewHolder1 = (NormalWordsViewHolder) holder;
//                assigningValuesToNormalWordsViewHolder(normalWordsViewHolder1);
//                break;
//            case 2:
//                CachedWordsViewHolder cachedWordsViewHolder = (CachedWordsViewHolder) holder;
//                assigningValuesToCachedWordsViewHolder(cachedWordsViewHolder);
//                break;
//            case 3:
//                MeaningParsedViewHolder meaningParsedViewHolder = (MeaningParsedViewHolder) holder;
//                assigningValuesToMeaningParsedViewHolder(meaningParsedViewHolder);
//                break;
//
//        }

    }

    private Object getItem(int position) {
        return objectList.get(position);
    }

    public void insertData(List<Object> list) {
        RecyclerDiffUtil recyclerDiffUtil = new RecyclerDiffUtil(objectList, list);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(recyclerDiffUtil);
        objectList.clear();
        objectList.addAll(list);
        diffResult.dispatchUpdatesTo(this);
    }

//    private void assigningValuesToMeaningParsedViewHolder(final MeaningParsedViewHolder holder) {
//
//        final MeaningParsed currentMeaningParsed = (MeaningParsed) objectList.get(holder.getAdapterPosition());
//
//        String example = "Example: " + currentMeaningParsed.getExample();
//        holder.getTextView_meaningParsed_wordType().setText(currentMeaningParsed.getWord_type());
//        holder.getTextView_meaningParsed_meaning().setText(currentMeaningParsed.getMeaning());
//        Log.i("New Tag ", String.valueOf(getTag()));
//        holder.getTextView_meaningParsed_example().setText(example);
//
//        holder.getRadioButton_meaningParsed().setChecked(holder.getAdapterPosition() == getTag());
//
//        holder.getConstraintLayout_meaningParsed().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.getRadioButton_meaningParsed().setChecked(true);
////                lastCheckedPosition = holder.getAdapterPosition();
//                setTag(holder.getAdapterPosition());
//                notifyDataSetChanged();
//            }
//        });
//
//    }
//
//    private void assigningValuesToCachedWordsViewHolder(final CachedWordsViewHolder holder) {
//
//        CachedWord currentCachedWord = (CachedWord) objectList.get(holder.getAdapterPosition());
//
//        holder.getTextView_cached_words_item_layout().setText(currentCachedWord.getWord());
//        holder.itemView.setTag(currentCachedWord.getId());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deletelistener.onItemClicked(holder.getAdapterPosition(), holder.itemView);
//            }
//        });
//
//    }
//
//    private void assigningValuesToNormalWordsViewHolder(final NormalWordsViewHolder holder) {
//
//        if (sortType == 1) {
//            holder.getSorting_container().setVisibility(View.GONE);
//        }
//
//        final SavedWord savedWord = (SavedWord) objectList.get(holder.getAdapterPosition());
//
//        try {
//
////            dealingWithCursorValues(position);
//            final String word = savedWord.getWord();
//            final String wordType = savedWord.getWordType();
//            final String meaning = "(" + wordType + ") " + savedWord.getMeaning();
//
//            char[] wordToArray = word.toCharArray();
//
//            holder.getSort_item_text_view().setText(String.valueOf(wordToArray[0]));
//            holder.getFirstLetter_item_text_view().setText(String.valueOf(wordToArray[0]));
//            holder.getWord_item_text_view().setText(word);
//            holder.getMeaning_item_text_view().setText(meaning);
//            holder.itemView.setTag(holder.getAdapterPosition());
//
//            addTitle(objectList, holder);
//
//            holder.getCardView().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deletelistener.onItemClicked(holder.getAdapterPosition(), view);
//                    Toast.makeText(mContext, "Touched", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            holder.getCardView().setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    deletelistener.onItemLongClicked(holder.getAdapterPosition(), savedWord.getId
//                            (), savedWord);
//                    return true;
//                }
//            });
//
//        } catch (Exception e) {
//
//            Log.i(RecyclerViewAdapter.class.getName(), "Problem happened in recycler view ", e);
//
//        }
//    }
//
//    private void addTitle(List<Object> mSavedWords, NormalWordsViewHolder holder) {
//        SavedWord previousSavedWord = (SavedWord) mSavedWords.get(holder.getAdapterPosition() - 1);
//        SavedWord savedWord = (SavedWord) mSavedWords.get(holder.getAdapterPosition());
//        if (sortType == 0) {
//            if (holder.getAdapterPosition() >= 1) {
//                char previousLetter = previousSavedWord.getWord().charAt(0);
////                char previousLetter = objectList.get(holder.getAdapterPosition() - 1)
////                        .getWord().charAt(0);
//                char currentLetter = savedWord.getWord().charAt(0);
//                if (currentLetter != previousLetter) {
//                    holder.getSorting_container().setVisibility(View.VISIBLE);
//                } else {
//                    holder.getSorting_container().setVisibility(View.GONE);
//
//                }
////            } else if (holder.getAdapterPosition() == 0) {
////                holder.getSorting_container().setVisibility(View.VISIBLE);
//
//            }
//        }
//    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }
}