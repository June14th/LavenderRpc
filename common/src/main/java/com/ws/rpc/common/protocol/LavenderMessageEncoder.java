package com.ws.rpc.common.protocol;

import com.ws.rpc.common.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Slf4j
public class LavenderMessageEncoder extends MessageToByteEncoder<LavenderProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, LavenderProtocol msg, ByteBuf out) throws Exception {
        log.info("开始编码");
        out.writeLong(msg.getSessionId());
        System.out.println(msg.getSessionId());
        out.writeByte(msg.getType());
        System.out.println(msg.getType());
        Object body = msg.getBody();
        if(body == null){
            out.writeInt(0);
        }else{
            byte[] bytes = CommonUtil.getByteArrayByObject(body);
            out.writeInt(bytes.length); //写入消息体长度:占4个字节
            System.out.println(msg.getLength());
            out.writeBytes(bytes); //写入消息体内容
        }
        log.info("编码完成");
    }
}
