package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by DaveArt on 7/30/2018.
 */

public class TypeFaceUtil {

    public static void overrideFont(Context context, String defaultFontNameToOverride, String
            customFontFileNameInAssets){
        try{
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(),
                    customFontFileNameInAssets);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField
                    (defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        }catch (Exception e){
            Log.e(TypeFaceUtil.class.getName(), "can not set custom font " + customFontFileNameInAssets +
                    " instead of " + defaultFontNameToOverride);
        }
    }

}
