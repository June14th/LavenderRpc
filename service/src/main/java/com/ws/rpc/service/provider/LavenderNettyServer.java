package com.ws.rpc.service.provider;

import com.ws.rpc.service.common.LavenderMessageDecoder;
import com.ws.rpc.service.common.LavenderMessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class LavenderNettyServer {

    public static void startServer(String hostName,int port){
        startServer0(hostName,port);
    }


    private static void startServer0(String hostName,int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
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
                            pipeline.addLast(new LavenderNettyServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(hostName, port).sync();
            System.out.println("-----LavenderNettyServer start!!!-----");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
