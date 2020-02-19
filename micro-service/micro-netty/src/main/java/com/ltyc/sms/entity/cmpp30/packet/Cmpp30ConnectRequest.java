package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.PacketStructure;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp30ConnectRequest implements PacketStructure {
    SOURCEADDR(Cmpp30DataType.OCTERSTRING, true, 6),
    AUTHENTICATORSOURCE(Cmpp30DataType.OCTERSTRING, true, 16),
    VERSION(Cmpp30DataType.UNSIGNEDINT, true, 1),
    TIMESTAMP(Cmpp30DataType.UNSIGNEDINT, true, 4);
    private DataType dataType;
    private boolean isFixFiledLength;
    private int length;
    private final static int bodyLength = SOURCEADDR.length + AUTHENTICATORSOURCE.length + VERSION.length + TIMESTAMP.length;

    private Cmpp30ConnectRequest(DataType dataType, boolean isFixFiledLength, int length) {
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
        return bodyLength;
    }
}
