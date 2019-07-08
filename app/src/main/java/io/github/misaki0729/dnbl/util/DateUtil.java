package io.github.misaki0729.dnbl.util;

import java.util.ArrayList;
import java.util.List;

public class DateUtil {
    private static String[] dow = {"月", "火", "水", "木", "金", "土", "日"};

    public static String[] getDow() {
        return dow;
    }

    public static String[] getDow(int[] dowCode) {
        List<String> retDow = new ArrayList<>();
        for (int code: dowCode) retDow.add(dow[code]);

        return retDow.toArray(new String[retDow.size()]);
    }

    public static String getDow(int dowCode) {
        return dow[dowCode];
    }
}
