package com.jian.collection.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.config.Config;
import com.jian.collection.mqtt.MsgSend;
import com.jian.collection.utils.XXTEA;

@Service
public class HandleSendDataService {
	
	@Autowired
    private Config config;
    @Autowired
	private MsgSend msgSend;
    
    
    /**
     * 	发送数据
     *
     * @param str
     */
    public void send(byte[] data) {
    	msgSend.sendToMqtt(data);
    }
    
    public void send(String str) {
    	send(XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes()));
    }
    

    public void handleSend(String sn, int funCode){
    	switch (funCode) {
		case 1:
			handleSendQueryData(sn);
			break;
		case 101:
			handleSendQuerySN(sn);
			break;
		case 80:
			handleSendQueryPic(sn);
			break;

		default:
			break;
		}
    }
    
    public void handleSendQuerySN(String sn){
		System.out.println("发送查询序列号指令。。。。。");
		String str = sn+">101";
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryData(String sn){
		System.out.println("发送查询检测数据指令。。。。。");
		String str = sn +">1";
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryPic(String sn){
		System.out.println("发送查询图片数据指令。。。。。");
		String str = sn +">80";
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    
}
