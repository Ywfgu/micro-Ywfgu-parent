import com.ltyc.sms.common.utils.CachedMillisecondClock;
import com.ltyc.sms.entity.cmpp30.msg.CmppDeliverRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppReportRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppSubmitRequestMessage;
import com.ltyc.sms.entity.cmpp30.msg.CmppSubmitResponseMessage;
import com.ltyc.sms.handler.api.MessageReceiveHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/6
 */
public class CMPPMessageReceiveHandler extends MessageReceiveHandler {
    @Override
    protected ChannelFuture reponse(final ChannelHandlerContext ctx, Object msg) {
        int result = RandomUtils.nextInt(0, 100) > 97 ? 0 : 0 ;
        if (msg instanceof CmppSubmitRequestMessage) {

            //接收到 CmppSubmitRequestMessage 消息
            CmppSubmitRequestMessage e = (CmppSubmitRequestMessage) msg;

            CmppSubmitResponseMessage resp = new CmppSubmitResponseMessage(e.getHeader().getSequenceId());
            resp.setResult(result);
            ChannelFuture future = ctx.channel().writeAndFlush(resp);
            return result == 0 ? future : null;

//            CmppDeliverRequestMessage deliver = new CmppDeliverRequestMessage();
//            deliver.setDestId(e.getSrcId());
//            deliver.setSrcterminalId(e.getDestterminalId()[0]);
//            CmppReportRequestMessage report = new CmppReportRequestMessage();
//            report.setDestterminalId(deliver.getSrcterminalId());
//            report.setMsgId(responseMessage.getMsgId());
//            String t = DateFormatUtils.format(CachedMillisecondClock.INS.now(), "yyMMddHHmm");
//            report.setSubmitTime(t);
//            report.setDoneTime(t);
//            report.setStat("DELIVRD");
//            report.setSmscSequence(0);
//            deliver.setReportRequestMessage(report);
//            reportlist.add(deliver);
//
//
//            final CmppSubmitResponseMessage resp = new CmppSubmitResponseMessage(e.getHeader().getSequenceId());
//            resp.setResult(result);
//
//            ChannelFuture future = ctx.channel().writeAndFlush(resp);
//            return result == 0 ? future : null;
        }

        return null;
    }
}
