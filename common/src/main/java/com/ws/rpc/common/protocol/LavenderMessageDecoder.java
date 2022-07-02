package com.ws.rpc.common.protocol;

import com.ws.rpc.common.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

@Slf4j
public class LavenderMessageDecoder extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.info("开始解码");
        LavenderProtocol record = new LavenderProtocol();
        record.setSessionId(in.readLong());
        record.setType(in.readByte());
        if(in.readableBytes()>4){
            int length = in.readInt();
            record.setLength(length);
            byte[] contents = new byte[length];
            //这里解决了TCP粘包问题
            in.readBytes(contents,0,length);
            record.setBody(CommonUtil.getObjectByByteArray(contents));
            out.add(record);
        }else{
            log.error("消息为空");
        }
        log.info("解码完成");
    }
}
