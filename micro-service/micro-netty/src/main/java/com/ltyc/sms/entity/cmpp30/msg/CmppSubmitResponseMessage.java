package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.utils.MsgId;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppSubmitResponseMessage extends DefaultMessage {
    private static final long serialVersionUID = -6806940736604019528L;
    private MsgId msgId = new MsgId();
    private long result = 0;

    public CmppSubmitResponseMessage(long sequenceId) {
        super(CmppPacketType.CMPPSUBMITRESPONSE, sequenceId);
    }

    public CmppSubmitResponseMessage(Header header) {
        super(CmppPacketType.CMPPSUBMITRESPONSE, header);
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
        sb.append("CmppSubmitResponseMessage [msgId=").append(msgId).append(", result=").append(result).append(", sequenceId=")
                .append(getHeader().getSequenceId()).append("]");
        return sb.toString();
    }

}
