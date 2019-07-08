package io.github.misaki0729.dnbl.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.event.CheckDialogEvent;
import io.github.misaki0729.dnbl.fragment.CheckboxDialogFragment;
import io.github.misaki0729.dnbl.fragment.DialogFragmentController;
import io.github.misaki0729.dnbl.fragment.SelectDialogFragment;
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
    private int settingDow[] = {1, 1, 1, 1, 1, 1, 1};
    private int settingDelayAlarmTime = 10;
    private int settingDelayAlarmTimeList[] = {5, 10, 15, 20, 25, 30, 60};

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveCheckedList(CheckDialogEvent e) {
        String dow = "";
        String[] dowList = DateUtil.getDow();

        int count = 0;
        for (int i = 0; i < e.getCheckedList().length; i++) {
            boolean isChecked = e.getCheckedList()[i];
            if (isChecked) {
                count++;
                dow = dow + dowList[i];
                settingDow[i] = 1;
            } else {
                settingDow[i] = -1;
            }
        }

        if (count == dowList.length) dow = "毎日";
        else if (count == 0) dow = "なし";

        alarm_dow_button.setText(dow);
    }

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

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    private void init(Intent intent) {
        alarmId = intent.getLongExtra(ALARM_ID, -1);

        if (alarmId == -1) return;

        Alarm alarm = new AlarmTableUtil().getRecord(alarmId);
        timePicker.setCurrentHour(alarm.hour);
        timePicker.setCurrentMinute(alarm.minute);

        if (alarm.description != null) description_edit_text.setText(alarm.description);

        String dow = alarm.dow;
        try {
            ObjectMapper mapper = new ObjectMapper();
            settingDow = mapper.readValue(dow, int[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int count = 0;
        dow = "";
        String[] dowList = DateUtil.getDow();
        for (int i = 0; i < settingDow.length; i++) {
            if (settingDow[i] == 1) {
                count++;
                dow = dow + dowList[i];
            }
        }
        if (count == dowList.length) dow = "毎日";
        else if (count == 0) dow = "なし";
        alarm_dow_button.setText(dow);

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

    private void displaySelectDelayTimeDialog() {
        String selectList[] = new String[settingDelayAlarmTimeList.length];
        for (int i = 0; i < settingDelayAlarmTimeList.length; i++)
            selectList[i] = DateUtil.getDelayTimeText(settingDelayAlarmTimeList[i]);


        Bundle args = new Bundle();
        args.putString(SelectDialogFragment.Dialog.TITLE, "時間選択");
        args.putCharSequenceArray(SelectDialogFragment.Dialog.SELECT_LIST, selectList);
        args.putInt(SelectDialogFragment.Dialog.SELECT_ITEM, 0);

        new DialogFragmentController(this, DialogFragmentController.TYPE_DELAY_TIME_SELECT, args, null).show();
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
                displaySelectDelayTimeDialog();
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
