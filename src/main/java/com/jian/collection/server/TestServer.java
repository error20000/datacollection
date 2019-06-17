package com.jian.collection.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("error20000.zicp.vip", 55412);  
			//Socket socket = new Socket("127.0.0.1", 80);    
			
			// 建立连接后获得输出流
		    OutputStream outputStream = socket.getOutputStream();
		    String message = "5555555555";
		    outputStream.write(message.getBytes("UTF-8"));
		    //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
		    socket.shutdownOutput();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
