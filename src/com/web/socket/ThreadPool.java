package com.web.socket;

public interface ThreadPool<Job extends Runnable> {
    //�������
    void execute(Job job);
    //�ر��̳߳ص�һ���߳�
    void shutdown();
    //���ӹ����߳�
    void addWorker(int num);
    //���ٹ����߳�
    void removeWorker(int num);
    //���������еĸ���
    int getJobNum();
}
