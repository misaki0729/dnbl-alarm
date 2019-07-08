package io.github.misaki0729.dnbl.util;

import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneUtil {
    public static String getRingtoneTitle(Uri uri) {
        return RingtoneManager
                .getRingtone(ApplicationController.getsInstance().getApplicationContext(), uri)
                .getTitle(ApplicationController.getsInstance().getApplicationContext());
    }
}
