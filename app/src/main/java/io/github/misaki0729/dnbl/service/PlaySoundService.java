package io.github.misaki0729.dnbl.service;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class PlaySoundService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer mediaPlayer;
    float volume = 0.3f;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        volume += 0.1f;
        play();
    }
}
