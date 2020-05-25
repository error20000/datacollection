package com.jian.collection.mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public class MqttConfig {
	
	@Value("${mqtt.username}")
	private String username;
	@Value("${mqtt.password}")
	private String password;
	@Value("${mqtt.urls}")
	private String[] urls;

	@Value("${mqtt.connection.timeout:30}")
	private int connectionTimeout;
	@Value("${mqtt.keepalive:60}")
	private int keepAlive;

	@Value("${mqtt.subscribe.client.id}")
	private String subscribeId;
	@Value("${mqtt.subscribe.default.topic}")
	private String[] subscribeDefaultTopic;
	@Value("${mqtt.subscribe.completion.timeout:30000}")
	private int subscribeCompletionTimeout;
	@Value("${mqtt.subscribe.qos:0}")
	private int subscribeQos;
	
	@Value("${mqtt.publish.client.id}")
	private String publishId;
	@Value("${mqtt.publish.default.topic}")
	private String publishDefaultTopic;
	
	private Logger logger = LoggerFactory.getLogger(MqttConfig.class);
	//订阅者的信息通道名称
	public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
	//发布者的信息通道名称
	public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";
	
	@Autowired
	private MqttService mqttService;

	/**
	 * Client
	 * @return
	 */
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setUserName(username);
		factory.setPassword(password);
		factory.setServerURIs(urls);
		factory.setKeepAliveInterval(keepAlive);
		factory.setConnectionTimeout(connectionTimeout);
		return factory;
	}

	//TODO -----------------------------------------------------------------------------------发布者
	
	/**
	 * MQTT信息通道（发布者）
	 * @return
	 */
	@Bean(name = CHANNEL_NAME_OUT)
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	/**
	 * MQTT消息处理器（发布者）
	 * @return
	 */
	@Bean
	@ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
	public MessageHandler outbound() {
		String tempId = MqttAsyncClient.generateClientId();
		System.out.println(tempId);
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(publishId, mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(publishDefaultTopic);
		return messageHandler;
	}
	
	//TODO -----------------------------------------------------------------------------------订阅者
	
	/**
	 * MQTT信息通道（订阅者）
	 * @return
	 */
	@Bean(name = CHANNEL_NAME_IN)
	public SubscribableChannel mqttInputChannel() {
		return new DirectChannel();
	}
	
	/**
	 * MQTT消息订阅
	 * @return
	 */
	@Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(subscribeId, mqttClientFactory(), subscribeDefaultTopic);
        DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
		converter.setPayloadAsBytes(true);
        adapter.setConverter(converter);
        adapter.setCompletionTimeout(subscribeCompletionTimeout);
        adapter.setQos(subscribeQos);
        adapter.setOutputChannelName(CHANNEL_NAME_IN);
        return adapter;
    }
	
	/**
	 * MQTT消息处理器（订阅者）
	 * @return
	 */
	@Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
            	logger.info("主题：{}，消息接收到的数据：{}", message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC), message.getPayload());
            	mqttService.receive(message.getPayload());
            }
        };
    }
	
}
