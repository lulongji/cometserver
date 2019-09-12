package com.yuntongxun.comet.common;

public interface Constants {

    String IM_USER = "ytx_im_user";

    String MQ_EXCHANGE_NAME = "ytx.comet.exchange";

    String MQ_QUEUE_PREFIX = "yxt.comet.queue.";


    //redis 存储queuename
    String YTX_QUEUE_NAME = "ytx100|:";

    //redis templateKey
    String YTX_TEMPLATEKEY_NAME = "ytx101|:";

    //queuename过期时间
    long YTX_QUEUE_NAME_EXPIRE = 1000 * 60;


}
