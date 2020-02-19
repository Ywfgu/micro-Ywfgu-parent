package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.SmsDcs;
import com.ltyc.sms.common.utils.CMPPCommonUtil;
import com.ltyc.sms.common.utils.DefaultMsgIdUtil;
import com.ltyc.sms.entity.PacketType;
import com.ltyc.sms.entity.cmpp30.msg.CmppDeliverRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppReportRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30DeliverRequest;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30ReportRequest;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ltyc.sms.common.utils.NettyByteBufUtil.toArray;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CmppDeliverRequestMessageCodec extends MessageToMessageCodec<Message, CmppDeliverRequestMessage> {
    private static final Logger logger = LoggerFactory.getLogger(CmppDeliverRequestMessageCodec.class);
    private PacketType packetType;

    /**
     *
     */
    public CmppDeliverRequestMessageCodec() {
        this(CmppPacketType.CMPPDELIVERREQUEST);
    }

    public CmppDeliverRequestMessageCodec(PacketType packetType) {
        this.packetType = packetType;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {

        long commandId = ((Long) msg.getHeader().getCommandId()).longValue();
        if (packetType.getCommandId() != commandId) {
            // 不解析，交给下一个codec
            out.add(msg);
            return;
        }

        CmppDeliverRequestMessage requestMessage = new CmppDeliverRequestMessage(msg.getHeader());

        ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());
        requestMessage.setMsgId(DefaultMsgIdUtil.bytes2MsgId(toArray(bodyBuffer, Cmpp30DeliverRequest.MSGID.getLength())));
        requestMessage.setDestId(bodyBuffer.readCharSequence(Cmpp30DeliverRequest.DESTID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
        requestMessage.setServiceid(bodyBuffer.readCharSequence(Cmpp30DeliverRequest.SERVICEID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setTppid(bodyBuffer.readUnsignedByte());
        requestMessage.setTpudhi(bodyBuffer.readUnsignedByte());
        requestMessage.setMsgfmt(new SmsDcs((byte)bodyBuffer.readUnsignedByte()));

        requestMessage.setSrcterminalId(bodyBuffer.readCharSequence(Cmpp30DeliverRequest.SRCTERMINALID.getLength(),GlobalConstance.defaultTransportCharset)
                .toString().trim());
        requestMessage.setSrcterminalType(bodyBuffer.readUnsignedByte());

        short registeredDelivery = bodyBuffer.readUnsignedByte();

//        int frameLength = LongMessageFrameHolder.getPayloadLength(requestMessage.getMsgfmt().getAlphabet(),bodyBuffer.readUnsignedByte());
        int frameLength =0;
        if (registeredDelivery == 0) {
            byte[] contentbytes = new byte[frameLength];
            bodyBuffer.readBytes(contentbytes);
            requestMessage.setMsgContentBytes(contentbytes);
            requestMessage.setMsgLength((short)frameLength);
        } else {
            if(frameLength != Cmpp30ReportRequest.DESTTERMINALID.getBodyLength()){
                logger.warn("CmppDeliverRequestMessage - MsgContent length is {}. should be {}.",frameLength, Cmpp30ReportRequest.DESTTERMINALID.getBodyLength());
            };
            requestMessage.setReportRequestMessage(new CmppReportRequestMessage());
            requestMessage.getReportRequestMessage().setMsgId(DefaultMsgIdUtil.bytes2MsgId(toArray(bodyBuffer,Cmpp30ReportRequest.MSGID.getLength())));
            requestMessage.getReportRequestMessage().setStat(
                    bodyBuffer.readCharSequence(Cmpp30ReportRequest.STAT.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
            requestMessage.getReportRequestMessage().setSubmitTime(
                    bodyBuffer.readCharSequence(Cmpp30ReportRequest.SUBMITTIME.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
            requestMessage.getReportRequestMessage().setDoneTime(
                    bodyBuffer.readCharSequence(Cmpp30ReportRequest.DONETIME.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
            requestMessage.getReportRequestMessage().setDestterminalId(
                    bodyBuffer.readCharSequence(Cmpp30ReportRequest.DESTTERMINALID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
            requestMessage.getReportRequestMessage().setSmscSequence(bodyBuffer.readUnsignedInt());
        }
        //卓望发送的状态报告 少了11个字节， 剩下的字节全部读取
        requestMessage.setLinkid(bodyBuffer.readCharSequence(bodyBuffer.readableBytes(),GlobalConstance.defaultTransportCharset).toString().trim());

        out.add(requestMessage);

        ReferenceCountUtil.release(bodyBuffer);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppDeliverRequestMessage requestMessage, List<Object> out) throws Exception {


        // bodyBuffer 会在CmppHeaderCodec.encode里释放
        ByteBuf bodyBuffer = ctx.alloc().buffer(Cmpp30DeliverRequest.DESTID.getBodyLength() + requestMessage.getMsgLength());
        bodyBuffer.writeBytes(DefaultMsgIdUtil.msgId2Bytes(requestMessage.getMsgId()));
        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getDestId().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30DeliverRequest.DESTID.getLength(), 0));
        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getServiceid().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30DeliverRequest.SERVICEID.getLength(), 0));
        bodyBuffer.writeByte(requestMessage.getTppid());
        bodyBuffer.writeByte(requestMessage.getTpudhi());
        bodyBuffer.writeByte(requestMessage.getMsgfmt().getValue());
        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getSrcterminalId().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30DeliverRequest.SRCTERMINALID.getLength(), 0));
        bodyBuffer.writeByte(requestMessage.getSrcterminalType());
        bodyBuffer.writeByte(requestMessage.isReport()?1:0);


        if (!requestMessage.isReport()) {
            bodyBuffer.writeByte(requestMessage.getMsgLength());

            bodyBuffer.writeBytes(requestMessage.getMsgContentBytes());

        } else {
            bodyBuffer.writeByte(Cmpp30ReportRequest.DESTTERMINALID.getBodyLength());

            bodyBuffer.writeBytes(DefaultMsgIdUtil.msgId2Bytes(requestMessage.getReportRequestMessage().getMsgId()));
            bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(
                    requestMessage.getReportRequestMessage().getStat().getBytes(GlobalConstance.defaultTransportCharset),
                    Cmpp30ReportRequest.STAT.getLength(), 0));
            bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(
                    requestMessage.getReportRequestMessage().getSubmitTime().getBytes(GlobalConstance.defaultTransportCharset),
                    Cmpp30ReportRequest.SUBMITTIME.getLength(), 0));
            bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(
                    requestMessage.getReportRequestMessage().getDoneTime().getBytes(GlobalConstance.defaultTransportCharset),
                    Cmpp30ReportRequest.DONETIME.getLength(), 0));
            bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(
                    requestMessage.getReportRequestMessage().getDestterminalId().getBytes(GlobalConstance.defaultTransportCharset),
                    Cmpp30ReportRequest.DESTTERMINALID.getLength(), 0));

            bodyBuffer.writeInt((int) requestMessage.getReportRequestMessage().getSmscSequence());
        }

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getLinkid().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30DeliverRequest.LINKID.getLength(), 0));


        requestMessage.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        requestMessage.getHeader().setBodyLength(requestMessage.getBodyBuffer().length);
        out.add(requestMessage);
        ReferenceCountUtil.release(bodyBuffer);

    }
}
