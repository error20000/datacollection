package com.jian.collection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleSocket implements Runnable{
	
	private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String key;
    private Logger logger = LoggerFactory.getLogger(HandleSocket.class);

    /**
     * 注册socket到map里
     *
     * @param socket
     * @return
     */
    public static HandleSocket register(Socket socket) {
        HandleSocket client = new HandleSocket();
        try {
            client.setSocket(socket);
            client.setInputStream(new DataInputStream(socket.getInputStream()));
            client.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            
            byte[] bytes = new byte[1024];
            client.getInputStream().read(bytes);
            client.setKey(new String(bytes, "utf-8"));
            
            DelongServerSocket.handleMap.put(client.getKey(), client);
            return client;
        } catch (IOException e) {
            client.logout();
        }
        return null;
    }

    /**
     * 发送数据
     *
     * @param str
     */
    public void send(String str) {
        try {
            outputStream.write(str.getBytes());
        } catch (IOException e) {
        	logger.error(e.getMessage());
            logout();
        }
    }

    /**
     * 接收数据
     *
     * @return
     * @throws IOException
     */
    public String receive()  {
        try {
			byte[] bytes = new byte[1024];
			int len;
			StringBuilder sb = new StringBuilder();
			//只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
			while ((len = inputStream.read(bytes)) != -1) {
				// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				sb.append(new String(bytes, 0, len, "UTF-8"));
			}
			System.out.println("original: " + sb);
            return sb.toString();
        } catch (IOException e) {
        	logger.error(e.getMessage());
            logout();
        }
        return null;
    }

    /**
     * 登出操作, 关闭各种流
     */
    public void logout() {
        if (DelongServerSocket.handleMap.containsKey(key)) {
            DelongServerSocket.handleMap.remove(key);
        }

        try {
            socket.shutdownOutput();
            socket.shutdownInput();
        } catch (IOException e) {
        	logger.error(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            	logger.error(e.getMessage());
            }
        }
    }
    
    

    /**
     * 发送数据包, 判断数据连接状态
     *
     * @return
     */
    public boolean isSocketClosed() {
        try {
            socket.sendUrgentData(1);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public void run() {
        // 每过5秒连接一次客户端
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isSocketClosed()) {
                System.out.println("socket is closed.");
                logout();
                break;
            }
        }

    }

    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket +
                ", inputStream=" + inputStream +
                ", outputStream=" + outputStream +
                ", key='" + key + '\'' +
                '}';
    }

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(DataInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public DataOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
    
}
