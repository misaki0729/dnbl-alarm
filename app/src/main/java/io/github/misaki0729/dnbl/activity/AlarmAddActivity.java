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

    private int settingDow[] = {0, 1, 2, 3, 4, 5, 6};
    private int settingDelayAlarmTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        ButterKnife.bind(this);
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
                Alarm addAlarm = new Alarm();

                String description = description_edit_text.getText().toString();
                String dow = Arrays.toString(settingDow);

                addAlarm.is_enable = true;
                addAlarm.description = description;
                addAlarm.alarm_delay_time = settingDelayAlarmTime;
                addAlarm.alarm_music_id_normal = 0;
                addAlarm.hour = timePicker.getCurrentHour();
                addAlarm.minute = timePicker.getCurrentMinute();
                addAlarm.dow = dow;

                new AlarmTableUtil().insertRecord(addAlarm);

                finish();

                break;
        }
        return true;
    }
}
