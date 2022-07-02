package com.ws.rpc.provider.server;

import com.ws.rpc.common.protocol.LavenderMessageDecoder;
import com.ws.rpc.common.protocol.LavenderMessageEncoder;
import com.ws.rpc.provider.handler.NettyRpcServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyRpcServer{

    @Autowired
    private NettyRpcServerHandler nettyRpcServerHandler;

    public void startServer(String hostName,int port){
        startServer0(hostName,port);
    }


    private void startServer0(String hostName,int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LavenderMessageDecoder());
                            pipeline.addLast(new LavenderMessageEncoder());
                            pipeline.addLast(nettyRpcServerHandler);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(hostName, port).sync();
            System.out.println("-----NettyRpcServer start!!!-----");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
