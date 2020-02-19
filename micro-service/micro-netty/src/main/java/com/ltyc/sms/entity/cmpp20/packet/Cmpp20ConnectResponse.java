package com.ltyc.sms.entity.cmpp20.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.PacketStructure;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30DataType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp20ConnectResponse implements PacketStructure {
    STATUS(Cmpp30DataType.UNSIGNEDINT, true, 1),
    AUTHENTICATORISMG(Cmpp30DataType.OCTERSTRING, true, 16),
    VERSION(Cmpp30DataType.UNSIGNEDINT, true, 1);
    private DataType dataType;
    private boolean isFixFiledLength;
    private int length;
    private Cmpp20ConnectResponse(DataType dataType, boolean isFixFiledLength, int length) {
        this.dataType = dataType;
        this.isFixFiledLength = isFixFiledLength;
        this.length = length;
    }
    @Override
    public DataType getDataType() {
        return dataType;
    }
    @Override
    public boolean isFixFiledLength() {
        return isFixFiledLength;
    }
    @Override
    public boolean isFixPacketLength() {
        return true;
    }
    @Override
    public int getLength() {
        return length;
    }
    @Override
    public int getBodyLength() {

        return 18;
    }
}
