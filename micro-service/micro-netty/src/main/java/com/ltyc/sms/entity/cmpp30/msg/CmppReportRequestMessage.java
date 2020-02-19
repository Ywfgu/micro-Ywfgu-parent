package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.utils.CachedMillisecondClock;
import com.ltyc.sms.common.utils.MsgId;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppReportRequestMessage extends DefaultMessage {
    private static final long serialVersionUID = -4631945859346437882L;

    private MsgId msgId = new MsgId();
    private String stat = GlobalConstance.emptyString;
    private String submitTime = String.format("%ty%<tm%<td%<tH%<tM", CachedMillisecondClock.INS.now());
    private String doneTime = String.format("%ty%<tm%<td%<tH%<tM", CachedMillisecondClock.INS.now());
    private String destterminalId = GlobalConstance.emptyString;
    private long smscSequence = 0;

    public MsgId getMsgId() {
        return msgId;
    }

    public void setMsgId(MsgId msgId) {
        this.msgId = msgId;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(String doneTime) {
        this.doneTime = doneTime;
    }

    public String getDestterminalId() {
        return destterminalId;
    }

    public void setDestterminalId(String destterminalId) {
        this.destterminalId = destterminalId;
    }

    public long getSmscSequence() {
        return smscSequence;
    }

    public void setSmscSequence(long smscSequence) {
        this.smscSequence = smscSequence;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("CmppReportRequestMessage [msgId=%s, stat=%s, submitTime=%s, doneTime=%s, destterminalId=%s, smscSequence=%s]", msgId, stat,
                submitTime, doneTime, destterminalId, smscSequence);
    }

}
