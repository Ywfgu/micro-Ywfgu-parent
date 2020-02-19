package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.SmsDcs;
import com.ltyc.sms.common.utils.CMPPCommonUtil;
import com.ltyc.sms.common.utils.DefaultSequenceNumberUtil;
import com.ltyc.sms.common.utils.MsgId;
import com.ltyc.sms.entity.LongSMSMessage;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppDeliverRequestMessage extends DefaultMessage implements LongSMSMessage<CmppDeliverRequestMessage> {
    private static final long serialVersionUID = 4851585208067281751L;
    private MsgId msgId = new MsgId();
    private String destId = GlobalConstance.emptyString;
    private String serviceid = GlobalConstance.emptyString;

    private String srcterminalId = GlobalConstance.emptyString;
    private short srcterminalType = 0;
    private short registeredDelivery = 0;

    private CmppReportRequestMessage reportRequestMessage = null;
    private String linkid = GlobalConstance.emptyString;

    private String reserved = GlobalConstance.emptyString;

    private short tppid = 0;// 0是普通GSM 类型，点到点方式 ,127 :写sim卡
    private short tpudhi = 0; // 0:msgcontent不带协议头。1:带有协议头
    private SmsDcs msgfmt = GlobalConstance.defaultmsgfmt;
    private short msgLength = 140;
    private byte[] msgContentBytes = GlobalConstance.emptyBytes;

    public CmppDeliverRequestMessage(Header header) {
        super(CmppPacketType.CMPPDELIVERREQUEST, header);
    }

    public CmppDeliverRequestMessage() {
        super(CmppPacketType.CMPPDELIVERREQUEST);
    }

    public MsgId getMsgId() {
        return msgId;
    }

    public void setMsgId(MsgId msgId) {
        this.msgId = msgId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getSrcterminalId() {
        return srcterminalId;
    }

    public void setSrcterminalId(String srcterminalId) {
        this.srcterminalId = srcterminalId;
    }

    public short getSrcterminalType() {
        return srcterminalType;
    }

    public void setSrcterminalType(short srcterminalType) {
        this.srcterminalType = srcterminalType;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public CmppReportRequestMessage getReportRequestMessage() {
        return reportRequestMessage;
    }

    public void setReportRequestMessage(CmppReportRequestMessage reportRequestMessage) {
        this.reportRequestMessage = reportRequestMessage;
        if (reportRequestMessage != null) {
            this.registeredDelivery = (short) 1;
        }
    }

    public String getLinkid() {
        return linkid;
    }

    public void setLinkid(String linkid) {
        this.linkid = linkid;
    }

    public boolean isReport() {
        return this.registeredDelivery != 0;
    }

    public String getMsgContent() {


        return "";
    }

    public short getTppid() {
        return tppid;
    }

    public void setTppid(short tppid) {
        this.tppid = tppid;
    }

    public short getTpudhi() {
        return tpudhi;
    }

    public void setTpudhi(short tpudhi) {
        this.tpudhi = tpudhi;
    }

    public SmsDcs getMsgfmt() {
        return msgfmt;
    }

    public void setMsgfmt(SmsDcs msgfmt) {
        this.msgfmt = msgfmt;
    }

    public short getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(short msgLength) {
        this.msgLength = msgLength;
    }

    public byte[] getMsgContentBytes() {
        return msgContentBytes;
    }

    public void setMsgContentBytes(byte[] msgContentBytes) {
        this.msgContentBytes = msgContentBytes;
    }

    @Override
    public CmppDeliverRequestMessage clone() throws CloneNotSupportedException {
        return (CmppDeliverRequestMessage) super.clone();
    }

    @Override
    public String toString() {
        if (isReport()) {
            StringBuilder sb = new StringBuilder();
            sb.append("CmppDeliverRequestMessage [msgId=").append(msgId).append(", destId=").append(destId).append(", srcterminalId=").append(srcterminalId)
                    .append(", getHeader()=").append(getHeader()).append(", ReportRequest=").append(getReportRequestMessage()).append("]");
            return sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("CmppDeliverRequestMessage [msgId=").append(msgId).append(", destId=").append(destId).append(", srcterminalId=").append(srcterminalId)
                .append(", msgContent=").append(getMsgContent()).append(", sequenceId=").append(getHeader().getSequenceId()).append("]");
        return sb.toString();
    }

}
