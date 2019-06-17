package com.jian.collection.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;
import com.jian.collection.utils.LongJsonDeserializer;
import com.jian.collection.utils.LongJsonSerializer;

@Table("s_data")
public class Data {

	@PrimaryKey(type=PrimaryKeyType.NORMAL)
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
	private long pid;
	private int af;
	private String sn;
	private int s1;
	private int s2;
	private int s3;
	private int s4;
	private float ax;
	private float ay;
	private int ty;
	private int tm;
	private int td;
	private int th;
	private int tmm;
	private int ts;
	private String gs;
	private String dxj;
	private float jd;
	private String nbw;
	private float wd;
	private String act;
	private String createtime;
	

	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getS1() {
		return s1;
	}
	public void setS1(int s1) {
		this.s1 = s1;
	}
	public int getS2() {
		return s2;
	}
	public void setS2(int s2) {
		this.s2 = s2;
	}
	public int getS3() {
		return s3;
	}
	public void setS3(int s3) {
		this.s3 = s3;
	}
	public int getS4() {
		return s4;
	}
	public void setS4(int s4) {
		this.s4 = s4;
	}
	public float getAx() {
		return ax;
	}
	public void setAx(float ax) {
		this.ax = ax;
	}
	public float getAy() {
		return ay;
	}
	public void setAy(float ay) {
		this.ay = ay;
	}
	public int getTy() {
		return ty;
	}
	public void setTy(int ty) {
		this.ty = ty;
	}
	public int getTm() {
		return tm;
	}
	public void setTm(int tm) {
		this.tm = tm;
	}
	public int getTd() {
		return td;
	}
	public void setTd(int td) {
		this.td = td;
	}
	public int getTh() {
		return th;
	}
	public void setTh(int th) {
		this.th = th;
	}
	public int getTmm() {
		return tmm;
	}
	public void setTmm(int tmm) {
		this.tmm = tmm;
	}
	public int getTs() {
		return ts;
	}
	public void setTs(int ts) {
		this.ts = ts;
	}
	public String getGs() {
		return gs;
	}
	public void setGs(String gs) {
		this.gs = gs;
	}
	public String getDxj() {
		return dxj;
	}
	public void setDxj(String dxj) {
		this.dxj = dxj;
	}
	public float getJd() {
		return jd;
	}
	public void setJd(float jd) {
		this.jd = jd;
	}
	public String getNbw() {
		return nbw;
	}
	public void setNbw(String nbw) {
		this.nbw = nbw;
	}
	public float getWd() {
		return wd;
	}
	public void setWd(float wd) {
		this.wd = wd;
	}
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getAf() {
		return af;
	}
	public void setAf(int af) {
		this.af = af;
	}
	
}
