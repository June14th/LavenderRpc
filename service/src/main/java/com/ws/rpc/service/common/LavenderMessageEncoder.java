package com.ws.rpc.service.common;

import com.alibaba.fastjson.JSON;
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
        out.writeByte(msg.getType());
        Object body = msg.getBody();
        if(body == null){
            out.writeInt(0);
        }else{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(body);
            byte[] bytes = bos.toByteArray();
            oos.close();
            bos.close();
            out.writeInt(bytes.length); //写入消息体长度:占4个字节
            out.writeBytes(bytes); //写入消息体内容
        }
        log.info("编码完成");
    }
}
