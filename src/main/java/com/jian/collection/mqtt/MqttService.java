package com.jian.collection.mqtt;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.data.AbstractHandleDataService;


@Service
public class MqttService extends AbstractHandleDataService {

	@Autowired
	private MsgSend msgSend;
	
	public void receive(Object obj) {
		byte[] bytes = (byte[]) obj;
		super.receive(new ByteArrayInputStream(bytes));
	}

	@Override
	public void send(byte[] data) {
		msgSend.sendToMqtt(data);
	}
	
}
