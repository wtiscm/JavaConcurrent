package com.web.socket;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    private static final int DEFAULTNUM = 5;
    private static final int MAXNUM = 10;
    private static final int MINNUM = 1;
    private LinkedList<Job> jobList = new LinkedList<Job>();
    private List<Work> workList = Collections.synchronizedList(new LinkedList<Work>());
    private AtomicInteger workNum = new AtomicInteger();
    DefaultThreadPool(){
        initialSize(DEFAULTNUM);
    }
    DefaultThreadPool(int num){
        int jobNum = num > MAXNUM ? MAXNUM : num < MINNUM ? MINNUM : num;
        initialSize(jobNum);
    }
    private void initialSize(int num){
        for (int i=0;i<num;i++){
            Work work = new Work();
            workList.add(work);
            Thread thread = new Thread(work,"thread-"+workNum.incrementAndGet());
            thread.start();
        }
    }
    class Work implements Runnable {
        private volatile boolean status = true;
        @Override
        public void run() {
            Job job = null;
            while (status) {
                synchronized (jobList) {
                    while (jobList.isEmpty()) {
                        try {
                            jobList.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                   	job = jobList.removeFirst();
                }
                if (job!=null){
                    job.run();
                }
            }
            System.out.println(">shut down"+Thread.currentThread().getName());
        }
        public void shutdown(){
            status = false;
        }
    }
    @Override
    public void execute(Job job) {
        if (job!=null){
            synchronized (jobList){
                jobList.add(job);
                jobList.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Work worker : workList){
            worker.shutdown();
        }
    }

    @Override
    public void addWorker(int num) {
        if (workList.size()+num<=MAXNUM){
            initialSize(num);
        }else{
            System.out.println("add num larger MAXNUM");
        }
    }

    @Override
    public void removeWorker(int num) {
        if (workList.size()-num>=0){
            for (int y=0;y<num;y++){
                Work work = workList.get(y);
                if (workList.remove(work)) {
                    workList.get(y).shutdown();
                }
            }
        }else{
            System.out.println("remove num less MINNUM");
        }
    }

    @Override
    public int getJobNum() {
        return jobList.size();
    }
}
