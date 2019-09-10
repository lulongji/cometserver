package com.yuntongxun.comet.service;

import com.yuntongxun.base.uuid.UUIDGenerator;
import com.yuntongxun.comet.common.Constants;
import com.yuntongxun.comet.model.IMClient;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class ClientService {

    /**
     * 过期时间600s
     */
    private final int expire = 600 * 1000;

    /**
     * 过期时间间隔
     *
     * @return 过期时间间隔(ms)
     */
    public int getExpire() {
        return expire;
    }

    /**
     * 获取默认过期时间的IMClient
     *
     * @param session httpSession
     */
    public IMClient getIMClient(HttpSession session) {
        return getIMClient(session, this.expire);
    }

    /**
     * 获取指定过期时间的IMClient
     *
     * @param session httpSession
     * @param expire  过期时间，<=0代表不会过期
     */
    private IMClient getIMClient(HttpSession session, int expire) {
        IMClient client = (IMClient) session.getAttribute(Constants.IM_USER);
        if (client == null) {
            client = new IMClient();
            client.setId(new UUIDGenerator().generate());
            client.setName(Constants.IM_USER);
            client.setSaveTime(new Date().getTime() + expire);
            session.setAttribute(Constants.IM_USER, client);
        } else {
            if (client.getSaveTime() <= new Date().getTime()) {
                session.removeAttribute(Constants.IM_USER);
                client = null;
            }
        }
        client.setExpire(expire);
        return client;
    }

    public boolean isExpired(IMClient client) {
        return client.getExpire() > 0 && client.getSaveTime() <= new Date().getTime();
    }
}
