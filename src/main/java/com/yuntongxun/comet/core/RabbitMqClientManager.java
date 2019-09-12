package com.yuntongxun.comet.core;

import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.comet.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RabbitMqClientManager {

    //维护启动的rabbitTemplate实例， key为 exchange-queue-routingKey
    private ConcurrentHashMap<String, RabbitTemplate> templateHolder = new ConcurrentHashMap<>();

    @Autowired
    private CachingConnectionFactory connectionFactory;

    /**
     * 动态新增
     *
     * @param routingKey
     */
    public void add(String routingKey) {
        if (StringUtils.isEmpty(routingKey)) {
            return;
        }
        Object o = RedisTemplateUtil.get(Constants.YTX_QUEUE_NAME + routingKey);//暂存routingKey对应的queueName
        if (o != null) {
            return;
        }
        String queueName = generateQueueName(routingKey);
        buildTemplate(Constants.MQ_EXCHANGE_NAME, queueName, routingKey);
        RedisTemplateUtil.set(Constants.YTX_QUEUE_NAME + routingKey, queueName, Constants.YTX_QUEUE_NAME_EXPIRE, TimeUnit.MILLISECONDS);
    }

    /**
     * @param routingKey
     * @return
     */
    private RabbitTemplate getClient(String routingKey) {
        Object templateKey = RedisTemplateUtil.get(Constants.YTX_TEMPLATEKEY_NAME + routingKey);
        if (templateKey == null) {
            return null;
        }
        return templateHolder.get(templateKey);
    }


    /**
     * @param exchangeName
     * @param queueName
     * @param routingKey
     * @return
     */
    private RabbitTemplate buildTemplate(String exchangeName, String queueName, String routingKey) {
        String key = generateKey(exchangeName, queueName, routingKey);
        if (templateHolder.contains(key)) {
            return templateHolder.get(key);
        }
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        Queue queue = createQueue(queueName, 255);
        admin.declareQueue(queue);
        TopicExchange exchange = new TopicExchange(exchangeName);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey));

        RabbitTemplate template = admin.getRabbitTemplate();
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        templateHolder.put(key, template);
        RedisTemplateUtil.set(Constants.YTX_TEMPLATEKEY_NAME + routingKey, key, Constants.YTX_QUEUE_NAME_EXPIRE, TimeUnit.MILLISECONDS);
        return template;
    }

    /**
     * 生成templateHolder的key
     * key为 exchange-queue-routingKey
     *
     * @return
     */
    private final String generateKey(String exchangeName, String queueName, String routingKey) {
        return new StringBuilder(exchangeName).append("-").append(queueName).append("-").append(routingKey).toString();
    }

    /**
     * @param routingKey
     * @return
     */
    private final String generateQueueName(String routingKey) {
        return new StringBuilder(Constants.MQ_QUEUE_PREFIX).append(routingKey).toString();
    }

    private Queue createQueue(String queueName, int maxPriority) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", maxPriority);
        //args.put("x-message-ttl", 1000 * 60 * 60 * 24);//消息过期时间
        args.put("x-expires", Constants.YTX_QUEUE_NAME_EXPIRE);//队列过期时间
        return new Queue(queueName, true, false, false, args);
    }

    /**
     * 往rabbitMQ发送消息
     */
    public boolean sendMessage(String routingKey, int priority, String msg) {
        this.add(routingKey);
        if (StringUtils.isAnyEmpty(msg, routingKey)) {
            log.error("Invalid param, msg={}, routingKey={}", msg, routingKey);
            return false;
        }
        RabbitTemplate rabbitTemplate = getClient(routingKey);
        if (rabbitTemplate == null) {
            log.error("rabbitTemplate with routingKey {} is not exist.", routingKey);
            return false;
        }

        MessageProperties properties = new MessageProperties();
        properties.setPriority(priority);
        Message message = new Message(msg.getBytes(), properties);
        rabbitTemplate.send(Constants.MQ_EXCHANGE_NAME, routingKey, message);
        return true;
    }

    /**
     * 从rabbitMQ拉取消息
     */
    public String pullMessage(String routingKey) {
        this.add(routingKey);
        RabbitTemplate rabbitTemplate = getClient(routingKey);
        if (rabbitTemplate == null) {
            log.error("rabbitTemplate with routingKey {} is not exist.", routingKey);
            return null;
        }
        Object o = RedisTemplateUtil.get(Constants.YTX_QUEUE_NAME + routingKey);//暂存routingKey对应的queueName
        byte[] message = (byte[]) rabbitTemplate.receiveAndConvert(o.toString());
        if (message == null) {
            return null;
        }
        log.debug("message pulled is {}", message);
        return new String(message);
    }


}
