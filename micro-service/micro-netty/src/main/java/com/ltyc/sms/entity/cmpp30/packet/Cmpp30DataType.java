package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.entity.DataType;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum Cmpp30DataType implements DataType {

    UNSIGNEDINT(0x1), OCTERSTRING(0x2);
    private int commandId;

    private Cmpp30DataType(int commandId) {
        this.commandId = commandId;
    }

    @Override
    public int getCommandId() {
        return commandId;
    }

    @Override
    public int getAllCommandId() {
        int defaultId = 0x0;
        int allCommandId = 0x0;
        for (Cmpp30DataType dataType : Cmpp30DataType.values()) {
            allCommandId |= dataType.commandId;
        }
        return allCommandId ^ defaultId;
    }
}
