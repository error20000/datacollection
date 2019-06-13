package com.jian.collection.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;
import com.jian.collection.utils.LongJsonDeserializer;
import com.jian.collection.utils.LongJsonSerializer;

@Table("s_beacon")
public class Beacon {

	@PrimaryKey(type=PrimaryKeyType.NORMAL)
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
	private long pid;
	private String name;
	private String sn;
	private String connected;
	private String status;
	private int resend;
	private String createtime;
	


	public long getPid() {
		return pid;
	}


	public void setPid(long pid) {
		this.pid = pid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public String getConnected() {
		return connected;
	}


	public void setConnected(String connected) {
		this.connected = connected;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public int getResend() {
		return resend;
	}


	public void setResend(int resend) {
		this.resend = resend;
	}


	public String getCreatetime() {
		return createtime;
	}


	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
