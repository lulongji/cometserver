package com.yuntongxun.comet.service.Impl;

import com.yuntongxun.comet.core.IMMessageQueue;
import com.yuntongxun.comet.model.Channel;
import com.yuntongxun.comet.model.IMClient;
import com.yuntongxun.comet.model.Message;
import com.yuntongxun.comet.service.ClientService;
import com.yuntongxun.comet.service.IChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChannelServiceImpl implements IChannelService {


    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);


    @Autowired
    private ClientService clientService;

    /**
     * 客户端ID与消息队列映射
     */
    private final Map<String, IMMessageQueue> resultMap = new ConcurrentHashMap<>();
    /**
     * 客户端ID与频道的映射
     */
    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();


    @Override
    public void subscribe(String channelName, IMClient client) {
        Channel channel = channelMap.get(channelName);
        if (channel == null) {
            channel = new Channel(channelName, new HashSet<>());
            channelMap.put(channelName, channel);
        }
        channel.getSubscriptionSet().add(client);
    }

    @Override
    public void unsubscribe(String channelName, IMClient client) {
        Channel channel = channelMap.get(channelName);
        if (channel == null) {
            return;
        }
        Set subscriptionSet = channel.getSubscriptionSet();
        subscriptionSet.remove(client);
        if (subscriptionSet.size() == 0) {
            channelMap.remove(channelName);
        }
    }

    @Override
    public void onMessage(Message message, IMClient receiver) {
        receiver.setSaveTime(new Date().getTime() + clientService.getExpire());
        Channel channel = channelMap.get(message.getChannel().getName());
        if (channel != null) {
            channel.getSubscriptionSet().forEach(user -> send(message, receiver));
            if (channel.getSubscriptionSet().size() == 0) {
                channelMap.remove(message.getChannel().getName());
            }
        }

    }

    @Override
    public DeferredResult<List<Message>> poll(IMClient receiver) {
        if (receiver == null) {
            logger.info("此次链接已过期！");
            return null;
        }
        IMMessageQueue queue = resultMap.get(receiver.getId());
        if (queue == null) {
            queue = new IMMessageQueue();
            resultMap.put(receiver.getId(), queue);
        }
        return queue.poll();
    }


    @Override
    public void send(Message message, IMClient client) {
        IMMessageQueue queue = resultMap.get(client.getId());
        if (queue != null) {
            queue.send(message);
        }
    }

}
