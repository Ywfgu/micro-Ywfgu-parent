package com.ltyc.sms.entity.cmpp30;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.entity.cmpp30.msg.DefaultMessage;
import com.ltyc.sms.entity.cmpp30.msg.Header;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppConnectRequestMessage extends DefaultMessage {
    private static final long serialVersionUID = -4852540410843278872L;
    private String sourceAddr = GlobalConstance.emptyString;
    private byte[] authenticatorSource = GlobalConstance.emptyBytes;
    private short version = 0x30;
    private long timestamp = 0L;

    public CmppConnectRequestMessage(Header header) {
        super(CmppPacketType.CMPPCONNECTREQUEST, header);
    }

    public CmppConnectRequestMessage() {
        super(CmppPacketType.CMPPCONNECTREQUEST);
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public byte[] getAuthenticatorSource() {
        return authenticatorSource;
    }

    public void setAuthenticatorSource(byte[] authenticatorSource) {
        this.authenticatorSource = authenticatorSource;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("CmppConnectRequestMessage [version=%s, sourceAddr=%s, sequenceId=%s]", version, sourceAddr, getHeader().getSequenceId());
    }
}
