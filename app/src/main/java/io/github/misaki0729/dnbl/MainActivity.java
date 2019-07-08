package io.github.misaki0729.dnbl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import io.github.misaki0729.dnbl.activity.AlarmEditActivity;
import io.github.misaki0729.dnbl.adapter.AlarmListItemAdapter;
import io.github.misaki0729.dnbl.entity.AlarmListItem;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.notification.AlarmReciever;
import io.github.misaki0729.dnbl.util.ApplicationController;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_EDIT_ALARM = 1;

    ListView alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_ALARM) {
            if (resultCode == RESULT_OK) {
                long alarmId = data.getLongExtra(AlarmEditActivity.ALARM_ID, -1);
                if (alarmId != -1) {
                    Alarm alarm = new AlarmTableUtil().getRecord(alarmId);
                    registerAlarm(alarm.alarm_set_time_millis);
                    Log.d("SetAlarm", String.valueOf(alarm.alarm_set_time_millis));
                }
            }
        }
    }

    private void init() {
        alarmList = (ListView)findViewById(R.id.alarm_list);
        alarmList.setOnItemClickListener(this);

        AlarmTableUtil util = new AlarmTableUtil();
        List<Alarm> records = util.getRecord();

        Log.d("Debug", String.valueOf(records.size()));

        AlarmListItemAdapter adapter = new AlarmListItemAdapter(this, AlarmListItem.createAlarmListItemList(records));
        alarmList.setAdapter(adapter);
    }

    private void registerAlarm(long alarmTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), pendingIntent);
    }

    private void unregister() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, AlarmReciever.class);
        intent.setClass(this, AlarmReciever.class);
        // 複数のアラームを登録する場合はPendingIntent.getBroadcastの第二引数を変更する
        // 第二引数が同じで第四引数にFLAG_CANCEL_CURRENTがセットされている場合、2回以上呼び出されたときは
        // あとからのものが上書きされる
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        AlarmListItem item = (AlarmListItem) listView.getAdapter().getItem(position);

        Log.d("MainActivity", String.valueOf(item.getAlarmId()));

        Intent intent = new Intent(MainActivity.this, AlarmEditActivity.class);
        intent.putExtra(AlarmEditActivity.ALARM_ID, item.getAlarmId());
        startActivityForResult(intent, REQUEST_CODE_EDIT_ALARM);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alarm_add:
                Intent intent = new Intent(MainActivity.this, AlarmEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ALARM);

                break;
        }
        return true;
    }
}
