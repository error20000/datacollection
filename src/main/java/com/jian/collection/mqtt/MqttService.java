package com.jian.collection.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.data.HandleReceiveDataService;


@Service
public class MqttService {

	@Autowired
	private MsgSend msgSend;
	@Autowired
	private HandleReceiveDataService receiveService;
	
	public void receive(Object obj) {
		byte[] bytes = (byte[]) obj;
		receiveService.receive(bytes);
	}

	
}
