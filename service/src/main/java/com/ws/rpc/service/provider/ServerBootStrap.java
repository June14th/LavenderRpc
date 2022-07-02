package com.ws.rpc.service.provider;

import com.ws.rpc.service.protocol.NettyConstant;

//启动服务提供者
public class ServerBootStrap {
    public static void main(String[] args) {
        //启动服务器
        LavenderNettyServer.startServer(NettyConstant.hostName,NettyConstant.PORT);
    }
}
