package com.jian.collection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jian.collection.entity.Beacon;
import com.jian.collection.entity.Data;
import com.jian.collection.service.BeaconService;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.collection.utils.XXTEA;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.MapTools;

public class HandleSocket implements Runnable{
	
	private Socket socket;
	private DataService dservice;
	private BeaconService bservice;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean connect = false;
    private String sn = "PCM01000000";
    private String secretKey = "666PCM01";
    
    private Logger logger = LoggerFactory.getLogger(HandleSocket.class);


    /**
     * 注册socket到map里
     *
     * @param socket
     * @return
     */
    public static HandleSocket register(Socket socket, DataService dservice, BeaconService bservice) {
        HandleSocket client = new HandleSocket();
        try {
            client.setSocket(socket);
            client.setDservice(dservice);
            client.setBservice(bservice);
            client.setInputStream(new DataInputStream(socket.getInputStream()));
            client.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            client.setConnect(true);
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
    	send(str.getBytes());
    }
    
    public void send(byte[] data) {
        try {
            outputStream.write(data);
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
        	byte[] bytes = new byte[128];
			int len = 0;
			while ((len = inputStream.read(bytes)) != -1) {
				handleReceive(new String(bytes));
			}
        } catch (IOException e) {
        	logger.error(e.getMessage());
            logout();
        }
        return null;
    }
    
    
    public void handleSend(String sn, int funCode){
    	switch (funCode) {
		case 1:
			handleSendQueryData(sn);
			break;
		case 101:
			handleSendQuerySN(sn);
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
    
    public void handleReceive(String recStr){
    	
		System.out.println("接收指令结果");
		System.out.println("原始数据：" + recStr);
    	//解析接收到的字符串
    	String dataStr = new String(XXTEA.decrypt(recStr.getBytes(), secretKey.getBytes())).trim();
		System.out.println("解密数据：" + dataStr);
		String[] dataArray = dataStr.split(">");
		if(dataArray.length < 2) {
			return;
		}
		String funCode = dataArray[1];
		switch (funCode) {
		case "1": //查询检测数据
			handleReceiveQueryData(dataArray);
			break;
		case "101": //查询序列号
			handleReceiveQuerySN(dataArray);
			break;

		default:
			break;
		}
    }
    
    public void handleReceiveQuerySN(String[] dataArray){
		System.out.println("解析查询序列号数据...");
		System.out.println("序列号：" + dataArray[0]);
		System.out.println("funCode：" + dataArray[1]);
		System.out.println("Flag：" + dataArray[2]);
		
		sn = dataArray[0];
		//存入map
		DelongServerSocket.handleMap.put(sn, this);
		//修改数据库
		Beacon beacon = bservice.findOne(MapTools.custom().put("sn", sn).build());
		if(beacon == null) {
			beacon = new Beacon();
			beacon.setName("");
			beacon.setConnected("Y");
			beacon.setSn(sn);
			beacon.setCreatetime(DateTools.formatDate());
			beacon.setPid(Utils.newId());
			beacon.setStatus("Y");
			beacon.setResend(0);
			bservice.add(beacon);
		}else {
			beacon.setConnected("Y");
			beacon.setStatus("Y");
			bservice.modify(beacon);
		}
    } 
    public void handleReceiveQueryData(String[] dataArray){
		System.out.println("解析查询检测数据...");
		System.out.println("序列号：" + dataArray[0]);
		System.out.println("funCode：" + dataArray[1]);
    }
    
	public void saveData(String dataStr){
		logger.info("{} 收到消息： {}", DateTools.formatDate(), dataStr);
		//SN>1>AF>S1>S2>S3>S4>AX>AY>TY>TM>TD>TH>Tm>TS>GS>DXJ>JD>NBW>WD
		//PCM011900001>1>0>99>98>99>99>1.0>6.2>19>03>27>16>35>06>Y>E>5604.051>N>2936.619
		Data obj = new Data();
		String[] str = dataStr.split(">");
		for (int i = 0; i < str.length; i++) {
			obj.setPid(Utils.newId());
			obj.setCreatetime(DateTools.formatDate());
			obj.setSn(str[0]);
			obj.setAf(Integer.parseInt(str[2]));
			obj.setS1(Integer.parseInt(str[3]));
			obj.setS2(Integer.parseInt(str[4]));
			obj.setS3(Integer.parseInt(str[5]));
			obj.setS4(Integer.parseInt(str[6]));
			obj.setAx(Float.parseFloat(str[7]));
			obj.setAy(Float.parseFloat(str[8]));
			obj.setTy(Integer.parseInt(str[9]));
			obj.setTm(Integer.parseInt(str[10]));
			obj.setTd(Integer.parseInt(str[11]));
			obj.setTh(Integer.parseInt(str[12]));
			obj.setTmm(Integer.parseInt(str[13]));
			obj.setTs(Integer.parseInt(str[14]));
			obj.setGs(str[15]);
			obj.setDxj(str[16]);
			obj.setJd(Float.parseFloat(str[17]));
			obj.setNbw(str[18]);
			obj.setWd(Float.parseFloat(str[19]));
			obj.setAct("");
		}
		dservice.add(obj);
	}

    /**
     * 登出操作, 关闭各种流
     */
    public void logout() {
        if (DelongServerSocket.handleMap.containsKey(sn)) {
            DelongServerSocket.handleMap.remove(sn);
        }
        System.out.println("socket 退出。");
        connect = false;
        //修改数据库
  		Beacon beacon = bservice.findOne(MapTools.custom().put("sn", sn).build());
  		if(beacon != null) {
  			beacon.setStatus("N");
  			bservice.modify(beacon);
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
        
        while (connect) {
        	//查询序列号
        	if("PCM01000000".equals(sn)) {
        		handleSend(sn, 101);
        	}
        	//监听
        	receive();
        }

    }

    @Override
    public String toString() {
        return "Client{" +
                "socket=" + socket +
                ", inputStream=" + inputStream +
                ", outputStream=" + outputStream +
                ", sn='" + sn + '\'' +
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

	public boolean isConnect() {
		return connect;
	}

	public void setConnect(boolean connect) {
		this.connect = connect;
	}

	public DataService getDservice() {
		return dservice;
	}

	public void setDservice(DataService dservice) {
		this.dservice = dservice;
	}

	public BeaconService getBservice() {
		return bservice;
	}

	public void setBservice(BeaconService bservice) {
		this.bservice = bservice;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
    
}
