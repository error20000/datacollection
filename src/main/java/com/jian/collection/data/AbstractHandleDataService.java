package com.jian.collection.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.collection.App;
import com.jian.collection.config.Config;
import com.jian.collection.entity.Beacon;
import com.jian.collection.entity.Data;
import com.jian.collection.service.BeaconService;
import com.jian.collection.service.DataService;
import com.jian.collection.utils.Utils;
import com.jian.collection.utils.XXTEA;
import com.jian.tools.core.DateTools;
import com.jian.tools.core.MapTools;
import com.jian.tools.core.Tools;

@Service
public abstract class AbstractHandleDataService {
	
	@Autowired
    private Config config;
	@Autowired
	private DataService dservice;
	@Autowired
	private BeaconService bservice;
	
    private boolean connect = false;
    private String sn = "PCM010000000";
    private String secretKey = "666PCM01";
    private boolean isOraginalSN = true;
    private Timer timer = null;
    private int tryTime = 10;
    private int sleepTime = 60 * 1000; //采集频率一分钟一次
    
    public static ConcurrentHashMap<String, String> handleMap = new ConcurrentHashMap<>();
    
    private Logger logger = LoggerFactory.getLogger(AbstractHandleDataService.class);
    

    /**
     * 	发送数据
     *
     * @param str
     */
    public abstract void send(byte[] data);
    
    public void send(String str) {
    	send(XXTEA.encrypt(str.getBytes(), secretKey.getBytes()));
    }
    

