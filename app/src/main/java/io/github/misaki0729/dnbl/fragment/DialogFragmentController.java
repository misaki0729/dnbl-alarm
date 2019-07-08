package io.github.misaki0729.dnbl.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.text.MessageFormat;
import java.util.ArrayList;

public class DialogFragmentController {
    public static final int TYPE_DOW_CHECK = 0;
    public static final int TYPE_DELAY_TIME_SELECT = 1;

    private AppCompatActivity activity;
    private int type;
    private Bundle args;
    private DialogInterface.OnClickListener clickListener = null;

    private DialogFragmentController() {}
    public DialogFragmentController(AppCompatActivity activity, int type, Bundle args, DialogInterface.OnClickListener clickListener) {
        this.activity = activity;
        this.type = type;
        this.args = args;
        this.clickListener = clickListener;
    }

    public void show() {
        String title;

        switch (type) {
            case TYPE_DOW_CHECK:
                title = args.getString(CheckboxDialogFragment.Dialog.TITLE, "");

                CharSequence[] itemList = args.getCharSequenceArray(CheckboxDialogFragment.Dialog.CHECKITEMLIST);
                boolean[] checkedFragList = args.getBooleanArray(CheckboxDialogFragment.Dialog.CHECKED_FRAG);

                if (title.isEmpty() || itemList == null || checkedFragList == null ||clickListener == null) {
                    throw new IllegalStateException(MessageFormat.format("You should input title {0} or itemList or checkedFragList clickListener", title));
                }

                CheckboxDialogFragment.Dialog checkboxDialog = new CheckboxDialogFragment.Dialog();
                checkboxDialog.setArguments(args);
                checkboxDialog.setOnPositiveButtonClickListener(clickListener);
                checkboxDialog.show(activity.getSupportFragmentManager(), "checkboxDialog");
                break;
            case TYPE_DELAY_TIME_SELECT:

                break;
            default:
                break;
        }
    }
}
