package com.ws.rpc.service.common;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.String;
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
            ByteArrayInputStream bis=new ByteArrayInputStream(contents);
            ObjectInputStream ois=new ObjectInputStream(bis);
            record.setBody(ois.readObject());
            ois.close();
            bis.close();
            out.add(record);
        }else{
            log.error("消息为空");
        }
        log.info("解码完成");
    }
}
