package com.ltyc.sms.entity.cmpp30.msg;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public interface Header extends Serializable {
    public void setHeadLength(long length);
    public long getHeadLength();
    public void setPacketLength(long length);
    public long getPacketLength();
    public void setBodyLength(long length);
    public long getBodyLength();
    public void setCommandId(long commandId);
    public long getCommandId();
    public void setSequenceId(long transitionId);
    public long getSequenceId();
    public long getNodeId();
    public void setNodeId(long nodeId);
}
