package com.example.daveart.vocabularyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.R;
//import com.example.daveart.vocabularyapp.adapters.CachedWordsRecycler;
import com.example.daveart.vocabularyapp.adapters.RecyclerViewAdapter;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.dialog_fragments.MeaningParsedDialogFragment;
import com.example.daveart.vocabularyapp.interfaces.RecyclerViewItemsClickLongClickListener;
import com.example.daveart.vocabularyapp.model.CachedWord;
import com.example.daveart.vocabularyapp.utils.ExceptionUtil;

import java.util.ArrayList;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class HardWordsFragment extends Fragment {

    private RecyclerView recyclerView_hard_words;
    private LinearLayout linearLayout;
//    private LinearLayoutManager layoutManager;

    private DataSource dataSource;
    private ExceptionUtil exceptionUtil;

    private ArrayList<Object> cachedWordArrayList;

    private MeaningParsedDialogFragment meaningParsedDialogFragment;
//    private WordsFragment wordsFragment;
    private RecyclerViewAdapter recyclerViewAdapter;
//    private CachedWordsRecycler cachedWordsRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.hard_words_fragment, container, false);

        recyclerView_hard_words = rootView.findViewById(R.id.hardWordsFragment_recycler);
        linearLayout = rootView.findViewById(R.id.linearLayout_hardWords_container);
        dataSource = new DataSource(getContext());
        exceptionUtil = new ExceptionUtil(getContext());
        cachedWordArrayList = dataSource.cachedWords();
        recyclerViewAdapter = new RecyclerViewAdapter(cachedWordArrayList);
//        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), cachedWordArrayList, 1);
//        cachedWordsRecycler = new CachedWordsRecycler(getContext(), cachedWordArrayList, this);
        meaningParsedDialogFragment = new MeaningParsedDialogFragment();
        meaningParsedDialogFragment.setTargetFragment(this, 0);

//        wordsFragment = new WordsFragment();

        checkIfDataExists(-1);
        addingRecyclerView();

        // 4 is ItemTouchHelper.LEFT and 8 is ItemTouchHelper.RIGHT
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 4 | 8) {
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//                dataSource.removeItemById((long)viewHolder.itemView.getTag(), ItemTables
//                        .TEMP_WORDS_TABLE, ItemTables.TEMP_COLUMN_ID);
//                int position = viewHolder.getAdapterPosition();
//                checkIfDataExists(position);
//
//            }
//
//        }).attachToRecyclerView(recyclerView_hard_words);

        // TODO: sometimes position of the item gets wrong, and displays wrong item on the dialog
        recyclerViewAdapter.setRecyclerViewItemsClickLongClickListener(new RecyclerViewItemsClickLongClickListener() {
            @Override
            public void onItemClicked(int position, View view, Object object) {
                CachedWord cachedWord = (CachedWord) cachedWordArrayList.get(position);
                meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance
                        (cachedWord.getWord(), position, null, false, -1);
                meaningParsedDialogFragment.show(getActivity().getSupportFragmentManager(), "hard_words");
                String word = cachedWord.getWord();
            }

            @Override
            public void onItemLongClicked(int position, long id, Object object) {
                dataSource.removeItemById(id, ItemTables
                        .TEMP_WORDS_TABLE, ItemTables.TEMP_COLUMN_ID);
//                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
//                cachedWordArrayList.remove(position);
//                recyclerViewAdapter.notifyItemRemoved(position);
                checkIfDataExists(position);
            }
        });

//        recyclerViewAdapter.setDeleteListener(new RecyclerViewAdapter.deleteListener() {
//            @Override
//            public void onItemClicked(int position, View view) {
//                CachedWord cachedWord = (CachedWord) cachedWordArrayList.get(position);
//                meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance
//                        (cachedWord.getWord(), position, null);
//                meaningParsedDialogFragment.show(getActivity().getSupportFragmentManager(), "hard_words");
//                String word = cachedWord.getWord();
//            }
//
//            @Override
//            public void onItemLongClicked(int position, long id, SavedWord savedWord) {
//
//            }
//        });
        return rootView;
    }

    public void checkIfDataExists(int position){

        if(position != -1){
            cachedWordArrayList.remove(position);
//            recyclerViewAdapter.notifyAll();
            recyclerViewAdapter.notifyDataSetChanged();
            // TODO: having trouble using notifyItemRemoved(), indexOutOfBoundsException is being
            // caused
//            recyclerViewAdapter.notifyItemRemoved(position);
//            recyclerViewAdapter.notifyItemRangeChanged(position, position - 3);
        }

        if(exceptionUtil.checkIfDataExists(ItemTables.TEMP_WORDS_TABLE, ItemTables.TEMP_COLUMN_TIMESTAMP)){
            linearLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addingRecyclerView() {

        recyclerView_hard_words.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(getContext());

        recyclerView_hard_words.setLayoutManager(new LinearLayoutManager (getContext()));
        recyclerView_hard_words.setAdapter(recyclerViewAdapter);

    }

    public void notifying(long id){
        CachedWord cachedWord = (CachedWord) dataSource.getSingleData(id, ItemTables
                .TEMP_WORDS_TABLE);
        recyclerViewAdapter.objectList.add(0, cachedWord);
//        recyclerViewAdapter.notifyItemInserted(0);
        recyclerViewAdapter.notifyItemRangeChanged(0, 1);
        linearLayout.setVisibility(View.INVISIBLE);
//        checkIfDataExists();
    }

//    @Override
//    public void onItemClickedMethod(int adapterPosition) {
//        meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance
//                (cachedWordArrayList.get(adapterPosition).getWord(), adapterPosition, null);
//        meaningParsedDialogFragment.show(getActivity().getSupportFragmentManager(), "hard_words");
//        word = cachedWordArrayList.get(adapterPosition).getWord();
//    }

//    @Override
//    public void onItemClicked(int position, View view) {
//        CachedWord cachedWord = (CachedWord) cachedWordArrayList.get(position);
//        meaningParsedDialogFragment = new MeaningParsedDialogFragment().newInstance
//                (cachedWord.getWord(), position, null);
//        meaningParsedDialogFragment.show(getActivity().getSupportFragmentManager(), "hard_words");
//        String word = cachedWord.getWord();
//    }
//
//    @Override
//    public void onItemLongClicked(int position, long id, SavedWord savedWord) {
//
//    }
}
