package com.admin.lightsensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 方向加速度：使用加速度传感器和地磁传感器共同实现。
 * 此模拟简易指南针功能。
 * Created by admin on 2017/12/26.
 */

public class ActOrientationSensor extends AppCompatActivity {

    @BindView(R.id.img_compass)
    ImageView imgCompass;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;

    private SensorManager sensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_orientationsensor);
        ButterKnife.bind(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorEventListener, magneticSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];
        private float lastRotateDegree;
        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //注意赋值时要调用clone()方法,
                //不然accelerometerValues和magneticValues将会指向同一个引用
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                //注意赋值时要调用clone()方法,
                //不然accelerometerValues和magneticValues将会指向同一个引用
                magneticValues = event.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            //为R数组赋值
            SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);
            //为values数组赋值，values中就已经包含手机在所有方向上旋转的弧度了
            SensorManager.getOrientation(R, values);
            //1.values[0]表示手机围绕Z轴旋转的弧度；
            //2.values[0]的取值范围为-180度到180度，
            //正负180度表示正南方向，0度表示正北方向，
            //-90度表示正西方向，90度表示正东方向；
            //3.Math.toDegrees()把弧度转换成角度；
            //将计算出的旋转角度取反，用于旋转指南针背景图
            float rotateDegree = -(float)Math.toDegrees(values[0]);
            if(Math.abs(rotateDegree - lastRotateDegree) > 1){
                RotateAnimation animation = new RotateAnimation(
                        lastRotateDegree, rotateDegree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                imgCompass.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
}
