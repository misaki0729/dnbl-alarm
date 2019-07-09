package io.github.misaki0729.dnbl.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.github.misaki0729.dnbl.R;

public class TrainConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_config);
        setTitle("路線選択");
    }
}
