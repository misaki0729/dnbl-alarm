package io.github.misaki0729.dnbl.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private SharedPreferences data;

    private String KEY_SUBWAY = "subway";

    public Preferences() {
        data = PreferenceManager.getDefaultSharedPreferences(ApplicationController.getsInstance().getApplicationContext());
    }

    public long getSubwayId() {
        return data.getLong(KEY_SUBWAY, -1);
    }

    public void setSubwayId(long subwayId) {
        data.edit().putLong(KEY_SUBWAY, subwayId).apply();
    }
}
