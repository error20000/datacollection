package com.jian.collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jian.collection.server.DelongServerSocket;
import com.jian.collection.server.MainServer;

@SpringBootApplication(scanBasePackages="com.jian")
@EnableAutoConfiguration
public class App {
	
	public static String rootPath = "";
	public static ApplicationContext applicationContext = null;
	public static String[] scanBasePackages = {};
	

	public static void main(String[] args) throws Exception {
		//项目目录
		rootPath = App.class.getResource("/").getPath().replace("/target/classes/", "/");
    	System.out.println(rootPath);
		//扫描范围
    	if(App.class.isAnnotationPresent(SpringBootApplication.class)){
    		SpringBootApplication sba = App.class.getAnnotation(SpringBootApplication.class);
    		scanBasePackages = sba.scanBasePackages();
    	}
    	System.out.println(scanBasePackages);
		//启动
        applicationContext = SpringApplication.run(App.class, args);
        //socket
        applicationContext.getBean(DelongServerSocket.class).start();
    }

	
}
