package com.web.socket;

public interface ThreadPool<Job extends Runnable> {
    //添加任务
    void execute(Job job);
    //关闭线程池的一个线程
    void shutdown();
    //增加工作线程
    void addWorker(int num);
    //减少工作线程
    void removeWorker(int num);
    //获得任务队列的个数
    int getJobNum();
}
