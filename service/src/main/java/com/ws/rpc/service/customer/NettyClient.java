package com.ws.rpc.service.customer;

import com.ws.rpc.service.common.LavenderMessageDecoder;
import com.ws.rpc.service.common.LavenderMessageEncoder;
import com.ws.rpc.service.protocol.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NettyClient {
    //创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler client;

    //使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass},(proxy,method,args)->{
                    System.out.println("开始创建代理对象...");
                    if(client == null){
                        initClient();
                    }
                    //设置要发给服务器端的信息
                    client.setParam((String) args[0]);
                    return executor.submit(client).get();
                });
    }

    //初始化客户端
    private static void initClient(){
        client = new NettyClientHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LavenderMessageDecoder());
                        pipeline.addLast(new LavenderMessageEncoder());
                        pipeline.addLast(client);
                    }
                });
        try {
            bootstrap.connect(NettyConstant.hostName,NettyConstant.PORT).sync();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
