package io.github.misaki0729.dnbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.notification.AlarmReciever;
import io.github.misaki0729.dnbl.service.PlaySoundService;

public class PlayAlarmActivity extends AppCompatActivity {

    @BindView(R.id.stop)
    MaterialButton stopButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);

        Log.d("PlayAlarmActivity", "onCreate");

        Intent startServiceIntent = init(getIntent());
        startService(startServiceIntent);
    }

    private Intent init(Intent intent) {
        long alarmId = intent.getLongExtra(AlarmReciever.ALARM_ID, -1);
        Intent startServiceIntent = new Intent(this, PlaySoundService.class);
        if (alarmId != -1) {
            return startServiceIntent.putExtra(PlaySoundService.ALARM_ID, alarmId);
        }

        return startServiceIntent;
    }

    @OnClick({R.id.stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stop:
                stopService(new Intent(PlayAlarmActivity.this, PlaySoundService.class));
                break;
            default:
                break;
        }
    }
}
