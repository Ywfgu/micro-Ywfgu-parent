package com.ltyc.netty;

import com.google.common.primitives.Bytes;
import com.ltyc.netty.utils.ByteUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.codec.digest.DigestUtils;
import java.nio.charset.Charset;

/**
 * @author guht
 * @version 1.0
 * @Description
 * @create 2019/11/13
 */
public class SessionLoginManager extends ChannelDuplexHandler {

    public static final String ICP001 = "ICP001";
    protected boolean state = false;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (!state) {
            doLogin(ctx.channel());
            state =true;
        }
        ctx.fireChannelActive();
    }

    protected void doLogin(Channel ch) {
        String username = "900030";
        String password = ICP001;
        // TODO 发送连接请求 ,创建密码
        byte[] sourceAddr = username.getBytes(Charset.forName("UTF-8"));

        String timestamp = DateFormatUtils.format(System.currentTimeMillis(), "MMddHHmmss");
        byte[] userBytes = username.getBytes(Charset.forName("UTF-8"));
        byte[] passwdBytes = password.getBytes(Charset.forName("UTF-8"));
        byte[] timestampBytes = timestamp.getBytes(Charset.forName("UTF-8"));
        byte[] AuthenticatorSource =DigestUtils.md5(Bytes.concat(userBytes, new byte[9], passwdBytes, timestampBytes));
        byte[] version = new byte[]{48};

        ch.writeAndFlush(ByteUtils.mergeBytes(sourceAddr,AuthenticatorSource,version,timestampBytes));
    }
}
