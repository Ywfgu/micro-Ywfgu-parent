package com.ltyc.sms.entity.cmpp30.msg;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.SmsDcs;
import com.ltyc.sms.common.utils.CMPPCommonUtil;
import com.ltyc.sms.common.utils.DefaultSequenceNumberUtil;
import com.ltyc.sms.common.utils.MsgId;
import com.ltyc.sms.entity.LongSMSMessage;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppSubmitRequestMessage extends DefaultMessage implements LongSMSMessage<CmppSubmitRequestMessage> {
    private static final long serialVersionUID = 1369427662600486133L;
    private MsgId msgid = new MsgId();

    private short registeredDelivery = 0;
    private short msglevel = 9;
    private String serviceId = GlobalConstance.emptyString;
    private short feeUserType = 2;
    private String feeterminalId = GlobalConstance.emptyString;
    private short feeterminaltype = 0;

    private String msgsrc = GlobalConstance.emptyString;
    private String feeType = "01";
    private String feeCode = "000000";
    private String valIdTime = GlobalConstance.emptyString;
    private String atTime = GlobalConstance.emptyString;
    private String srcId = GlobalConstance.emptyString;
    private String[] destterminalId = GlobalConstance.emptyStringArray;
    private short destterminaltype = 0;

    private String linkID = GlobalConstance.emptyString;

    private String reserve = GlobalConstance.emptyString;


    private short pktotal = 1;
    private short pknumber = 1;
    private short tppid = 0;// 0是普通GSM 类型，点到点方式 ,127 :写sim卡
    private short tpudhi = 0; // 0:msgcontent不带协议头。1:带有协议头
    private SmsDcs msgfmt = GlobalConstance.defaultmsgfmt;
    private short msgLength = 140;
    private byte[] msgContentBytes = GlobalConstance.emptyBytes;

    public CmppSubmitRequestMessage(Header header) {
        super(CmppPacketType.CMPPSUBMITREQUEST, header);
    }

    public CmppSubmitRequestMessage() {
        super(CmppPacketType.CMPPSUBMITREQUEST);
    }

    public MsgId getMsgid() {
        return msgid;
    }

    public void setMsgid(MsgId msgid) {
        this.msgid = msgid;
    }

    public boolean isReport() {
        return false;
    }

    public short getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(short registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public short getMsglevel() {
        return msglevel;
    }

    public void setMsglevel(short msglevel) {
        this.msglevel = msglevel;
    }

    public String getServiceId() {
        return serviceId == null ? "" : serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public short getFeeUserType() {
        return feeUserType;
    }

    public void setFeeUserType(short feeUserType) {
        this.feeUserType = feeUserType;
    }

    public String getFeeterminalId() {
        return feeterminalId == null ? "" : feeterminalId;
    }

    public void setFeeterminalId(String feeterminalId) {
        this.feeterminalId = feeterminalId;
    }

    public short getFeeterminaltype() {
        return feeterminaltype;
    }

    public void setFeeterminaltype(short feeterminaltype) {
        this.feeterminaltype = feeterminaltype;
    }

    public String getMsgsrc() {
        return msgsrc == null ? "" : msgsrc;
    }

    public void setMsgsrc(String msgsrc) {
        this.msgsrc = msgsrc;
    }

    public String getFeeType() {
        return feeType == null ? "" : feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeCode() {
        return feeCode == null ? "" : feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getValIdTime() {
        return valIdTime == null ? "" : valIdTime;
    }

    public void setValIdTime(String valIdTime) {
        this.valIdTime = valIdTime;
    }

    public String getAtTime() {
        return atTime == null ? "" : atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public String getSrcId() {
        return srcId == null ? "" : srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public short getDestUsrtl() {
        return (short) this.destterminalId.length;
    }

    public String[] getDestterminalId() {
        return destterminalId;
    }

    public void setDestterminalId(String[] destterminalId) {
        this.destterminalId = destterminalId;
    }

    public void setDestterminalId(String destterminalId) {
        this.destterminalId = new String[] { destterminalId };
    }

    public short getDestterminaltype() {
        return destterminaltype;
    }

    public void setDestterminaltype(short destterminaltype) {
        this.destterminaltype = destterminaltype;
    }

    public String getLinkID() {
        return linkID == null ? "" : linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getMsgContent() {


        return "";
    }


    public byte[] getMsgContentBytes() {
        return msgContentBytes;
    }

    public void setMsgContentBytes(byte[] msgContentBytes) {
        this.msgContentBytes = msgContentBytes;
    }

    public short getPktotal() {
        return pktotal;
    }

    public void setPktotal(short pktotal) {
        this.pktotal = pktotal;
    }

    public short getPknumber() {
        return pknumber;
    }

    public void setPknumber(short pknumber) {
        this.pknumber = pknumber;
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

    public static CmppSubmitRequestMessage create(String phone, String spid, String text) {
        CmppSubmitRequestMessage ret = new CmppSubmitRequestMessage();
        ret.setDestterminalId(new String[] { phone });
        ret.setSrcId(spid);
        return ret;
    }

    @Override
    public CmppSubmitRequestMessage clone() throws CloneNotSupportedException {
        return (CmppSubmitRequestMessage) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CmppSubmitRequestMessage [msgid=").append(msgid).append(", serviceId=").append(serviceId).append(", srcId=").append(srcId)
                .append(", msgsrc=").append(msgsrc).append(", destterminalId=").append(Arrays.toString(destterminalId)).append(", msgContent=")
                .append(getMsgContent()).append(", sequenceId=").append(getHeader().getSequenceId()).append("]");
        return sb.toString();
    }

    private List<CmppSubmitRequestMessage> fragments = null;

}
