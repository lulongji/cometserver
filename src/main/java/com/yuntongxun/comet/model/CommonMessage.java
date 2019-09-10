package com.yuntongxun.comet.model;

import lombok.Data;

@Data
public class CommonMessage {

    private String id;
    private String msgType;
    private String context;

    public CommonMessage(String id, String msgType, String context) {
        this.id = id;
        this.msgType = msgType;
        this.context = context;
    }
}
