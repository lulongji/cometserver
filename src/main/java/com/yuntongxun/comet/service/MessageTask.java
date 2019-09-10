package com.yuntongxun.comet.service;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 11:43 2018/11/17
 */
public class MessageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageTask.class);

    private String message;

    private RestTemplate restTemplate;

    private String mcmUri;

    public MessageTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            if (this.push(restTemplate, message, mcmUri)) {
                logger.info("push msg zx to the 3rd party ：num=1");
            } else {
                Thread.sleep(1000);
                if (this.push(restTemplate, message, mcmUri)) {
                    logger.info("push msg zx to the 3rd party：num=2");
                } else {
                    Thread.sleep(1000);
                    if (this.push(restTemplate, message, mcmUri)) {
                        logger.info("push msg zx to the 3rd party：num=3");
                    } else {
                        logger.info("push 3 times,all failed,over.");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("MessageTask#run#exception:", e);
        }

    }

    /**
     * 向mcm发送消息
     *
     * @param sendData
     * @return
     */
    public static boolean push(RestTemplate restTemplate, String sendData, String mcmuri) {
        try {
            String result = restTemplate.postForObject(mcmuri, sendData, String.class);
            logger.info("调用mcm返回：" + result);
            if (null != result && !"".equals(result)) {
                HashMap<String, Object> resultMap = JSONObject.parseObject(result, HashMap.class);
                if ((Boolean) resultMap.get(CommonConstants.ValType.SUCCESS_INFO)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("MessageTask#push#exception:", e);
            return false;
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setMcmUri(String mcmUri) {
        this.mcmUri = mcmUri;
    }
}