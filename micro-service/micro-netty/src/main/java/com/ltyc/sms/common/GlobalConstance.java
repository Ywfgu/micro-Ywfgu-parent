package com.ltyc.sms.common;

import com.ltyc.sms.handler.BlackHoleHandler;
import com.ltyc.sms.handler.cmpp.CmppServerIdleStateHandler;
import com.ltyc.sms.session.SessionState;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2020/2/5
 */
public class GlobalConstance {
    public final static byte[] emptyBytes= new byte[0];
    public final static String[] emptyStringArray= new String[0];
    public final static String codecName = "codecName";
    public final static String emptyString = "";
    public final static String IdleCheckerHandlerName = "IdleStateHandler";
    public final static String loggerNamePrefix = "entity.%s";
    public static final Charset defaultTransportCharset = Charset.forName("UTF-8");
    public final static AttributeKey<SessionState> attributeKey = AttributeKey.newInstance(SessionState.Connect.name());
    public static final SmsDcs defaultmsgfmt = SmsDcs.getGeneralDataCodingDcs(SmsAlphabet.ASCII, SmsMsgClass.CLASS_UNKNOWN);

    public final static BlackHoleHandler blackhole = new BlackHoleHandler();
    public final static CmppServerIdleStateHandler idleHandler = new CmppServerIdleStateHandler();
}
