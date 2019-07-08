package io.github.misaki0729.dnbl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;
import io.github.misaki0729.dnbl.activity.AlarmAddActivity;
import io.github.misaki0729.dnbl.adapter.AlarmListItemAdapter;
import io.github.misaki0729.dnbl.entity.AlarmListItem;
import io.github.misaki0729.dnbl.entity.db.Alarm;
import io.github.misaki0729.dnbl.util.db.AlarmTableUtil;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    ListView alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                Intent intent = new Intent(MainActivity.this, AlarmAddActivity.class);
                startActivity(intent);

                break;
        }
        return true;
    }
}
