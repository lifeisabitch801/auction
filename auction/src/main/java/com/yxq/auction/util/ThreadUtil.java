package com.yxq.auction.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtil {
    //单例模式创建线程池
//execute传入runnable实现类
    private static ThreadPoolExecutor threadpool=null;

    private  ThreadUtil() {

    }
    public static ThreadPoolExecutor getThreadPool() {
        if(threadpool==null) {
            synchronized (ThreadUtil.class) {
                if(threadpool==null) {
                    threadpool = new ThreadPoolExecutor(2, 3, 1,
                            TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(2));
                }
            }

        }
        return threadpool;
    }

}

