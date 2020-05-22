package com.jian.collection.data;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.config.Config;
import com.jian.collection.mqtt.MsgSend;
import com.jian.collection.utils.XXTEA;

@Service
public abstract class HandleSendDataService {
	
	@Autowired
    private Config config;
	
    private boolean connect = false;
    private String sn = "PCM010000000";
    private String secretKey = "666PCM01";
    private boolean isOraginalSN = true;
    private Timer timer = null;
    private int tryTime = 10;
    private int sleepTime = 60 * 1000; //采集频率一分钟一次
    
    public static ConcurrentHashMap<String, String> handleMap = new ConcurrentHashMap<>();
    
    private Logger logger = LoggerFactory.getLogger(HandleSendDataService.class);


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
    	send(XXTEA.encrypt(str.getBytes(), secretKey.getBytes()));
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
		byte[] data = XXTEA.encrypt(str.getBytes(), secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryData(String sn){
		System.out.println("发送查询检测数据指令。。。。。");
		String str = sn +">1";
		byte[] data = XXTEA.encrypt(str.getBytes(), secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryPic(String sn){
		System.out.println("发送查询图片数据指令。。。。。");
		String str = sn +">80";
		byte[] data = XXTEA.encrypt(str.getBytes(), secretKey.getBytes());
		send(data);
    }
    
    
}
