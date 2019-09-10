package com.yuntongxun.comet.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class Message {

    private String appId;//与企业对应id
    private String accessId;//渠道id
    private String userId;//用户唯一id
    private String event;//1请求， 5会话
    private String msgType;//事件类型
    private String msgId;//消息id（每一条消息都不一样）
    private String context;//发送的消息内容

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
