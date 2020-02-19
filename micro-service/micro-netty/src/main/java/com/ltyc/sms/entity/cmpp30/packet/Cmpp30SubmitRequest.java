package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.PacketStructure;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp30SubmitRequest implements PacketStructure {
    MSGID(Cmpp30DataType.UNSIGNEDINT, true, 8),
    PKTOTAL(Cmpp30DataType.UNSIGNEDINT, true, 1),
    PKNUMBER(Cmpp30DataType.UNSIGNEDINT, true, 1),
    REGISTEREDDELIVERY(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGLEVEL(Cmpp30DataType.UNSIGNEDINT, true, 1),
    SERVICEID(Cmpp30DataType.OCTERSTRING, true, 10),
    FEEUSERTYPE(Cmpp30DataType.UNSIGNEDINT, true, 1),
    FEETERMINALID(Cmpp30DataType.OCTERSTRING, true, 32),
    FEETERMINALTYPE(Cmpp30DataType.UNSIGNEDINT, true, 1),
    TPPID(Cmpp30DataType.UNSIGNEDINT, true, 1),
    TPUDHI(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGFMT(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGSRC(Cmpp30DataType.OCTERSTRING, true, 6),
    FEETYPE(Cmpp30DataType.OCTERSTRING, true, 2),
    FEECODE(Cmpp30DataType.OCTERSTRING, true, 6),
    VALIDTIME(Cmpp30DataType.OCTERSTRING, true, 17),
    ATTIME(Cmpp30DataType.OCTERSTRING, true, 17),
    SRCID(Cmpp30DataType.OCTERSTRING, true, 21),
    DESTUSRTL(Cmpp30DataType.UNSIGNEDINT, true, 1),
    DESTTERMINALID(Cmpp30DataType.OCTERSTRING, true, 32),
    DESTTERMINALTYPE(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGLENGTH(Cmpp30DataType.UNSIGNEDINT, true, 1),
    MSGCONTENT(Cmpp30DataType.OCTERSTRING, false, 0),
    LINKID(Cmpp30DataType.OCTERSTRING, true, 20);
    private Cmpp30DataType dataType;
    private boolean isFixFiledLength;
    private int length;

    private Cmpp30SubmitRequest(Cmpp30DataType dataType, boolean isFixFiledLength, int length) {
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

        return 151;
    }
}
