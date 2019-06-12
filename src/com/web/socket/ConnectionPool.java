package com.web.socket;


import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool {
    LinkedList<Connection> pool = new LinkedList<Connection>();
	private static final String DB_URL = "jdbc:mysql://localhost:3306/shop?useUnicode=true&amp;characterEncoding=UTF8";
	private static final String DB_USER = "root";
	private static final String DB_PWD = "admin";
    ConnectionPool(int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                pool.add(getConnection());
            }
        }
    }
    
	public static Connection getConnection(){
		// mysql连接
		Connection conn = null;
		try {
			conn = (Connection) DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
		} catch (SQLException e) {
			System.out.println("数据库连接失败原因:"+e.getMessage());
			return null;
		}
		return conn;
	}

    public Connection getConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills < 0) {
                if (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long futuer = System.currentTimeMillis() + mills;
                long remaing = mills;
                while (pool.isEmpty() && remaing > 0){
                    pool.wait(remaing);
                    remaing = futuer - System.currentTimeMillis();
                }
                Connection col = null;
                if (!pool.isEmpty()){
                    col = pool.removeFirst();
                }
                return col;
            }
        }
    }

    public void releaseConnection(Connection coonection) {
        if (coonection != null){
            synchronized(pool){
                pool.addLast(coonection);
                pool.notifyAll();
            }
        }
    }
}

