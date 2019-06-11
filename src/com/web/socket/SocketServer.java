package com.web.socket;

import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	static ConnectionPool pool;
	public static void main(String[] args) throws Exception {
		// ����ָ���Ķ˿�
		int port = 55532;
		ServerSocket server = new ServerSocket(port);
		//����һ���̳߳�(����10��)
		DefaultThreadPool<Job> threadPool = new DefaultThreadPool<Job>(20);
		//�������ݿ����ӳ�
		pool = new ConnectionPool(10);
		// server��һֱ�ȴ����ӵĵ���
		System.out.println("server��һֱ�ȴ����ӵĵ���");
    
		while (true) {
			Socket socket = server.accept();
			Job job = new Job(socket);
			threadPool.execute(job);
      }
  }
}