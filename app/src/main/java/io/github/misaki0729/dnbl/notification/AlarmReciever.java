package io.github.misaki0729.dnbl.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.github.misaki0729.dnbl.MainActivity;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivityIntent);
    }
}
