package com.yuntongxun.comet.service;

import com.yuntongxun.base.uuid.UUIDGenerator;
import com.yuntongxun.comet.common.Constants;
import com.yuntongxun.comet.common.MapConstants;
import com.yuntongxun.comet.model.IMClient;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class ClientService {


    /**
     * 获取默认过期时间的IMClient
     *
     * @param session httpSession
     */
    public IMClient getIMClient(HttpSession session) {
        return getIMClient(session, MapConstants.expire);
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
            client.setSessionId(session.getId());
            client.setName(Constants.IM_USER);
            client.setSaveTime(System.currentTimeMillis() + expire);
            session.setAttribute(Constants.IM_USER, client);
        } else {
            if (client.getSaveTime() <= System.currentTimeMillis()) {
                session.removeAttribute(Constants.IM_USER);
                client = null;
            }
        }
        client.setExpire(expire);
        return client;
    }

}
