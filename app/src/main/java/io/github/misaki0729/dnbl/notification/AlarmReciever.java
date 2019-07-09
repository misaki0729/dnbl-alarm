package io.github.misaki0729.dnbl.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.github.misaki0729.dnbl.MainActivity;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class AlarmReciever extends BroadcastReceiver {
    public static final String ALARM_ID = "alarm_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);

        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        if (alarmId != -1) {
            AlarmTableUtil util = new AlarmTableUtil();
            Alarm alarm = util.getRecord(alarmId);
            alarm.is_enable = false;
            util.updateRecord(alarm);
        }

        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }
}
