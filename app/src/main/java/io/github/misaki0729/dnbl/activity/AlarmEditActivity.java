package io.github.misaki0729.dnbl.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
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
import io.github.misaki0729.dnbl.event.SelectDialogEvent;
import io.github.misaki0729.dnbl.fragment.CheckboxDialogFragment;
import io.github.misaki0729.dnbl.fragment.DialogFragmentController;
import io.github.misaki0729.dnbl.fragment.SelectDialogFragment;
import io.github.misaki0729.dnbl.util.DateUtil;
import io.github.misaki0729.dnbl.util.RingtoneUtil;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class AlarmEditActivity extends AppCompatActivity {

    public static final String ALARM_ID = "alarm_id";
    public static final String URI = "URI";
    private static final int REQUEST_CODE_RINGTONE_PICKER = 1;

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
    private Uri uri = null;
    private int settingDow[] = {1, 1, 1, 1, 1, 1, 1};
    private int settingDelayAlarmTime = 0;
    private int settingDelayAlarmTimeList[] = {0, 5, 10, 15, 20, 25, 30, 60};

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieveSelectDelayTime(SelectDialogEvent e) {
        settingDelayAlarmTime = e.getSelectItem();
        delay_alarm_time_button.setText(DateUtil.getDelayTimeText(settingDelayAlarmTimeList[settingDelayAlarmTime]));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_edit);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        init(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RINGTONE_PICKER) {
            if (resultCode == RESULT_OK) {
                uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                if (uri != null) {
                    alarm_music_button.setText(RingtoneUtil.getRingtoneTitle(uri));
                }
            }
        }
    }


    private void init(Intent intent) {
        alarmId = intent.getLongExtra(ALARM_ID, -1);

        if (alarmId == -1) {
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            alarm_music_button.setText(RingtoneUtil.getRingtoneTitle(uri));
            return;
        }

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

        settingDelayAlarmTime = alarm.alarm_delay_time;
        delay_alarm_time_button.setText(DateUtil.getDelayTimeText(settingDelayAlarmTimeList[settingDelayAlarmTime]));

        uri = Uri.parse(alarm.alarm_music_id_normal);
        alarm_music_button.setText(RingtoneUtil.getRingtoneTitle(uri));
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
        args.putInt(SelectDialogFragment.Dialog.SELECT_ITEM, settingDelayAlarmTime);

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
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                startActivityForResult(intent, REQUEST_CODE_RINGTONE_PICKER);
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

                alarm.description = description;
                alarm.alarm_delay_time = settingDelayAlarmTime;
                alarm.alarm_music_id_normal = uri.toString();
                alarm.hour = timePicker.getCurrentHour();
                alarm.minute = timePicker.getCurrentMinute();
                alarm.dow = dow;

                if (alarmId != -1) util.updateRecord(alarm);
                else alarmId = util.insertRecord(alarm);

                Intent intent = new Intent();
                intent.putExtra(ALARM_ID, alarmId);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
        return true;
    }
}
