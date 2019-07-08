package io.github.misaki0729.dnbl.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.fragment.CheckboxDialogFragment;
import io.github.misaki0729.dnbl.fragment.DialogFragmentController;
import io.github.misaki0729.dnbl.util.DateUtil;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class AlarmEditActivity extends AppCompatActivity {

    public static final String ALARM_ID = "alarm_id";

    @BindView(R.id.alarm_time_picker)
    TimePicker timePicker;

    @BindView(R.id.edittext_alarm_description)
    TextInputEditText description_edit_text;

    @BindView(R.id.setting_alarm_music)
    MaterialButton alarm_music_button;

    @BindView(R.id.setting_alarm_dow)
    MaterialButton alarm_dow_button;

    @BindView(R.id.setting_delay_alarm_time)
    MaterialButton delay_alarm_time_button;

    private long alarmId = -1;
    private int settingDow[] = {0, 1, 2, 3, 4, 5, 6};
    private int settingDelayAlarmTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        init(getIntent());
    }

    private void init(Intent intent) {
        alarmId = intent.getLongExtra(ALARM_ID, -1);

        if (alarmId == -1) return;

        Alarm alarm = new AlarmTableUtil().getRecord(alarmId);
        timePicker.setCurrentHour(alarm.hour);
        timePicker.setCurrentMinute(alarm.minute);

        if (alarm.description != null) description_edit_text.setText(alarm.description);

        //TODO: あとで追加
    }

    private void displayCheckDowDialog() {
        Bundle args = new Bundle();
        args.putString(CheckboxDialogFragment.Dialog.TITLE, "曜日選択");
        args.putCharSequenceArray(CheckboxDialogFragment.Dialog.CHECKITEMLIST, DateUtil.getDow());
        args.putBooleanArray(CheckboxDialogFragment.Dialog.CHECKED_FRAG, getCheckedDow());

        DialogInterface.OnClickListener clickListener = (dialog, which) -> {
            dialog.dismiss();
        };

        new DialogFragmentController(this, DialogFragmentController.TYPE_DOW_CHECK, args, clickListener).show();
    }

    private boolean[] getCheckedDow() {
        boolean[] checkedDow = new boolean[settingDow.length];
        for (int i = 0; i < settingDow.length; i++)
            checkedDow[i] = settingDow[i] != -1;

        return checkedDow;
    }

    @OnClick({R.id.setting_alarm_dow, R.id.setting_delay_alarm_time, R.id.setting_alarm_music})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_alarm_dow:
                displayCheckDowDialog();
                break;
            case R.id.setting_delay_alarm_time:
                break;
            case R.id.setting_alarm_music:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alarm_add_done:
                AlarmTableUtil util = new AlarmTableUtil();

                Alarm alarm = alarmId == -1 ? new Alarm() : util.getRecord(alarmId);

                String description = description_edit_text.getText().toString();
                String dow = Arrays.toString(settingDow);

                alarm.is_enable = true;
                alarm.description = description;
                alarm.alarm_delay_time = settingDelayAlarmTime;
                alarm.alarm_music_id_normal = 0;
                alarm.hour = timePicker.getCurrentHour();
                alarm.minute = timePicker.getCurrentMinute();
                alarm.dow = dow;

                if (alarmId != -1) util.updateRecord(alarm);
                else util.insertRecord(alarm);

                finish();

                break;
        }
        return true;
    }
}
