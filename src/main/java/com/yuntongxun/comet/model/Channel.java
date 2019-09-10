package com.yuntongxun.comet.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Channel {
    private String name; //channeName
    private Set<Object> subscriptionSet; //channel 数据

    public Channel(String channelName, HashSet<Object> objects) {
        this.name = channelName;
        this.subscriptionSet = objects;
    }
}
