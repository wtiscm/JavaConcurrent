package com.web.socket;

public interface ThreadPool<Job extends Runnable> {
    //执行�?个任�?
    void execute(Job job);
    //终止线程�?
    void shutdown();
    //增加工作�?
    void addWorker(int num);
    //删除工作�?
    void removeWorker(int num);
    //或�?�任务队列个�?
    int getJobNum();
}
