package com.jian.collection;

import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jian.collection.config.Config;
import com.jian.collection.mqtt.MsgSend;
import com.jian.collection.utils.XXTEA;

@RunWith(SpringRunner.class)   
@SpringBootTest(classes={App.class})
public class MqttSendTest {
    
	@Autowired
	private MsgSend msgSend;
	@Autowired
	private Config config;

	@Test
	public void Test(){
		msgSend.sendToMqtt("MsgSend test", "test/topic");
		System.out.println("------Test end------");
	}
	

	@Test
	public void Test2(){
		String dataStr = "PCM011900001>1>0>99>98>99>99>1.0>6.2>19>03>27>16>35>06>Y>E>5604.051>N>2936.619>10000";
		byte[] dataByte = dataStr.getBytes();
		byte[] temp = new byte[128];
		System.arraycopy(dataByte, 0, temp, 0, dataByte.length);
		byte[] data = XXTEA.encrypt(temp, config.secretKey.getBytes());
		System.out.println("=====>" + data.length);
		msgSend.sendToMqtt(data);
		System.out.println("------Test2 end------");
	}
	

	@Test
	public void Test3(){
		try {
			
			String message = "PCM011900001>80>19>07>16>10>28>32>";
			byte[] b = message.getBytes("UTF-8");
			
			FileInputStream in = new FileInputStream("C:\\Users\\Administrator\\Documents\\WeChat Files\\QQ449667481\\FileStorage\\File\\2020-02\\photo.dat");
			
			System.out.println(in.available());
			byte[] b2 = new byte[in.available()]; 
			in.read(b2);
			in.close();
			
			byte[] data = new byte[b.length + b2.length]; 
			//System.arraycopy(b, 0, data, 0, b.length);
			//System.arraycopy(b2, 0, data, b.length, b2.length);
			data = b2; //b2已包含头信息
			
			System.out.println("=====>" + data.length);
			msgSend.sendToMqtt(data);
			System.out.println("------Test3 end------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test4(){
		String dataStr = "PCM011900001>3>OK";
		byte[] dataByte = dataStr.getBytes();
		byte[] temp = new byte[128];
		System.arraycopy(dataByte, 0, temp, 0, dataByte.length);
		byte[] data = XXTEA.encrypt(temp, config.secretKey.getBytes());
		System.out.println("=====>" + data.length);
		msgSend.sendToMqtt(data, "test/topic");
		System.out.println("------Test4 end------");
	}
	
}
