package com.web.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SocketClient implements Runnable {
	private final int j;
	SocketClient(int m) {
		this.j = m;
	}

	@Override
	public void run() {
		try {
			connection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connection() throws IOException{
	    // 要连接的服务端IP地址和端口
	    String host = "127.0.0.1";
	    int port = 55532;
	    // 每个socket发送的数据片段个数
	    int num = 1;
	    // 与服务端建立连接
	    Socket socket = new Socket(host, port);
	    // 建立连接后获得输出流
	    OutputStream outputStream = socket.getOutputStream();

	    byte[] sendBytes;
	    byte[] bytes;
	    for (int i=0;i<num;i++){
	    	String message = "你好,我是"+j+"个socket的第"+i+"段数据";
	    	sendBytes = message.getBytes("UTF-8");
	    	// 发送数据长度，第一个字段保存超过8位，第二个字段保存8位以内的
	    	outputStream.write(sendBytes.length >> 8);
	    	outputStream.write(sendBytes.length);
	    	outputStream.write(sendBytes);
	    	outputStream.flush();
	    }
	    socket.shutdownOutput();

	    InputStream inputStream = socket.getInputStream();
        while (true) {
        	int firstData = inputStream.read();
        	//如果读取的值为-1 说明到了流的末尾，Socket已经被关闭了
        	if(firstData==-1){
        		break;
        	}
        	int secondData = inputStream.read();
        	int length = (firstData << 8) + secondData;
        	// 然后构造一个指定长的byte数组
        	bytes = new byte[length];
        	inputStream.read(bytes);
        	System.out.println("get message from server: " + new String(bytes, "UTF-8")+">>>"+Thread.currentThread().getName());
        }
        socket.shutdownInput();
    
	    socket.close();
	  }
	}
