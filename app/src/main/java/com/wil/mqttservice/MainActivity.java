package com.wil.mqttservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wil.mqttservice.entity.MqttBeans;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MqttUtils utils;
    private TextView mTemp, mWind, mLed;
    int temp = 0, wind = 0, led = 0;
    Gson gson;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                MqttBeans beans = gson.fromJson(msg.obj.toString(), MqttBeans.class);
                mTemp.setText("温度=" + beans.getTemperature());
                mLed.setText("Led灯状态=" + (beans.getMotor_led() == MqttUtils.motor_led_close ? "关" : "开"));
                mWind.setText("风速=" + beans.getWind());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTemp = findViewById(R.id.textView);
        mWind = findViewById(R.id.textView2);
        mLed = findViewById(R.id.textView3);
        gson = new Gson();


        findViewById(R.id.butto3).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

    }

    public void init() {
        utils = new MqttUtils();
        new Thread(new Runnable() {
            @Override
            public void run() {
                utils.init(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        if (message != null) {
                            Message message1 = Message.obtain();
                            message1.obj = message.toString();
                            mHandler.sendMessage(message1);
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.butto3) {
            init();
        }
        if (utils != null) {
            if (v.getId() == R.id.button) {
                temp = temp+1;
                utils.sendMessage(temp, wind, led);
            } else if (v.getId() == R.id.button2) {
                if (temp >0) {
                    temp = temp - 1;
                }
                utils.sendMessage(temp, wind, led);
            } else if (v.getId() == R.id.button3) {
                wind = wind+1;
                utils.sendMessage(temp, wind, led);
            } else if (v.getId() == R.id.button4) {
                if (wind>0){
                    wind = wind-1;
                }
                utils.sendMessage(temp, wind, led);
            } else if (v.getId() == R.id.button6) {
                led = MqttUtils.motor_led_close;
                utils.sendMessage(temp, wind, led);
            } else if (v.getId() == R.id.button5) {
                led = MqttUtils.motor_led_open;
                utils.sendMessage(temp, wind, led);
            }
        } else {
            Toast.makeText(MainActivity.this, "请先初始化", Toast.LENGTH_SHORT).show();
        }
    }
}
