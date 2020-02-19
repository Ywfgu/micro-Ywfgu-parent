package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppActiveTestResponseMessage extends DefaultMessage{
    private static final long serialVersionUID = 4300214238350805590L;
    private short reserved = 0;
    public CmppActiveTestResponseMessage(long sequenceId) {
        super(CmppPacketType.CMPPACTIVETESTRESPONSE,sequenceId);
    }
    public CmppActiveTestResponseMessage(Header header) {
        super(CmppPacketType.CMPPACTIVETESTRESPONSE,header);
    }
    public short getReserved() {
        return reserved;
    }
    public void setReserved(short reserved) {
        this.reserved = reserved;
    }

}
