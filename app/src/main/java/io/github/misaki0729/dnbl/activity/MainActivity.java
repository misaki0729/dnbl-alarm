package io.github.misaki0729.dnbl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.adapter.AlarmListItemAdapter;
import io.github.misaki0729.dnbl.entity.AlarmListItem;
import io.github.misaki0729.dnbl.entity.SubwayDelayInfo;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.network.SubwayDelay;
import io.github.misaki0729.dnbl.util.RingtoneUtil;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;
import rx.Observer;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_EDIT_ALARM = 1;

    ListView alarmList;

    @BindView(R.id.debug_textview)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("アラーム一覧");

        ButterKnife.bind(this);

        SubwayDelay delay = new SubwayDelay();
        delay.setDelayInfo()
                .subscribe(new Observer<List<SubwayDelayInfo.Info>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<SubwayDelayInfo.Info> infos) {
                        String name = "";
                        for (SubwayDelayInfo.Info info: infos)
                            name += info.name + ", ";
                        textView.setText(name);
                    }
                });
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
                    RingtoneUtil.registerAlarm(alarmId);
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
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_alarm_add:
                intent = new Intent(MainActivity.this, AlarmEditActivity.class);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ALARM);

                break;
            case R.id.action_train_config:
                intent = new Intent(MainActivity.this, TrainConfigActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
