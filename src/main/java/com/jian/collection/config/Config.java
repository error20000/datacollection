package com.jian.collection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	
	
	//自动填充主键
	@Value("${auto_fill_primary_key}")
	public String autoFillPrimaryKey; //自动填充主键
		
	//日期自动填充配置
	@Value("${auto_fill_date_for_add}")
	public String autoFillDateForAdd; //新增日期类型自动填充
	@Value("${auto_fill_date_for_modify}")
	public String autoFillDateForModify; //修改日期类型自动填充
	
	//静态资源
	@Value("${out_static_path}")
	public String out_static_path; //外部静态资源  如上传
	@Value("${logs_path}")
	public String logs_path; //日志地址
	
	//登录session
	@Value("${login_session_key}")
	public String login_session_key="login_user";

	//登录sso
	@Value("${sso_url}")
	public String sso_url;
	@Value("${sso_module}")
	public String sso_module;

	//socket
	@Value("${socket_port}")
	public int socket_port;
	

	public String secretKey = "666PCM01";

	
}
