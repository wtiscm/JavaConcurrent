package com.web.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.log.Log;

public class Job implements Runnable{
	private Socket socket;
	Job(Socket socket1) {
		this.socket = socket1;
	}
    public void run() {
        try {
      	    byte[] bytes;
            //从socket中获取输入流
            InputStream inputStream = socket.getInputStream();
            ArrayList<Integer> num = new ArrayList<Integer>();
            while (true) {
                int firstData = inputStream.read();
                //如果读取的值为-1 说明Socket已经被关闭了
                if(firstData==-1){
                    break;
                  }
                int secondData = inputStream.read();
                int length = (firstData << 8) + secondData;
                // 然后构造一个指定长的byte数组
                bytes = new byte[length];
                inputStream.read(bytes);
                String data = new String(bytes, "UTF-8");
                int id = Integer.parseInt(String.valueOf(data.charAt(5)));
                num.add(id);
                System.out.println("get message from client: "+data+">>>"+Thread.currentThread().getName());
            }
            int rat = num.get(0);
            socket.shutdownInput();
            
            Connection connection = null;
            String message = "";
            try {
                connection = SocketServer.pool.getConnection(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (connection != null){
                try {
					message = getData(connection,rat);
                } finally {
                    SocketServer.pool.releaseConnection(connection);
                }   
            }else {
				message = "数据库读取超时";
				}
            
            OutputStream outputStream = socket.getOutputStream();
            byte[] sendBytes; 
            sendBytes = message.getBytes("UTF-8");
  	      	outputStream.write(sendBytes.length >>8);
  	      	outputStream.write(sendBytes.length);
            outputStream.write(sendBytes);
            socket.shutdownOutput();

            socket.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        
        public static String getData(Connection connection,int seckillId) {
    		PreparedStatement pre = null;
    		ResultSet rs = null;
    		String str = "";
    		String sql = "SELECT seckill_id,name,number FROM seckill WHERE seckill_id = ?";
    		try {
    			// 预编译sql
    			pre = (PreparedStatement) connection.clientPrepareStatement(sql);
    			// 设置条件 字符串查询
    			pre.setInt(1, seckillId+1000);
    			rs = pre.executeQuery();
    			while(rs.next()){
    				// 输出结果集
    				str += rs.getInt(1);
    				str += ",";
    				str += rs.getString(2);
                    str += ",";
                    str += rs.getInt(3);
                    str += ",";
    			}
    		} catch (SQLException e) {
    			System.out.println(e.getMessage());
    		} finally{
    				try {
    					if(rs!= null){
    						rs.close();
    					}
    					if(pre!= null){
    						pre.close();
    					}
    				} catch (SQLException e) {
    					System.out.println("关闭未成功："+e.getMessage());
    				}
    		}
    		return str;
    }
}
