package com.yuntongxun.comet.controller;

import com.yuntongxun.base.bean.Result;
import com.yuntongxun.comet.common.exception.ErrorEnum;
import com.yuntongxun.comet.model.IMClient;
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
 *
 * @author lu
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
     * 建立链接(心跳)
     *
     * @param req
     * @return
     */
    @PostMapping("/polling")
    public Result poll(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.success();
        try {
            List<Message> messages = channelService.poll(clientService.getIMClient(req.getSession()), message);
            result.setResult(messages);
        } catch (Exception e) {
            logger.error("IMController#poll#Exception:{}", e);
            result = Result.failure();
        }
        return result;
    }

    /**
     * 断开链接（停止心跳）
     *
     * @param req
     * @param message
     * @return
     */
    @PostMapping("/unpoll")
    public Result unpoll(HttpServletRequest req, @RequestBody Message message) {
        Result result = Result.success();
        try {
            IMClient receiver = clientService.getIMClient(req.getSession());
            logger.info("----------------[unpoll session {}，message  {}]---------------", receiver.getSessionId(), message.toString());
            result.setCode(ErrorEnum.UNPOLL.getCode());
            result.setResult(ErrorEnum.UNPOLL.getInfo());
        } catch (Exception e) {
            logger.error("IMController#unpoll#Exception:{}", e);
            result = Result.failure();
        }
        return result;
    }

    /**
     * 接收消息
     *
     * @param message
     * @return
     */
    @PostMapping("/receive")
    public Result receive(@RequestBody Message message) {
        Result result = Result.failure();
        try {
            channelService.receive(message);
            result = Result.success("接收成功!");
        } catch (Exception e) {
            result.setInfo("接收失败！");
            logger.error("IMController#receive#Exception:{}", e);
        }
        return result;
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
            channelService.sendMsg(message);
            result = Result.success("发送成功!");
        } catch (Exception e) {
            result.setInfo("发送失败！");
            logger.error("IMController#send#Exception:{}", e);
        }
        return result;
    }

}