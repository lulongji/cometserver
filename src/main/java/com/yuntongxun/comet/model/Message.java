package com.yuntongxun.comet.model;

import lombok.Data;

@Data
public class Message {

    private Channel channel;

    private CommonMessage commonMessage;

    public Message(Channel channel, CommonMessage commonMessage) {
        this.channel = channel;
        this.commonMessage = commonMessage;
    }
}
