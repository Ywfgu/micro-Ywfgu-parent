package com.ltyc.sms.entity.cmpp30.packet;

import com.ltyc.sms.codec.cmpp.*;
import com.ltyc.sms.entity.PacketStructure;
import com.ltyc.sms.entity.PacketType;
import io.netty.handler.codec.MessageToMessageCodec;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public enum CmppPacketType implements PacketType {
    CMPPCONNECTREQUEST(0x00000001L, Cmpp30ConnectRequest.class, CmppConnectRequestMessageCodec.class),
    CMPPCONNECTRESPONSE(0x80000001L, Cmpp30ConnectResponse.class, CmppConnectResponseMessageCodec.class),
    //    CMPPTERMINATEREQUEST(0x00000002L, CmppTerminateRequest.class,CmppTerminateRequestMessageCodec.class),
//    CMPPTERMINATERESPONSE(0x80000002L, CmppTerminateResponse.class,CmppTerminateResponseMessageCodec.class),
    CMPPSUBMITREQUEST(0x00000004L, Cmpp30SubmitRequest.class,CmppSubmitRequestMessageCodec.class),
    CMPPSUBMITRESPONSE(0x80000004L, Cmpp30SubmitResponse.class,CmppSubmitResponseMessageCodec.class),
    CMPPDELIVERREQUEST(0x00000005L, Cmpp30DeliverRequest.class,CmppDeliverRequestMessageCodec.class),
    CMPPDELIVERRESPONSE(0x80000005L, Cmpp30DeliverResponse.class,CmppDeliverResponseMessageCodec.class),
//    CMPPQUERYREQUEST(0x00000006L, CmppQueryRequest.class,CmppQueryRequestMessageCodec.class),
//    CMPPQUERYRESPONSE(0x80000006L, CmppQueryResponse.class,CmppQueryResponseMessageCodec.class),
//    CMPPCANCELREQUEST(0x00000007L, CmppCancelRequest.class,CmppCancelRequestMessageCodec.class),
//    CMPPCANCELRESPONSE(0x80000007L, CmppCancelResponse.class,CmppCancelResponseMessageCodec.class),
    CMPPACTIVETESTREQUEST(0x00000008L, Cmpp30ActiveTestRequest.class, CmppActiveTestRequestMessageCodec.class),
    CMPPACTIVETESTRESPONSE(0x80000008L, Cmpp30ActiveTestResponse.class, CmppActiveTestResponseMessageCodec.class);

    private long commandId;
    private Class<? extends PacketStructure> packetStructure;
    private Class<? extends MessageToMessageCodec> codec;

    private CmppPacketType(long commandId, Class<? extends PacketStructure> packetStructure, Class<? extends MessageToMessageCodec> codec) {
        this.commandId = commandId;
        this.packetStructure = packetStructure;
        this.codec = codec;
    }

    @Override
    public long getCommandId() {
        return commandId;
    }

    @Override
    public PacketStructure[] getPacketStructures() {
        return packetStructure.getEnumConstants();
    }

    @Override
    public long getAllCommandId() {
        long defaultId = 0x0;
        long allCommandId = 0x0;
        for (CmppPacketType packetType : CmppPacketType.values()) {
            allCommandId |= packetType.commandId;
        }
        return allCommandId ^ defaultId;
    }

    @Override
    public MessageToMessageCodec getCodec() {

        try {
            return codec.newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