    /**
     *	 接收数据
     *
     * @return
     * @throws IOException
     */
    public String receive(InputStream inputStream)  {
        try {
        	List<Byte> picByteList = new ArrayList<>();
        	boolean isPic = false;
        	byte[] bytes = new byte[128];
			int len = 0;
			int count = 0;
			while ((len = inputStream.read(bytes)) != -1) {
				System.out.println(len);
				
				System.out.println("接收指令结果");
				System.out.println("原始数据：" + bytes);
				System.out.println("原始数据str：" + new String(bytes));
				
				String dataStr = "";
				String test = new String(bytes);
				//图片流程
				if(test.contains(">80>")) {
					System.out.println("进入处理图片流程。。。");
					//获取全部图片数据
					byte[] res = new byte[bytes.length];
					System.arraycopy(bytes, 0, res, 0, bytes.length);
					//读取剩余数据
					while ((len = inputStream.read(bytes)) != -1) {
						byte[] temp = new byte[len];
						System.arraycopy(bytes, 0, temp, 0, len);
						byte[] temp2 = new byte[res.length + temp.length];
						System.arraycopy(res, 0, temp2, 0, res.length);
						System.arraycopy(temp, 0, temp2, res.length, temp.length);
						res = temp2;
						if(len < 128) {
							break;
						}
					}
					System.out.println("接收图片数据总长度：" + res.length);
					/*System.out.println("开始处理图片。。。");
					
					int lastIndex = test.lastIndexOf(">");
					byte[] temp = new byte[lastIndex + 1]; 
					System.arraycopy(bytes, 0, temp, 0, lastIndex + 1);
					String head = test.substring(0, lastIndex + 1);
					System.out.println("head 长度：" + (lastIndex + 1));
					System.out.println("head str：" + head);
					
					byte[] body = new byte[res.length - (lastIndex + 1)];
					System.arraycopy(res, lastIndex + 1, body, 0, body.length);
					System.out.println("body 长度：" + body.length);*/
					
					dataStr = new String(res, "ISO-8859-1");
					
				//正常流程
				}else {
					//解析接收到的字符串
					dataStr = new String(XXTEA.decrypt(bytes, secretKey.getBytes())).trim();
					System.out.println("解密数据：" + dataStr);
				}
				
				String[] dataArray = dataStr.split(">");
				if(dataArray.length < 2) {
					System.out.println("数据异常：" + dataStr);
					continue;
				}
				String funCode = dataArray[1];
				
				handleReceive(funCode, dataArray);

	        	try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	
	        	//发送查询检测数据指令
				if(!isOraginalSN) {
					handleSend(sn, 1); 
					handleSend(sn, 80); 
				}
			}
        } catch (IOException e) {
        	logger.error(e.getMessage());
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
		case 80:
			handleSendQueryPic(sn);
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
    
    public void handleSendQueryPic(String sn){
		System.out.println("发送查询图片数据指令。。。。。");
		String str = sn +">80";
		byte[] data = XXTEA.encrypt(str.getBytes(), secretKey.getBytes());
		send(data);
    }
    
    
    public void handleReceive(String funCode, String[] dataArray){
		
		switch (funCode) {
		case "1": //查询检测数据
			handleReceiveQueryData(dataArray);
			//启动数据监测Timer
			//new DataCollectionTimer().start();
			break;
		case "101": //查询序列号
			handleReceiveQuerySN(dataArray);
			break;
		case "80": //查询图片
			handleReceiveQueryPic(dataArray);
			break;

		default:
			break;
		}
    }
    
    public void handleReceiveQueryPic(String[] dataArray) {
		System.out.println("解析查询图片数据...");
		System.out.println("序列号：" + dataArray[0]);
		System.out.println("funCode：" + dataArray[1]);
		System.out.println("年：" + dataArray[2]);
		System.out.println("月：" + dataArray[3]);
		System.out.println("日：" + dataArray[4]);
		System.out.println("时：" + dataArray[5]);
		System.out.println("分：" + dataArray[6]);
		System.out.println("秒：" + dataArray[7]);
		System.out.println("length：" + dataArray.length);
		
		String bodyStr = "";
		for (int i = 8; i < dataArray.length; i++) {
			if(i == dataArray.length - 1) {
				bodyStr += dataArray[i];
			}else {
				bodyStr += dataArray[i] + ">";
			}
		}
		try {
			byte[] body = bodyStr.getBytes("ISO-8859-1");
			System.out.println("handleReceiveQueryPic body 长度：" + body.length);
			
			String basePath = Tools.isNullOrEmpty(config.out_static_path) ? App.rootPath + "static/" : config.out_static_path;
			basePath = basePath.endsWith("/") ? basePath : basePath + "/";
			basePath = basePath + "upload/" + dataArray[0] + "/";
			String suffix = dataArray[2]+dataArray[3]+dataArray[4]+dataArray[5]+dataArray[6]+dataArray[7];
			parsePicData(body, 0, basePath, suffix);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    public void parsePicData(byte[] data, int index, String basePath, String suffix) {
    	//图片名称
    	index++;
    	String fileName = basePath + suffix + "_" +index + ".jpg";
    	//test error
		/*byte[] test = new byte[data.length - 2000];
		System.arraycopy(data, 0, test, 0, test.length);
		data = test;*/
    	//获取图片长度
    	byte[] len = new byte[4];
		System.arraycopy(data, 0, len, 0, len.length);
		int lenInt = (int) ( (len[0] & 0xff) | (len[1] & 0xff) << 8 | (len[2] & 0xff) << 16 | (len[3] & 0xff) << 24 );
		System.out.println("pic body length：" + lenInt);
    	//获取图片数据
    	byte[] body = new byte[lenInt];
    	if(data.length < len.length + lenInt) {
			System.out.println("图片数据不全：" + fileName);
    		System.arraycopy(data, len.length, body, 0, data.length - len.length);
    	}else {
    		System.arraycopy(data, len.length, body, 0, body.length);
    	}
    	//保存图片
    	System.out.println(fileName);
    	File file = new File(fileName);
		File pfile = file.getParentFile();
		if(!pfile.exists()){
			pfile.mkdirs();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(body);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  
		//检测是否有多张图片
    	if(data.length > len.length + lenInt) {
    		//剩余数据
        	byte[] other = new byte[data.length - len.length - lenInt];
    		System.arraycopy(data, len.length + lenInt, other, 0, other.length);
    		parsePicData(other, index, basePath, suffix);
    	}
    }
    
    public void handleReceiveQuerySN(String[] dataArray){
		System.out.println("解析查询序列号数据...");
		System.out.println("序列号：" + dataArray[0]);
		System.out.println("funCode：" + dataArray[1]);
		System.out.println("Flag：" + dataArray[2]);
		
		if(!"OK".equalsIgnoreCase(dataArray[2])) {
			return;
		}
		
		sn = dataArray[0];
		isOraginalSN = false;
		//存入map
		handleMap.put(sn, sn);
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
		//成功后退出
		timer.cancel();
    } 
    
    public void handleReceiveQueryData(String[] dataArray){
		System.out.println("解析查询检测数据...");
		System.out.println("序列号：" + dataArray[0]);
		System.out.println("funCode：" + dataArray[1]);
		saveData(dataArray);
    }
    
	public void saveData(String[] str){
		//SN>1>AF>S1>S2>S3>S4>AX>AY>TY>TM>TD>TH>Tm>TS>GS>DXJ>JD>NBW>WD
		//PCM011900001>1>0>99>98>99>99>1.0>6.2>19>03>27>16>35>06>Y>E>5604.051>N>2936.619
		logger.info("{} 收到消息： {}", DateTools.formatDate(), Arrays.stream(str).collect(Collectors.joining(">")));
		Data obj = new Data();
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

    
}
