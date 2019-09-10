package com.yuntongxun.comet.common;

import com.yuntongxun.comet.model.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface MapConstants {


    Map<String, Channel> connSessionMap = new ConcurrentHashMap<>();

}
