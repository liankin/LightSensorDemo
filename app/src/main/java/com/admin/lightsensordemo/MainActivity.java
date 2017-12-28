package com.admin.lightsensordemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_light_sensor)
    Button btnLightSensor;
    @BindView(R.id.btn_accelerate_sensor)
    Button btnAccelerateSensor;
    @BindView(R.id.btn_orientation_sensor)
    Button btnOrientationSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_light_sensor, R.id.btn_accelerate_sensor, R.id.btn_orientation_sensor})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_light_sensor:
                intent = new Intent(MainActivity.this, ActLightSensor.class);
                startActivity(intent);
                break;
            case R.id.btn_accelerate_sensor:
                intent = new Intent(MainActivity.this, ActAccelerateSensor.class);
                startActivity(intent);
                break;
            case R.id.btn_orientation_sensor:
                intent = new Intent(MainActivity.this, ActOrientationSensor.class);
                startActivity(intent);
                break;
        }
    }
}
