package io.github.misaki0729.dnbl.util.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import io.github.misaki0729.dnbl.entity.db.Subway;

public class SubwayTableUtil {
    public List<Subway> getRecord() {
        List<Subway> records = new ArrayList<>();

        ActiveAndroid.beginTransaction();
        try {
            records = new Select()
                    .from(Subway.class)
                    .execute();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return records;
    }

    public Subway getRecord(long subwayId) {
        Subway item = new Subway();

        ActiveAndroid.beginTransaction();
        try {
            item = new Select()
                    .from(Subway.class)
                    .where(Subway.SUBWAY_ID + "= ?", subwayId)
                    .executeSingle();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return item;
    }

    public Subway getRecord(String name) {
        Subway item = new Subway();

        ActiveAndroid.beginTransaction();
        try {
            item = new Select()
                    .from(Subway.class)
                    .where(Subway.NAME + "= ?", name)
                    .executeSingle();

            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActiveAndroid.endTransaction();
        }

        return item;
    }

    public void updateRecord(Subway item) {
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
}
