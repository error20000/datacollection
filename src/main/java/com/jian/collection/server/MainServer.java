package com.jian.collection.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jian.collection.config.Config;
import com.jian.collection.entity.Data;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.tools.core.DateTools;

@Component
public class MainServer {
	
	private ExecutorService pool = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));
	private ServerSocket server;
	
	private Logger logger = LoggerFactory.getLogger(MainServer.class);

	@Autowired
	private Config config;
	@Autowired
	private DataService service;

	public void start(){
		try {
			server = new ServerSocket(config.socket_port);
			
			System.out.println("Server Socket Start on " + config.socket_port + " ...");
			
			while (true) {
			      Socket socket = server.accept();
			      //启动线程
			      pool.submit(new Runnable() {
					
					@Override
					public void run() {
						handle(socket);
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handle(Socket socket){
		try {
			socket.setKeepAlive(true);
			// 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
			InputStream in = socket.getInputStream();
			byte[] bytes = new byte[1024];
			int len;
			StringBuilder sb = new StringBuilder();
			//只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
			while ((len = in.read(bytes)) != -1) {
				// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				sb.append(new String(bytes, 0, len, "UTF-8"));
			}
			System.out.println("original: " + sb);
			
			//解析接收到的字符串
			String dataStr = "PCM011900003>01>99>98>99>99>1.0>6.2>19>03>27>16>35>06>Y>E>5604.051>N>2936.619";
			String fun = "";
			switch (fun) {
			case "01":
				saveData(dataStr);
				break;

			default:
				break;
			}
			
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void queryData(String sn){
		String query = sn + ">01";
	}
	
	public void saveData(String dataStr){
		logger.info("{} 收到消息： {}", DateTools.formatDate(), dataStr);
		//SN>01>S1>S2>S3>S4>AX>AY>TY>TM>TD>TH>Tm>TS>GS>DXJ>JD>NBW>WD
		//PCM011900001>01>99>98>99>99>1.0>6.2>19>03>27>16>35>06>Y>E>5604.051>N>2936.619
		Data obj = new Data();
		String[] str = dataStr.split(">");
		for (int i = 0; i < str.length; i++) {
			obj.setPid(Utils.newId());
			obj.setCreatetime(DateTools.formatDate());
			obj.setSn(str[0]);
			obj.setS1(Integer.parseInt(str[2]));
			obj.setS2(Integer.parseInt(str[3]));
			obj.setS3(Integer.parseInt(str[4]));
			obj.setS4(Integer.parseInt(str[5]));
			obj.setAx(Float.parseFloat(str[6]));
			obj.setAy(Float.parseFloat(str[7]));
			obj.setTy(Integer.parseInt(str[8]));
			obj.setTm(Integer.parseInt(str[9]));
			obj.setTd(Integer.parseInt(str[10]));
			obj.setTh(Integer.parseInt(str[11]));
			obj.setTmm(Integer.parseInt(str[12]));
			obj.setTs(Integer.parseInt(str[13]));
			obj.setGs(str[14]);
			obj.setDxj(str[15]);
			obj.setJd(Float.parseFloat(str[16]));
			obj.setNbw(str[17]);
			obj.setWd(Float.parseFloat(str[18]));
			obj.setAct("");
		}
		service.add(obj);
	}
	
}
