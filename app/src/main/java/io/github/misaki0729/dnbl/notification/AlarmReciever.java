package io.github.misaki0729.dnbl.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.misaki0729.dnbl.MainActivity;
import io.github.misaki0729.dnbl.activity.PlayAlarmActivity;

public class AlarmReciever extends BroadcastReceiver {
    public static final String ALARM_ID = "alarm_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, PlayAlarmActivity.class);

        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        startActivityIntent.putExtra(ALARM_ID, alarmId);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }
}
