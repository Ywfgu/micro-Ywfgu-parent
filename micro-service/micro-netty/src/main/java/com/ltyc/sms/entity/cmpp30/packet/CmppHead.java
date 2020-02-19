package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;
import com.ltyc.sms.entity.Head;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum CmppHead implements Head {
    COMMANDID(Cmpp30DataType.UNSIGNEDINT, 4),
    TOTALLENGTH(Cmpp30DataType.UNSIGNEDINT, 4),
    SEQUENCEID(Cmpp30DataType.OCTERSTRING, 4);
    private DataType dataType;
    private int length;


    private CmppHead(DataType dataType, int length) {
        this.dataType = dataType;
        this.length = length;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getHeadLength() {

        return 12;
    }

}
