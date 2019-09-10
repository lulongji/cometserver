package com.yuntongxun.comet.controller;

import com.yuntongxun.base.bean.Result;
import com.yuntongxun.comet.model.Message;
import com.yuntongxun.comet.service.ClientService;
import com.yuntongxun.comet.service.IChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 长轮询即时通讯
 */
@RestController
@RequestMapping("/im")
public class IMController {
    private Logger logger = LoggerFactory.getLogger(IMController.class.getName());

    @Autowired
    private IChannelService channelService;

    @Autowired
    private ClientService clientService;

    /**
     * 长轮询
     *
     * @param req
     * @return
     */
    @PostMapping("/polling")
    public DeferredResult<List<Message>> poll(HttpServletRequest req) {
        logger.info("----------------[ping session  {}]---------------", req.getSession().getId());
        return channelService.poll(clientService.getIMClient(req.getSession()));
    }


    /**
     * 发送消息
     *
     * @param req
     * @param message
     * @return
     */
    @PostMapping("/send")
    public Result send(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.failure();
        try {
            //TODO 用户发送消息处理
            channelService.onMessage(message, clientService.getIMClient(req.getSession()));
            result = Result.success("发送成功!");
        } catch (Exception e) {
            result.setInfo("发送失败！");
            logger.error("exception:{}", e);
        }
        return result;
    }

    /**
     * 接收消息
     *
     * @param req
     * @param message
     * @return
     */
    @PostMapping("/receive")
    public Result receive(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.failure();
        try {
            //TODO MCM 过来的消息下发处理
            result = Result.success("接收成功!");
        } catch (Exception e) {
            result.setInfo("接收失败！");
            logger.error("exception:{}", e);
        }
        return result;
    }

    /**
     * 消息订阅
     *
     * @param req
     * @param message
     * @return
     */
    @PostMapping("/subscribe")
    public Result subscribe(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.failure();
        try {
            channelService.subscribe(message.getChannel().getName(), clientService.getIMClient(req.getSession()));
            result = Result.success("订阅channel:" + message.getChannel().getName() + "成功！");
        } catch (Exception e) {
            Result.failure("订阅失败！");
            logger.error("exception:{}", e);
        }
        return result;
    }

    /**
     * 取消订阅
     *
     * @param req
     * @param message
     * @return
     */
    @PostMapping("/unsubscribe")
    public Result close(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.failure();
        try {
            channelService.unsubscribe(message.getChannel().getName(), clientService.getIMClient(req.getSession()));
            result.setInfo("取消订阅:" + message.getChannel());
            result = Result.success();
        } catch (Exception e) {
            result.setInfo("订阅失败！");
            logger.error("exception:{}", e);
        }
        return result;
    }

}