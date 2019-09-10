package com.yuntongxun.comet.common;

import com.yuntongxun.comet.model.Message;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MapConstants {

    /**
     * 过期时间600s
     */
    int expire = 600 * 1000;

    Map<String, LinkedList<Message>> messageMap = new ConcurrentHashMap<>();

}
