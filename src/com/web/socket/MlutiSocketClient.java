package com.web.socket;

public class MlutiSocketClient {
	public static void main(String[] args) throws Exception {
		int socketNum = 1000;
		for (int i=0;i<socketNum;i++){
			Thread socketclient = new Thread(new SocketClient(i)) ;
			socketclient.start();
		}
	}
}
