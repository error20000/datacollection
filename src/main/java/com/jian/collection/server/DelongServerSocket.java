package com.jian.collection.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jian.collection.config.Config;
import com.jian.collection.service.DataService;

@Component
public class DelongServerSocket {
	
	@Autowired
	private Config config;
	@Autowired
	private DataService service;
	
	private boolean started;
    private ServerSocket ss;
    public static ConcurrentHashMap<String, HandleSocket> handleMap = new ConcurrentHashMap<>();
    private ExecutorService pool = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));

    private Logger logger = LoggerFactory.getLogger(DelongServerSocket.class);

    public static void main(String[] args) {
        new DelongServerSocket().start(30000);
    }

    public void start() {
        start(null);
    }

    public void start(Integer port) {
        try {
        	port = port == null ? config.socket_port : port;
            ss = new ServerSocket(port);
            started = true;
            System.out.println("服务端口已开启,占用"+port+"端口号....");
        } catch (Exception e) {
            System.out.println("端口使用中....");
            System.out.println("请关掉相关程序并重新运行服务器！");
            logger.error(e.getMessage());
            System.exit(0);
        }

        try {
            while (started) {
                Socket socket = ss.accept();
                socket.setKeepAlive(true);
                System.out.println(socket.getLocalSocketAddress().toString());
                HandleSocket register = HandleSocket.register(socket, service);
                if (register != null) {
                    pool.submit(register);
                }
            }
        } catch (IOException e) {
        	logger.error(e.getMessage());
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
            	logger.error(e.getMessage());
            }
        }
    }
}

