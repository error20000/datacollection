package com.jian.collection.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * MQTT发布消息发送接口
 * @author Administrator
 *
 */
@Component
@MessagingGateway(defaultRequestChannel = MqttConfig.CHANNEL_NAME_OUT)
public interface MsgSend {
	
	void sendToMqtt(String data);
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos);
    
	void sendToMqtt(byte[] data);
    void sendToMqtt(byte[] data, @Header(MqttHeaders.TOPIC) String topic);
}
