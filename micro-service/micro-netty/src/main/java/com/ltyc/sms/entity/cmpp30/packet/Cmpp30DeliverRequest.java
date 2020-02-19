package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.PacketStructure;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp30DeliverRequest implements PacketStructure {
    MSGID(Cmpp30DataType.UNSIGNEDINT, true, 8),
    DESTID(Cmpp30DataType.OCTERSTRING, true, 21),
    SERVICEID(Cmpp30DataType.OCTERSTRING, true, 10),
    TPPID(Cmpp30DataType.UNSIGNEDINT, true, 1),
    TPUDHI(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGFMT(Cmpp30DataType.UNSIGNEDINT, true, 1),
    SRCTERMINALID(Cmpp30DataType.OCTERSTRING, true, 32),
    SRCTERMINALTYPE(Cmpp30DataType.UNSIGNEDINT, true, 1),
    REGISTEREDDELIVERY(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGLENGTH(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGCONTENT(Cmpp30DataType.OCTERSTRING, false, 0),
    LINKID(Cmpp30DataType.OCTERSTRING, true, 20);

    private final static int bodyLength = 97;

    private DataType dataType;
    private boolean isFixFiledLength;
    private int length;

    private Cmpp30DeliverRequest(DataType dataType, boolean isFixFiledLength, int length) {
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
        return false;
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