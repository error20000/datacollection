package com.jian.collection.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jian.annotation.PrimaryKey;
import com.jian.annotation.PrimaryKeyType;
import com.jian.annotation.Table;

@Table("s_user")
public class User {

	private long pid;
	@PrimaryKey(type=PrimaryKeyType.NORMAL)
	private String username;
	private String password;
	

	public Map<String, Object> resultSetToMap(ResultSet resultSet){
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("pid", resultSet.getLong("pid"));
			map.put("username", resultSet.getString("username"));
			map.put("password", resultSet.getString("password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	
}
