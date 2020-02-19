package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import org.apache.commons.codec.binary.Hex;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppConnectResponseMessage extends DefaultMessage{
    private static final long serialVersionUID = -5010314567064353091L;
    private long status = 3;
    private byte[] authenticatorISMG = GlobalConstance.emptyBytes;
    private short version = 0x30;

    public CmppConnectResponseMessage(Header header ) {
        super(CmppPacketType.CMPPCONNECTRESPONSE,header);
    }
    public CmppConnectResponseMessage(long sequenceId) {
        super(CmppPacketType.CMPPCONNECTRESPONSE,sequenceId);
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public byte[] getAuthenticatorISMG() {
        return authenticatorISMG;
    }

    public void setAuthenticatorISMG(byte[] authenticatorISMG) {
        this.authenticatorISMG = authenticatorISMG;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return String.format("CmppConnectResponseMessage [version=%s, status=%s,authenticatorISMG = %s, sequenceId=%s]", version, status, Hex.encodeHexString(authenticatorISMG), getHeader().getSequenceId());
    }
}
