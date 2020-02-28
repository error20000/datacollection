package com.jian.collection.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.jian.collection.utils.XXTEA;

public class TestServer {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("error20000.zicp.vip", 13379);  
			//Socket socket = new Socket("127.0.0.1", 80);    
			
			// 建立连接后获得输出流
		    OutputStream outputStream = socket.getOutputStream();
		    String message = "5555555555";
		    outputStream.write(message.getBytes("UTF-8"));
		    //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
		    //socket.shutdownOutput();
		    new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						InputStream inputStream = socket.getInputStream();
						byte[] b = new byte[128];
						inputStream.read(b);
						System.out.println("收到服务器原始数据:"+new String(b));
						System.out.println("解密数据:"+ new String(XXTEA.decrypt(b, "666PCM01".getBytes())).trim() );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		    
		    Thread.sleep(5*1000);
		    outputStream.write(message.getBytes("UTF-8"));
		    Thread.sleep(5*1000);
		    outputStream.write(message.getBytes("UTF-8"));
		    Thread.sleep(5*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
