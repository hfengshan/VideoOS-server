/*
 * Copyright 2017 Zhongan.com All right reserved. This software is the
 * confidential and proprietary information of Zhongan.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Zhongan.com.
 */

package com.videojj.videoservice.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发基础类包
 * 
 * @author
 */
public class ConcurrencyUtil {

    /**
     * 获取线程工厂
     * 
     * @param threadKey
     * @return ThreadFactory
     */
    public static ThreadFactory newThreadFactory(final String threadKey) {

        return new ThreadFactory() {

            private AtomicInteger threadIndex = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadKey.concat(String.valueOf(threadIndex.incrementAndGet())));
            }
        };
    }

    /**
     * 获取新的线程池执行
     * 
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadKey
     * @param handler 当线程池与等待队列到底边界时处理策略
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                           TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                                           String threadKey, RejectedExecutionHandler handler) {

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                newThreadFactory(threadKey), handler);
    }

    /**
     * 获取新的线程池执行
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadKey
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                           TimeUnit unit, BlockingQueue<Runnable> workQueue,
                                                           String threadKey) {

        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                newThreadFactory(threadKey));
    }
}
