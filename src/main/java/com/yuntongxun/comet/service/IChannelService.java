package com.yuntongxun.comet.service;

import com.yuntongxun.comet.model.IMClient;
import com.yuntongxun.comet.model.Message;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

public interface IChannelService {

    void subscribe(String channelName, IMClient client);

    void unsubscribe(String channelName, IMClient client);

    void onMessage(Message message, IMClient client);

    DeferredResult<List<Message>> poll(IMClient receiver);

    void send(Message message, IMClient client);
}
