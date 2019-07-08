package io.github.misaki0729.dnbl.util;

import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import java.util.Calendar;

public class RingtoneUtil {
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
