package com.ws.rpc.consumer.handler;

import com.ws.rpc.common.RpcResponse;
import com.ws.rpc.common.protocol.LavenderProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;


@Component

public class NettyRpcClientHandler extends SimpleChannelInboundHandler<LavenderProtocol> implements Callable {

    ChannelHandlerContext context;
    private Object reqMsg;
    private Object respMsg;

    public void setReqMsg(Object reqMsg) {
        this.reqMsg = reqMsg;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, LavenderProtocol msg) throws Exception {
        System.out.println("接收到服务器消息");
        respMsg = msg.getBody();
        notify();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("---建立连接---");
        context = ctx;
    }

    /**
     * 给服务器发送消息
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        //发送给服务器消息
        context.writeAndFlush(reqMsg);
        System.out.println("发送消息并等待服务器回复");
        wait();
        //返回结果
        System.out.println("返回rpcResponse");
        return respMsg;
    }
}
