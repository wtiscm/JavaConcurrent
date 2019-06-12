package com.web.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	static ConnectionPool pool;
	public static void main(String[] args) throws Exception {
		// 监听指定的端口
		int port = 55532;
		ServerSocket server = new ServerSocket(port);
		//创建一个线程池(个数10个)
		DefaultThreadPool<Job> threadPool = new DefaultThreadPool<Job>(20);
		//创建数据库连接池
		pool = new ConnectionPool(10);
		// server将一直等待连接的到来
		System.out.println("server将一直等待连接的到来");
    
		while (true) {
			Socket socket = server.accept();
			Job job = new Job(socket);
			threadPool.execute(job);
      }
  }
}