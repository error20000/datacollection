package com.jian.collection.data;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.config.Config;
import com.jian.collection.mqtt.MsgSend;
import com.jian.collection.utils.XXTEA;

@Service
public class HandleSendTimer {
	
	@Autowired
    private Config config;
	@Autowired
	private HandleSendDataService sendService;
	
    private boolean connect = false;
    private String sn = "PCM010000000";
    private boolean isOraginalSN = true;
    private Timer timer = null;
    private int tryTime = 10;
    private int sleepTime = 60 * 1000; //采集频率一分钟一次
    
    public static ConcurrentHashMap<String, String> handleMap = new ConcurrentHashMap<>();
    
    private Logger logger = LoggerFactory.getLogger(HandleSendTimer.class);


    
    public void start(){
    	//查询序列号
		timer = new Timer(true); 
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(isOraginalSN) {
					sendService.handleSend(sn, 101); 
				}
				//失败次数后，自动退出。
	        	tryTime--;
	        	if(tryTime <= 0) {
	        		timer.cancel();
	        	}
			}
		}, 0, 10 * 1000);
		 
    	//监听
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("启动监听数据线程。。。");
			}
		}).start();
    }
    
    
}
