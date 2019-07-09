package io.github.misaki0729.dnbl.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.notification.AlarmReciever;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class RingtoneUtil {

    public static void registerAlarm(long alarmId) {
        Alarm alarm = new AlarmTableUtil().getRecord(alarmId);
        alarm.alarm_set_time_millis = RingtoneUtil.getRingtoneTimeMillis(alarm.hour, alarm.minute);
        alarm.is_enable = true;
        new AlarmTableUtil().updateRecord(alarm);

        AlarmManager alarmManager = (AlarmManager) ApplicationController.getsInstance().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(alarmId);
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.alarm_set_time_millis, null), pendingIntent);
    }

    public static void unregister(long alarmId) {
        AlarmManager alarmManager = (AlarmManager) ApplicationController.getsInstance().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(alarmId));

        Alarm alarm = new AlarmTableUtil().getRecord(alarmId);
        alarm.is_enable = false;
        new AlarmTableUtil().updateRecord(alarm);
    }

    private static PendingIntent getPendingIntent(long alarmId) {
        Intent intent = new Intent(ApplicationController.getsInstance().getApplicationContext(), AlarmReciever.class);
        intent.setClass(ApplicationController.getsInstance().getApplicationContext(), AlarmReciever.class);
        intent.putExtra(AlarmReciever.ALARM_ID, alarmId);
        // 複数のアラームを登録する場合はPendingIntent.getBroadcastの第二引数を変更する
        // 第二引数が同じで第四引数にFLAG_CANCEL_CURRENTがセットされている場合、2回以上呼び出されたときは
        // あとからのものが上書きされる
        return PendingIntent.getBroadcast(ApplicationController.getsInstance().getApplicationContext(), (int) alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public static String getRingtoneTitle(Uri uri) {
        return RingtoneManager
                .getRingtone(ApplicationController.getsInstance().getApplicationContext(), uri)
                .getTitle(ApplicationController.getsInstance().getApplicationContext());
    }

    public static long getRingtoneTimeMillis(int hour, int minute) {

        Calendar cal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        cal.set(getYear(), getMonth(), getDay(), hour, minute, 0);

        Log.d("Year", String.valueOf(getYear()));
        Log.d("TimeMillis", String.valueOf(cal.getTimeInMillis()));
        Log.d("TimeMillis2", String.valueOf(currentCal.getTimeInMillis()));
        if (cal.compareTo(currentCal) <= 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return Long.parseLong(String.format("%10d", cal.getTimeInMillis()));
    }

    public static int getYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.get(Calendar.SECOND);
    }
}
