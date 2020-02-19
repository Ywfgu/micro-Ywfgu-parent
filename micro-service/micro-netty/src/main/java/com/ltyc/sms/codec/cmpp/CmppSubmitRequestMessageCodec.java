package com.ltyc.sms.codec.cmpp;

import com.ltyc.sms.common.GlobalConstance;
import com.ltyc.sms.common.SmsDcs;
import com.ltyc.sms.common.utils.CMPPCommonUtil;
import com.ltyc.sms.common.utils.DefaultMsgIdUtil;
import com.ltyc.sms.common.utils.NettyByteBufUtil;
import com.ltyc.sms.entity.PacketType;
import com.ltyc.sms.entity.cmpp30.msg.CmppSubmitRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.Message;
import com.ltyc.sms.entity.cmpp30.packet.CmppPacketType;
import com.ltyc.sms.entity.cmpp30.packet.Cmpp30SubmitRequest;
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
public class CmppSubmitRequestMessageCodec extends MessageToMessageCodec<Message, CmppSubmitRequestMessage> {
    private final static Logger logger = LoggerFactory.getLogger(CmppSubmitRequestMessageCodec.class);
    private PacketType packetType;

    /**
     *
     */
    public CmppSubmitRequestMessageCodec() {
        this(CmppPacketType.CMPPSUBMITREQUEST);
    }

    public CmppSubmitRequestMessageCodec(PacketType packetType) {
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

        CmppSubmitRequestMessage requestMessage = new CmppSubmitRequestMessage(msg.getHeader());

        ByteBuf bodyBuffer = Unpooled.wrappedBuffer(msg.getBodyBuffer());

        requestMessage.setMsgid(DefaultMsgIdUtil.bytes2MsgId(toArray(bodyBuffer, Cmpp30SubmitRequest.MSGID.getLength())));


        requestMessage.setPktotal(bodyBuffer.readUnsignedByte());
        requestMessage.setPknumber(bodyBuffer.readUnsignedByte());

        requestMessage.setRegisteredDelivery(bodyBuffer.readUnsignedByte());
        requestMessage.setMsglevel(bodyBuffer.readUnsignedByte());
        requestMessage.setServiceId(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.SERVICEID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());
        requestMessage.setFeeUserType(bodyBuffer.readUnsignedByte());

        requestMessage.setFeeterminalId(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.FEETERMINALID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setFeeterminaltype(bodyBuffer.readUnsignedByte());

        requestMessage.setTppid(bodyBuffer.readUnsignedByte());
        requestMessage.setTpudhi(bodyBuffer.readUnsignedByte());
        requestMessage.setMsgfmt(new SmsDcs((byte)bodyBuffer.readUnsignedByte()));

        requestMessage.setMsgsrc(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.MSGSRC.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setFeeType(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.FEETYPE.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setFeeCode(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.FEECODE.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setValIdTime(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.VALIDTIME.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setAtTime(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.ATTIME.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        requestMessage.setSrcId(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.SRCID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        short destUsrtl = bodyBuffer.readUnsignedByte();
        String[] destTermId = new String[destUsrtl];
        for (int i = 0; i < destUsrtl; i++) {
            destTermId[i] = bodyBuffer.readCharSequence(Cmpp30SubmitRequest.DESTTERMINALID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim();
        }
        requestMessage.setDestterminalId(destTermId);

        requestMessage.setDestterminaltype(bodyBuffer.readUnsignedByte());

//        short msgLength = (short)(LongMessageFrameHolder.getPayloadLength(requestMessage.getMsgfmt().getAlphabet(),bodyBuffer.readUnsignedByte()) & 0xffff);
        short msgLength = 0;

        byte[] contentbytes = new byte[msgLength];
        bodyBuffer.readBytes(contentbytes);
        requestMessage.setMsgContentBytes(contentbytes);
        requestMessage.setMsgLength((short)msgLength);

        requestMessage.setLinkID(bodyBuffer.readCharSequence(Cmpp30SubmitRequest.LINKID.getLength(),GlobalConstance.defaultTransportCharset).toString().trim());

        out.add(requestMessage);
        ReferenceCountUtil.release(bodyBuffer);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, CmppSubmitRequestMessage requestMessage, List<Object> out) throws Exception {
        assert(requestMessage.getDestUsrtl()>0);
        ByteBuf bodyBuffer = ctx.alloc().buffer(Cmpp30SubmitRequest.ATTIME.getBodyLength() + requestMessage.getMsgLength() + (requestMessage.getDestUsrtl()-1)
                * Cmpp30SubmitRequest.DESTTERMINALID.getLength());

        bodyBuffer.writeBytes(DefaultMsgIdUtil.msgId2Bytes(requestMessage.getMsgid()));

        bodyBuffer.writeByte(requestMessage.getPktotal());
        bodyBuffer.writeByte(requestMessage.getPknumber());
        bodyBuffer.writeByte(requestMessage.getRegisteredDelivery());
        bodyBuffer.writeByte(requestMessage.getMsglevel());

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getServiceId().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.SERVICEID.getLength(), 0));

        bodyBuffer.writeByte(requestMessage.getFeeUserType());

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getFeeterminalId().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.FEETERMINALID.getLength(), 0));
        bodyBuffer.writeByte(requestMessage.getFeeterminaltype());
        bodyBuffer.writeByte(requestMessage.getTppid());
        bodyBuffer.writeByte(requestMessage.getTpudhi());
        bodyBuffer.writeByte(requestMessage.getMsgfmt().getValue());

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getMsgsrc().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.MSGSRC.getLength(), 0));

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getFeeType().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.FEETYPE.getLength(), 0));

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getFeeCode().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.FEECODE.getLength(), 0));

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getValIdTime().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.VALIDTIME.getLength(), 0));

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getAtTime().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.ATTIME.getLength(), 0));

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getSrcId().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.SRCID.getLength(), 0));

        bodyBuffer.writeByte(requestMessage.getDestUsrtl());
        for (int i = 0; i < requestMessage.getDestUsrtl(); i++) {
            String[] destTermId = requestMessage.getDestterminalId();
            bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(destTermId[i].getBytes(GlobalConstance.defaultTransportCharset),
                    Cmpp30SubmitRequest.DESTTERMINALID.getLength(), 0));
        }
        bodyBuffer.writeByte(requestMessage.getDestterminaltype());

        bodyBuffer.writeByte(requestMessage.getMsgLength());

        bodyBuffer.writeBytes(requestMessage.getMsgContentBytes());

        bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getLinkID().getBytes(GlobalConstance.defaultTransportCharset),
                Cmpp30SubmitRequest.LINKID.getLength(), 0));

        // bodyBuffer.writeBytes(CMPPCommonUtil.ensureLength(requestMessage.getReserve().getBytes(GlobalConstance.defaultTransportCharset),
        // Cmpp30SubmitRequest.RESERVE.getLength(), 0));/**cmpp3.0 无该字段，不进行编解码
        // */
        requestMessage.setBodyBuffer(toArray(bodyBuffer,bodyBuffer.readableBytes()));
        requestMessage.getHeader().setBodyLength(requestMessage.getBodyBuffer().length);
        out.add(requestMessage);
        ReferenceCountUtil.release(bodyBuffer);
    }
}
