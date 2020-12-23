package com.jian.collection.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.config.Config;
import com.jian.collection.mqtt.MsgSend;
import com.jian.collection.utils.XXTEA;

@Service
public class HandleSendDataService {
	
	@Autowired
    private Config config;
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
    	send(XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes()));
    }
    

    public void handleSend(String sn, int funCode){
    	switch (funCode) {
		case InstructionCode.QueryData:
			handleSendQueryData(sn);
			break;
		case InstructionCode.QuerySN:
			handleSendQuerySN(sn);
			break;
		case InstructionCode.QueryPic:
			handleSendQueryPic(sn);
			break;

		default:
			break;
		}
    }
    
    public void handleSendQuerySN(String sn){
		String str = sn + ">" + InstructionCode.QuerySN;
		System.out.println("发送查询序列号指令。。。。。"+str);
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryData(String sn){
		String str = sn + ">" + InstructionCode.QueryData;
		System.out.println("发送查询检测数据指令。。。。。"+str);
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    public void handleSendQueryPic(String sn){
		String str = sn + ">" + InstructionCode.QueryPic;
		System.out.println("发送查询图片数据指令。。。。。"+str);
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
    public void handleSendSetting(String sn, int s1, int s2, int s3, int s4, float ax, float ay){
		String str = sn + ">" + InstructionCode.Setting + ">" + s1 + ">" + s2 + ">" + s3 + ">" + s4 + ">" + ax + ">" + ay + ">Y";
		System.out.println("发送设置配置指令。。。。。"+str);
		byte[] data = XXTEA.encrypt(str.getBytes(), config.secretKey.getBytes());
		send(data);
    }
    
}
