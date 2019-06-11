package com.web.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class SocketClient implements Runnable {
	int j;
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
	    // Ҫ���ӵķ����IP��ַ�Ͷ˿�
	    String host = "127.0.0.1";
	    int port = 55532;
	    int num = 1;
	    // �����˽�������
	    Socket socket = new Socket(host, port);
	    // �������Ӻ��������
	    OutputStream outputStream = socket.getOutputStream();

	    byte[] sendBytes;
	    byte[] bytes;
	    for (int i=0;i<num;i++){
	    	String message = "���,����"+j+"��socket�ĵ�"+i+"������";
	    	sendBytes = message.getBytes("UTF-8");
	    	outputStream.write(sendBytes.length >>8);
	    	outputStream.write(sendBytes.length);
	    	outputStream.write(sendBytes);
	    	outputStream.flush();
	    }
	    socket.shutdownOutput();

	    InputStream inputStream = socket.getInputStream();
        while (true) {
        int first = inputStream.read();
        //�����ȡ��ֵΪ-1 ˵����������ĩβ��Socket�Ѿ����ر��ˣ���ʱ��������ȥ��ȡ
        if(first==-1){
            break;
          }
        int second = inputStream.read();
        int length = (first << 8) + second;
        // Ȼ����һ��ָ������byte����
        bytes = new byte[length];
        // Ȼ���ȡָ�����ȵ���Ϣ����
        inputStream.read(bytes);
        System.out.println("get message from server: " + new String(bytes, "UTF-8")+">>>"+Thread.currentThread().getName());
        }
        socket.shutdownInput();
    
	    socket.close();
	  }
	}
