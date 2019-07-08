package io.github.misaki0729.dnbl.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.greenrobot.eventbus.EventBus;

import io.github.misaki0729.dnbl.event.SelectDialogEvent;

public class SelectDialogFragment {

    public static class Dialog extends DialogFragment {
        public static final String TITLE = "title";
        public static final String SELECT_LIST = "select_list";
        public static final String SELECT_ITEM = "select_item";

        private String title;
        private CharSequence[] selectList;
        private int selectItem;

        @Override
        public void setArguments(@Nullable Bundle args) {
            title = args.getString(TITLE);
            selectList = args.getCharSequenceArray(SELECT_LIST);
            selectItem = args.getInt(SELECT_ITEM, 1);
        }

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setSingleChoiceItems(selectList, selectItem,
                            (dialogInterface, which) -> {
                                EventBus.getDefault().post(new SelectDialogEvent(which));
                            }
                        )
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            return builder.create();
        }
    }
}
