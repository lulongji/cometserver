package com.yuntongxun.comet.service;

import com.yuntongxun.comet.model.IMClient;
import com.yuntongxun.comet.model.Message;

import java.util.List;

public interface IChannelService {

    void receive(Message message) throws Exception;

    void sendMsg(Message message) throws Exception;

    List<Message> poll(IMClient receiver, Message message) throws Exception;
}
