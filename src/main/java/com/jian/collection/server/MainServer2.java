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

public class MainServer2 {
	
	private ExecutorService pool = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));
	private ServerSocket server;
	
	private Logger logger = LoggerFactory.getLogger(MainServer2.class);


	public void start(){
		try {
			server = new ServerSocket(80);
			
			System.out.println("Server Socket Start on 80 ...");
			
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
				break;

			default:
				break;
			}
			
			socket.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MainServer2 ma = new MainServer2();
		ma.start();
	}
	
}
