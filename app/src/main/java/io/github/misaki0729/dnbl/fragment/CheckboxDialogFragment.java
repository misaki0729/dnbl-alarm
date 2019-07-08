package io.github.misaki0729.dnbl.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.greenrobot.eventbus.EventBus;

import io.github.misaki0729.dnbl.event.CheckDialogEvent;

public class CheckboxDialogFragment {

    public static class Dialog extends DialogFragment {
        public static final String TITLE = "title";
        public static final String CHECKITEMLIST = "check_item_list";
        public static final String CHECKED_FRAG = "checked_frag";

        private DialogInterface.OnClickListener clickListener;

        private String title;
        private CharSequence[] checkItemList;
        private boolean[] checkedFrag;

        @Override
        public void setArguments(@Nullable Bundle args) {
            title = args.getString(TITLE);
            checkItemList = args.getCharSequenceArray(CHECKITEMLIST);
            checkedFrag = args.getBooleanArray(CHECKED_FRAG);
        }

        @NonNull
        @Override
        public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title)
                    .setMultiChoiceItems(checkItemList, checkedFrag,
                            (dialogInterface, which, flag) -> {
                                checkedFrag[which] = flag;
                                EventBus.getDefault().post(new CheckDialogEvent(checkedFrag));
                            }
                    )
                    .setPositiveButton("OK", clickListener)
                    .setNegativeButton("Cancel",
                            (dialog, which) -> dialog.dismiss());

            return builder.create();
        }

        public void setOnPositiveButtonClickListener(DialogInterface.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }
}
