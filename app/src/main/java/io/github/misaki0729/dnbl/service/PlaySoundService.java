package io.github.misaki0729.dnbl.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.util.RingtoneUtil;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class PlaySoundService extends Service implements MediaPlayer.OnCompletionListener {

    public static final String ALARM_ID = "alarm_id";

    Alarm alarm;
    AlarmTableUtil util;
    MediaPlayer mediaPlayer;
    float volume = 0.6f;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();
        util = new AlarmTableUtil();

        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        if (alarmId != -1) {
            alarm = util.getRecord(alarmId);
        }

        Log.d("PlayService", "start");
        play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void play() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setDataSource(this, getSound());
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Uri getSound() {
        if (alarm != null)
            return Uri.parse(alarm.alarm_music_id_normal);
        else
            return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (alarm != null) {
            alarm.is_enable = false;
            util.updateRecord(alarm);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        volume += 0.1f;
        play();
    }
}
