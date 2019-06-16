package com.jian.collection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jian.collection.entity.Data;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.collection.utils.XXTEA;
import com.jian.tools.core.DateTools;

public class HandleSocket implements Runnable{
	
	private Socket socket;
	private DataService service;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String key;
    private Logger logger = LoggerFactory.getLogger(HandleSocket.class);
    
    private String secretKey = "666PCM01";

    /**
     * 注册socket到map里
     *
     * @param socket
     * @return
     */
    public static HandleSocket register(Socket socket, DataService service) {
        HandleSocket client = new HandleSocket();
        try {
            client.setSocket(socket);
            client.setService(service);
            client.setInputStream(new DataInputStream(socket.getInputStream()));
            client.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            
            //key
            
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
    
    public void handleReceive(){
    	String recStr = receive();
    	//解析接收到的字符串
    	String dataStr = new String(XXTEA.decrypt(recStr.getBytes(), secretKey.getBytes()));
    	String fun = "";
		switch (fun) {
		case "1": //查询检测数据
			saveData(dataStr);
			break;
		case "101": //查询序列号
			saveData(dataStr);
			break;

		default:
			break;
		}
			
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

	public DataService getService() {
		return service;
	}

	public void setService(DataService service) {
		this.service = service;
	}
    
}
