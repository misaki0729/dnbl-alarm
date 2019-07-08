package io.github.misaki0729.dnbl.entity;

import java.util.ArrayList;
import java.util.List;

import io.github.misaki0729.dnbl.entity.db.Alarm;

public class AlarmListItem {
    private long alarmId;
    private int hour;
    private int minute;
    private String description;
    private boolean isEnabled;

    public AlarmListItem(long alarmId, int hour, int minute, String description, boolean isEnabled) {
        this.alarmId = alarmId;
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.isEnabled = isEnabled;
    }

    public long getAlarmId() {
        return alarmId;
    }
    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public static List<AlarmListItem> createAlarmListItemList(List<Alarm> alarms) {
        List<AlarmListItem> list = new ArrayList<>();

        for (Alarm item: alarms) {
            AlarmListItem alarmListItem = new AlarmListItem(item.getAlarm_id(), item.hour, item.minute, item.description, item.is_enable);
            list.add(alarmListItem);
        }

        return list;
    }
}
