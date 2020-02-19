package com.ltyc.sms.session.cmpp;

import com.ltyc.sms.common.storedMap.VersionObject;
import com.ltyc.sms.connect.manager.EndpointEntity;
import com.ltyc.sms.entity.cmpp30.msg.CmppActiveTestRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.session.AbstractSessionStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppSessionStateManager extends AbstractSessionStateManager<Long, Message> {
    private static final Logger logger = LoggerFactory.getLogger(CmppSessionStateManager.class);

    public CmppSessionStateManager(EndpointEntity entity) {
        super(entity);
    }

    @Override
    protected Long getSequenceId(Message msg) {
        return msg.getHeader().getSequenceId();
    }

}
