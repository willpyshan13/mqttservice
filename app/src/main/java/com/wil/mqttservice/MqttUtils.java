package com.wil.mqttservice;

import android.app.NotificationManager;
import android.util.Log;

import com.google.gson.Gson;
import com.wil.mqttservice.entity.MqttBeans;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * desc
 *
 * @author will
 * Created time 2019/3/24 11:23 AM.
 */
public class MqttUtils {

    private String PATH = "path";
    private String TEMPERATURE = "temperature";
    private String WIND = "wind";
    private String MOTOR_LED = "motor_led";

    private String TOPIC = "IOT/report/data";

    public static int path_type_nb = 0;
    public static int path_type_app = 1;
    public static int path_type_server = 2;

    public static int motor_led_close = 0;
    public static int motor_led_open = 1;
    Gson gson;

    private NotificationManager notificationManager;
    private MqttClient sampleClient = null;
    private String[] topicFilters = {"wanlink",TOPIC};//发布或订阅主题，（我要发布或订阅什么消息）
    private String broker = "tcp://www.wan-link.cn:1883";//服务器地址及端口 就是自己的IP（我要去哪里订阅消息）
    private String clientId = "paho13527985776497022";//客户端ID  就是用这个来标识客户端在服务器上的唯一（是谁在服务器上发布或订阅消息）
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttConnectOptions connOpts = new MqttConnectOptions();//连接配置

    public void init(MqttCallback callback){
        try {
            gson = new Gson();
            //创建客户端
            sampleClient = new MqttClient(broker, clientId, persistence);
            connOpts.setCleanSession(false);
            //连接服务器
            sampleClient.connect(connOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        setsubscribe(callback);

    }

    MqttBeans beans;

    public void sendMessage(int temp,int wind,int ledState){
        try {
            beans = new MqttBeans(path_type_app,temp,wind,ledState);
            //创建要发布的消息
            Log.d("sendMessage",gson.toJson(beans));
            MqttMessage message = new MqttMessage(gson.toJson(beans).getBytes());
            message.setQos(1);
            //发布消息
            sampleClient.publish(TOPIC, message);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public void setsubscribe(MqttCallback callback){
        try {
            //获取消息回调
            sampleClient.setCallback(callback);
            //订阅消息
            sampleClient.subscribe(topicFilters, new int[]{1, 1});
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    MqttBeans beans1;
    class MyCallBack implements MqttCallback {

        @Override
        public void messageArrived(String arg0, final MqttMessage arg1) throws Exception {
            System.out.println("MqttMessage:" + arg1.toString());

            beans1 = gson.fromJson(arg0,MqttBeans.class);


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
        }

        @Override
        public void connectionLost(Throwable arg0) {

        }
    }

}
