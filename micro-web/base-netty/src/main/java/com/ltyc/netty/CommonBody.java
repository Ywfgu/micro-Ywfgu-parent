package com.ltyc.netty;

import java.io.Serializable;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2019/11/13
 */
public class CommonBody implements Serializable,Cloneable {
    private long totalLength;
    private long commandId;
    private long sequenceId;
    private byte[] customHeaderBuffer;
    private byte[] headerBuffer;
    private byte[] bodyBuffer;

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getCommandId() {
        return commandId;
    }

    public void setCommandId(long commandId) {
        this.commandId = commandId;
    }

    public long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public byte[] getCustomHeaderBuffer() {
        return customHeaderBuffer;
    }

    public void setCustomHeaderBuffer(byte[] customHeaderBuffer) {
        this.customHeaderBuffer = customHeaderBuffer;
    }

    public byte[] getHeaderBuffer() {
        return headerBuffer;
    }

    public void setHeaderBuffer(byte[] headerBuffer) {
        this.headerBuffer = headerBuffer;
    }

    public byte[] getBodyBuffer() {
        return bodyBuffer;
    }

    public void setBodyBuffer(byte[] bodyBuffer) {
        this.bodyBuffer = bodyBuffer;
    }
}
