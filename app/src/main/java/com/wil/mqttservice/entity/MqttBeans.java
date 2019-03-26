package com.wil.mqttservice.entity;

import androidx.annotation.NonNull;

/**
 * desc
 *
 * @author will
 * Created time 2019/3/26 10:12 PM.
 */
public class MqttBeans {

    /**
     * temperature : 28
     * wind : 7
     * motor_led : 1
     */

    private int path;
    private int temperature;
    private int wind;
    private int motor_led;

    public MqttBeans(int path, int temperature, int wind, int motor_led) {
        this.path = path;
        this.temperature = temperature;
        this.wind = wind;
        this.motor_led = motor_led;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public int getMotor_led() {
        return motor_led;
    }

    public void setMotor_led(int motor_led) {
        this.motor_led = motor_led;
    }
}
