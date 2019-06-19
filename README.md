# JavaConcurrent

## 项目介绍

![plateform](https://img.shields.io/badge/plateform-windows-lightgrey.svg) 
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-2018.1.2-8B0000.svg) 
![JDK](https://img.shields.io/badge/JDK-1.8.0_16-3A5FCD.svg) 
![MySQL](https://img.shields.io/badge/MySQL-8.0.16-brightgreen.svg) 


本项目是基于socket通讯，实现高并发访问服务器与数据库的应用。

**项目功能介绍：**

- 利用基础的socket实现client与server通讯，采用数据长度+数据内容的形式传输数据。

- 针对高并发访问server，采用线程池实现有效高并发。

- 针对并发访问数据库，以及数据库连接超时，采用数据库连接池。


**特性：**

- 使用无界阻塞队列维护每个socket分发任务，线程池的工作线程不断的从这个任务队列取值执行
- server启动时加载线程池与数据库连接池，并可以动态改变线程池大小
- 数据库连接池为有界阻塞队列，对于超时未获得数据库连接，采用返回错误信息的方式防止永久阻塞



**项目启动过程：**

- 启动 server，也就是 SocketServer
- 启动client，执行 MutiSocketClient
- server 与 client 实现交互


**注意事项：**

- 需要在ConnectionPool 中设置自己对用的⚠️**数据库，用户名，用户密码**
```
private static final String DB_URL = "jdbc:mysql://localhost:3306/shop?useUnicode=true&amp;characterEncoding=UTF8";
private static final String DB_USER = "root";
private static final String DB_PWD = "admin";
``` 
- MutiSocketClient 设置模拟访问server的⚠️**client数量**
```
//client 数量
int socketNum = 1000;
``` 
- SocketServer 设置构造⚠️**线程池的大小**，以及⚠️**数据库连接池的大小**
```
//创建一个线程池(个数20个)
DefaultThreadPool<Job> threadPool = new DefaultThreadPool<Job>(20);
//创建数据库连接池(个数10个)
pool = new ConnectionPool(10);
```

**引用：**
> 《Java并发编程的艺术》  
> 《深入理解Java虚拟机》  
> 《高性能MySQL》
