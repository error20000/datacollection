package com.jian.collection.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;

@Table("s_data")
public class Data extends Base<Data>  {

	@PrimaryKey(type=PrimaryKeyType.NORMAL)
	private long pid;
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
	

	public Map<String, Object> resultSetToMap(ResultSet resultSet){
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("pid", resultSet.getLong("pid"));
			map.put("sn", resultSet.getString("sn"));
			map.put("s1", resultSet.getInt("s1"));
			map.put("s2", resultSet.getInt("s2"));
			map.put("s3", resultSet.getInt("s3"));
			map.put("s4", resultSet.getInt("s4"));
			map.put("ax", resultSet.getFloat("ax"));
			map.put("ay", resultSet.getFloat("ay"));
			map.put("ty", resultSet.getInt("ty"));
			map.put("tm", resultSet.getInt("tm"));
			map.put("td", resultSet.getInt("td"));
			map.put("th", resultSet.getInt("th"));
			map.put("tmm", resultSet.getInt("tmm"));
			map.put("ts", resultSet.getInt("ts"));
			map.put("gs", resultSet.getString("gs"));
			map.put("dxj", resultSet.getString("dxj"));
			map.put("jd", resultSet.getFloat("jd"));
			map.put("nbw", resultSet.getString("nbw"));
			map.put("wd", resultSet.getFloat("wd"));
			map.put("act", resultSet.getString("act"));
			map.put("createtime", resultSet.getString("createtime"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
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
	
}
