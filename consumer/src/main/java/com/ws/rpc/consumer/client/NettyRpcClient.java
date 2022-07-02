package com.ws.rpc.consumer.client;

import com.ws.rpc.common.constant.NettyConstant;
import com.ws.rpc.common.protocol.LavenderMessageDecoder;
import com.ws.rpc.common.protocol.LavenderMessageEncoder;
import com.ws.rpc.common.protocol.LavenderProtocol;
import com.ws.rpc.consumer.handler.NettyRpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class NettyRpcClient implements InitializingBean, DisposableBean {

    EventLoopGroup group = null;
    Channel channel = null;

    //创建线程池
    ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    NettyRpcClientHandler nettyRpcClientHandler;
    /**
     * 连接服务端
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("开始连接客户端");
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LavenderMessageDecoder());
                            pipeline.addLast(new LavenderMessageEncoder());
                            pipeline.addLast(nettyRpcClientHandler);
                    }
                });
            channel = bootstrap.connect(NettyConstant.hostName, NettyConstant.PORT).sync().channel();
        }catch (Exception e){
            e.printStackTrace();
            if(channel != null){
                channel.close();
            }
            if(group != null){
                group.shutdownGracefully();
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if(channel != null){
            channel.close();
        }
        if(group != null){
            group.shutdownGracefully();
        }
    }

    /**
     * 消息发送
     * @param message
     * @return
     */
    public Object send(LavenderProtocol message) throws ExecutionException,InterruptedException {
        nettyRpcClientHandler.setReqMsg(message);
        Future future = executor.submit(nettyRpcClientHandler);
        return future.get();
    }
}
