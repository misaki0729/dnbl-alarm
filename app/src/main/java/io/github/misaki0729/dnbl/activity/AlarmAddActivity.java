package io.github.misaki0729.dnbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.misaki0729.dnbl.MainActivity;
import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class AlarmAddActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_alarm_add);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alarm_add_done:
                AlarmTableUtil util = new AlarmTableUtil();

                Alarm alarm = new Alarm();
                if (alarmId != -1) alarm = util.getRecord(alarmId);

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
