package com.yuntongxun.comet.service;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * @Author: lu
 * @Date: Created in 11:41 2018/11/17
 */
public class MessageThreadPool {

    private static ExecutorService singleThreadPool;

    public MessageThreadPool() {

        singleThreadPool = newSingleThreadExecutor();
    }

    public static void addTask(MessageTask messageTask) {
        singleThreadPool.execute(messageTask);
    }
}
