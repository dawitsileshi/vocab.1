package com.example.daveart.vocabularyapp.dialog_fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.example.daveart.vocabularyapp.R;

public class ExitDialogFragment extends DialogFragment {

    public interface ExitInterface {
        void onYesClicked();
        void onNoClicked();
    }

    public ExitInterface exitInterfaceListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Quit");

        builder.setMessage("Are you sure you want to exit?");

        setCancelable(false);
        builder.setIcon(R.drawable.ic_close_black_24dp);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitInterfaceListener.onYesClicked();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                exitInterfaceListener.onNoClicked();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        exitInterfaceListener = (ExitInterface) getTargetFragment();
    }
}
