package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppActiveTestRequestMessage extends DefaultMessage {
    private static final long serialVersionUID = 4496674961657465872L;

    public CmppActiveTestRequestMessage() {
        super(CmppPacketType.CMPPACTIVETESTREQUEST);
    }
    public CmppActiveTestRequestMessage(Header header) {
        super(CmppPacketType.CMPPACTIVETESTREQUEST,header);
    }
}
