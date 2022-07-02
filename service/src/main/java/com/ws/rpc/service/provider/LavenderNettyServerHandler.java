package com.ws.rpc.service.provider;

import com.ws.rpc.service.common.LavenderProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class LavenderNettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        LavenderProtocol message = (LavenderProtocol) msg;
        LavenderProtocol response = new LavenderProtocol();
        response.setSessionId(message.getSessionId());
        response.setType((byte) 1);
        System.out.println("服务端收到message");
        String result = null;
        if(message.getLength() != 0){
            Object body = message.getBody();
            System.out.println("其中的数据为：" + (String) body);
            //调用接口处理
            result = new HelloServiceImpl().hello(body.toString());
        }else{
            result = "没有参数传入";
        }
        byte[] bytes = result.getBytes();
        response.setLength(bytes.length);
        response.setBody(result);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
