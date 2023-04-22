package com.example.demoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    ArcGauge arcGaugeTemp, arcGaugeHumi;
    TextView txtTemp, txtLux, txtHumi, motion;

    LabeledSwitch btnLed, btnPump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        txtTemp = findViewById(R.id.txtTemperature);
//        txtHumi = findViewById(R.id.txtHumidity);
//        txtLux = findViewById(R.id.txtLux);
        btnLed = findViewById(R.id.btnLed);
        btnPump = findViewById(R.id.btnPump);
        motion = findViewById(R.id.motiondetect);

        btnLed.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn){
                    sendDataMQTT("NgocManh/feeds/nutnhan1", "1");
                } else {
                    sendDataMQTT("NgocManh/feeds/nutnhan1", "0");
                }
            }
        });

        btnPump.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn){
                    sendDataMQTT("NgocManh/feeds/nutnhan2", "1");
                } else {
                    sendDataMQTT("NgocManh/feeds/nutnhan2", "0");
                }
            }
        });

        // Gauge temp
        arcGaugeTemp = findViewById(R.id.tempgauge);
//        Range range1 = new Range();
//        range1.setFrom(0.0);
//        range1.setTo(15.0);
//        range1.setColor(Color.BLACK);
//
//        Range range2 = new Range();
//        range2.setFrom(15.0);
//        range2.setTo(35.0);
//        range2.setColor(Color.GREEN);
//
//        Range range3 = new Range();
//        range3.setFrom(35.0);
//        range3.setTo(100.0);
//        range3.setColor(Color.RED);
//
//        arcGaugeTemp.addRange(range1);
//        arcGaugeTemp.addRange(range2);
//        arcGaugeTemp.addRange(range3);
//        arcGauge.setValue(Helper.getLastYValue());

        //Gauge
        arcGaugeHumi = findViewById(R.id.humigauge);
//        Range range4 = new Range();
//        range4.setFrom(0.0);
//        range4.setTo(40.0);
//        range4.setColor(Color.BLACK);
//
//        Range range5 = new Range();
//        range5.setFrom(40.0);
//        range5.setTo(70.0);
//        range5.setColor(Color.GREEN);
//
//        Range range6 = new Range();
//        range6.setFrom(71.0);
//        range6.setTo(100.0);
//        range6.setColor(Color.RED);
//
//        arcGaugeHumi.addRange(range4);
//        arcGaugeHumi.addRange(range5);
//        arcGaugeHumi.addRange(range6);

        startMQTT();
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEXT", topic + "***" + message.toString());
                if(topic.contains("cambien1")){
//                    txtTemp.setText(message.toString() + "°C");

                    arcGaugeTemp = findViewById(R.id.tempgauge);
                    Range range1 = new Range();
                    range1.setFrom(0.0);
                    range1.setTo(20.0);
                    range1.setColor(Color.BLACK);

                    Range range2 = new Range();
                    range2.setFrom(21.0);
                    range2.setTo(35.0);
                    range2.setColor(Color.GREEN);

                    Range range3 = new Range();
                    range3.setFrom(36.0);
                    range3.setTo(100.0);
                    range3.setColor(Color.RED);

                    arcGaugeTemp.addRange(range1);
                    arcGaugeTemp.addRange(range2);
                    arcGaugeTemp.addRange(range3);
                    double val = Double.parseDouble(new String(message.toString()));
                    arcGaugeTemp.setValue(val);
                } else if(topic.contains("cambien2")){
                    txtHumi.setText(message.toString() + "Lux");
                } else if (topic.contains("cambien3")) {
//                    txtLux.setText(message.toString() + "%");

                    arcGaugeHumi = findViewById(R.id.humigauge);
                    Range range1 = new Range();
                    range1.setFrom(0.0);
                    range1.setTo(40.0);
                    range1.setColor(Color.BLACK);

                    Range range2 = new Range();
                    range2.setFrom(40.0);
                    range2.setTo(70.0);
                    range2.setColor(Color.GREEN);

                    Range range3 = new Range();
                    range3.setFrom(71.0);
                    range3.setTo(100.0);
                    range3.setColor(Color.RED);

                    arcGaugeHumi.addRange(range1);
                    arcGaugeHumi.addRange(range2);
                    arcGaugeHumi.addRange(range3);
                    double val = Double.parseDouble(new String(message.toString()));
                    arcGaugeHumi.setValue(val);
                } else if(topic.contains("nutnhan1")){
                    if(message.toString().equals("1")){
                        btnLed.setOn(true);
                    } else {
                        btnLed.setOn(false);
                    }
                } else if (topic.contains("nutnhan2")) {
                    if(message.toString().equals("1")){
                        btnPump.setOn(true);
                    } else {
                        btnPump.setOn(false);
                    }
                } else if(topic.contains("ai")){
                    motion.setText(message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}