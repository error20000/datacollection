package com.jian.collection.server;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectionTimer {
	
	private boolean timerStart = false;
	private Timer timer = null;
	private long delayTime = 0;
	private long runTime = 60 * 1000;
	private Logger logger = LoggerFactory.getLogger(DataCollectionTimer.class);
	
	
	
	public void close(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		timerStart = false;
	}
	
	public void start(){
		if(!timerStart){
			timer = new Timer(true); 
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					try {
						collection();
					} catch (Exception e) {
			        	logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}, delayTime, runTime);
			timerStart = true;
		}
	}
	
	private void collection(){
		
	}
}
