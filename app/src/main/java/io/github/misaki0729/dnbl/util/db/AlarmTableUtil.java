package io.github.misaki0729.dnbl.util.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.github.misaki0729.dnbl.entity.db.Alarm;

public class AlarmTableUtil {
    public List<Alarm> getRecord() {
        List<Alarm> records = new ArrayList<>();

        ActiveAndroid.beginTransaction();
        try {
            records = new Select()
                    .from(Alarm.class)
                    .execute();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return records;
    }

    public Alarm getRecord(long alarmId) {
        Alarm item = new Alarm();

        ActiveAndroid.beginTransaction();
        try {
            item = new Select()
                    .from(Alarm.class)
                    .where(Alarm.ALARM_ID + "= ?", alarmId)
                    .executeSingle();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return item;
    }

    public long insertRecord(Alarm item) {
        Alarm insertRecord = new Alarm();

        ActiveAndroid.beginTransaction();
        try {
            insertRecord.hour = item.hour;
            insertRecord.minute = item.minute;
            insertRecord.description = item.description;
            insertRecord.is_enable = item.is_enable;

            insertRecord.save();
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return insertRecord.getId();
    }

    public void updateRecord(Alarm item) {
        ActiveAndroid.beginTransaction();
        try {
            item.save();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void deleteRecord(Alarm item) {
        ActiveAndroid.beginTransaction();
        try {
            item.delete();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
