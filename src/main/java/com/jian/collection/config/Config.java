package com.jian.collection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	
	
	//自动填充主键
	public String autoFillPrimaryKey; //自动填充主键
		
	//日期自动填充配置
	public String autoFillDateForAdd; //新增日期类型自动填充
	public String autoFillDateForModify; //修改日期类型自动填充
	
	//静态资源
	public String out_static_path; //外部静态资源  如上传
	public String logs_path; //日志地址
	
	//登录session
	public String login_session_key="login_user";

	//登录sso
	public String sso_url;
	public String sso_module;

	

	@Value("${auto_fill_primary_key}")
	public void setAutoFillPrimaryKey(String autoFillPrimaryKey) {
		this.autoFillPrimaryKey = autoFillPrimaryKey;
	}
	
	@Value("${auto_fill_date_for_add}")
	public void setAutoFillDateForAdd(String autoFillDateForAdd) {
		this.autoFillDateForAdd = autoFillDateForAdd;
	}
	
	@Value("${auto_fill_date_for_modify}")
	public void setAutoFillDateForModify(String autoFillDateForModify) {
		this.autoFillDateForModify = autoFillDateForModify;
	}

	@Value("${out_static_path}")
	public void setOut_static_path(String out_static_path) {
		this.out_static_path = out_static_path;
	}
	
	@Value("${logs_path}")
	public void setLogs_path(String logs_path) {
		this.logs_path = logs_path;
	}

	@Value("${login_session_key}")
	public void setLogin_session_key(String login_session_key) {
		this.login_session_key = login_session_key;
	}
	
	@Value("${sso_url}")
	public void setSso_url(String sso_url) {
		this.sso_url = sso_url;
	}
	
	@Value("${sso_module}")
	public void setSso_module(String sso_module) {
		this.sso_module = sso_module;
	}

	
}
