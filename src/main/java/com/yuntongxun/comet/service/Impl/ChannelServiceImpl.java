package com.yuntongxun.comet.service.Impl;

import com.yuntongxun.base.utils.json.FastjsonUtils;
import com.yuntongxun.comet.common.MapConstants;
import com.yuntongxun.comet.model.IMClient;
import com.yuntongxun.comet.model.Message;
import com.yuntongxun.comet.service.IChannelService;
import com.yuntongxun.comet.service.MessageTask;
import com.yuntongxun.comet.service.MessageThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ChannelServiceImpl implements IChannelService {


    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);


    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Value("${comet.sendMcmMsgUrl}")
    private String mcmUri;

    @Override
    public List<Message> poll(IMClient receiver, Message message) throws Exception {
        List<Message> messages = null;
        LinkedList<Message> messageLinkedList = MapConstants.messageMap.get(getKey(message));
        if (messageLinkedList != null && messageLinkedList.size() > 0) {
            messages = new ArrayList<>();
            messages.add(messageLinkedList.get(0));
            messageLinkedList.remove(0);
            receiver.setSaveTime(System.currentTimeMillis() + MapConstants.expire);
        }
        return messages;
    }

    @Override
    public void receive(IMClient receiver, Message message) throws Exception {
        LinkedList<Message> messageLinkedList = MapConstants.messageMap.get(getKey(message));
        if (messageLinkedList == null) {
            messageLinkedList = new LinkedList<>();
        }
        messageLinkedList.add(message);
        MapConstants.messageMap.put(getKey(message), messageLinkedList);
    }

    @Override
    public void sendMsg(IMClient receiver, Message message) throws Exception {
        logger.info("ChannelServiceImpl#sendMsg message:" + message.toString());
        MessageTask messageTask = new MessageTask(this.formatMsg(message));
        messageTask.setRestTemplate(restTemplate);
        messageTask.setMcmUri(mcmUri);
        MessageThreadPool.addTask(messageTask);

    }

    private String formatMsg(Message message) throws Exception {
        return FastjsonUtils.beanToJson(message);
    }

    private String getKey(Message message) {
        return message.getAppId() + "#" + message.getAccessId() + "#" + message.getUserId();
    }


}
