package io.github.misaki0729.dnbl.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.adapter.TrainListItemAdapter;
import io.github.misaki0729.dnbl.entity.TrainListItem;
import io.github.misaki0729.dnbl.entity.db.Subway;
import io.github.misaki0729.dnbl.util.Preferences;
import io.github.misaki0729.dnbl.util.db.SubwayTableUtil;

public class TrainConfigActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    ListView trainList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_config);
        setTitle("路線選択");
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    private void init() {
        trainList = (ListView) findViewById(R.id.train_list);
        trainList.setOnItemClickListener(this);

        SubwayTableUtil util = new SubwayTableUtil();
        List<Subway> records = util.getRecord();

        TrainListItemAdapter adapter = new TrainListItemAdapter(this, TrainListItem.createTrainListItemList(records));
        trainList.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        TrainListItem item = (TrainListItem) listView.getAdapter().getItem(position);

        new Preferences().setSubwayId(item.getSubwayId());
        finish();
    }
}
