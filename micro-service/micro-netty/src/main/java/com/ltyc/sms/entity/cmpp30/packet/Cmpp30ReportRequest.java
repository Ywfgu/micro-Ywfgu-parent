package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.PacketStructure;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp30ReportRequest implements PacketStructure {
    MSGID(Cmpp30DataType.UNSIGNEDINT, true, 8),
    STAT(Cmpp30DataType.OCTERSTRING, true, 7),
    SUBMITTIME(Cmpp30DataType.OCTERSTRING, true, 10),
    DONETIME(Cmpp30DataType.OCTERSTRING, true, 10),
    DESTTERMINALID(Cmpp30DataType.OCTERSTRING, true, 32),
    SMSCSEQUENCE(Cmpp30DataType.UNSIGNEDINT, true, 4);

    private DataType dataType;
    private boolean isFixFiledLength;
    private int length;

    private Cmpp30ReportRequest(DataType dataType, boolean isFixFiledLength, int length) {
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

        return 71;
    }
}
