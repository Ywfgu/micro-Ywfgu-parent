package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.utils.MsgId;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppDeliverResponseMessage extends DefaultMessage {
    private static final long serialVersionUID = -8362723084094916290L;
    private MsgId msgId = new MsgId();
    private long result = 0;

    public CmppDeliverResponseMessage(long sequenceId) {
        super(CmppPacketType.CMPPDELIVERRESPONSE, sequenceId);
    }

    public CmppDeliverResponseMessage(Header header) {
        super(CmppPacketType.CMPPDELIVERRESPONSE, header);
    }

    public MsgId getMsgId() {
        return msgId;
    }

    public void setMsgId(MsgId msgId) {
        this.msgId = msgId;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CmppDeliverResponseMessage [msgId=").append(msgId).append(", result=").append(result).append(", sequenceId=")
                .append(getHeader().getSequenceId()).append("]");
        return sb.toString();
    }

}
