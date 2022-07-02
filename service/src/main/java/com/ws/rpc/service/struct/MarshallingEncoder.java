package com.ws.rpc.service.struct;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class MarshallingEncoder {

    protected void encode(Object msg, ByteBuf out) throws Exception{

    }
}
