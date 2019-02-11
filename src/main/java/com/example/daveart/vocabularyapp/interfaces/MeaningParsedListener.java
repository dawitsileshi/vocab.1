package com.example.daveart.vocabularyapp.interfaces;

import android.app.Dialog;

public interface MeaningParsedListener{

    void onSaveClicked(String cachedWord, boolean isCheckBoxChecked, int wordPostion, Dialog dialog, Object object, String word, boolean isEdited);
    void onCacheClicked(String word);
    void onClosingDialog();

}

