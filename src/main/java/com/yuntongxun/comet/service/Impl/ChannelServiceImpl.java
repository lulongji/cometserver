package com.yuntongxun.comet.service.Impl;

import com.yuntongxun.base.utils.json.FastjsonUtils;
import com.yuntongxun.comet.common.MapConstants;
import com.yuntongxun.comet.core.RabbitMqClientManager;
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
import java.util.List;

@Service
public class ChannelServiceImpl implements IChannelService {


    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);


    @Autowired(required = false)
    private RestTemplate restTemplate;

    @Autowired
    private RabbitMqClientManager rabbitMqClientManager;


    @Value("${comet.sendMcmMsgUrl}")
    private String mcmUri;

    @Override
    public List<String> poll(IMClient receiver, Message message) throws Exception {
        List<String> messages = null;
        String routingKey = getKey(message);
        String msg = rabbitMqClientManager.pullMessage(routingKey);
        if (msg != null) {
            messages = new ArrayList<>();
            messages.add(msg);
            receiver.setSaveTime(System.currentTimeMillis() + MapConstants.expire);
        }
        return messages;
    }

    @Override
    public void receive(IMClient receiver, Message message) throws Exception {
        String routingKey = getKey(message);
        String msg = FastjsonUtils.beanToJson(message);
        rabbitMqClientManager.sendMessage(routingKey, 0, msg);
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
        return message.getAppId() + "#" + message.getUserId();
    }


}
