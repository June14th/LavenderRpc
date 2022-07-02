package com.ws.rpc.service.customer;

import com.alibaba.fastjson.JSON;
import com.ws.rpc.service.common.LavenderProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;
import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String param;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" channelActive 被调用  ");
        context = ctx;
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(" channelRead 被调用  ");
        LavenderProtocol res = (LavenderProtocol) msg;
        result = (String) res.getBody();
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，之后wait，等待被channelRead唤醒
    @Override
    public synchronized Object call() throws Exception {
        System.out.println(" call1 被调用  ");
        LavenderProtocol req = new LavenderProtocol();
        req.setSessionId(123456L);
        req.setType((byte) 0);
        byte[] bytes = param.getBytes();
        req.setLength(bytes.length);
        req.setBody(param);
        context.writeAndFlush(req);
        wait();
        System.out.println(" call2 被调用  ");
        return result;
    }

    void setParam(String param){
        System.out.println(" setPara  ");
        this.param = param;
    }
}
