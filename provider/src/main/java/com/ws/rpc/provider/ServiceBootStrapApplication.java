package com.ws.rpc.provider;

import com.ws.rpc.common.constant.NettyConstant;
import com.ws.rpc.provider.server.NettyRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceBootStrapApplication implements CommandLineRunner {

    @Autowired
    NettyRpcServer nettyRpcServer;

    public static void main(String[] args) {
        SpringApplication.run(ServiceBootStrapApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyRpcServer.startServer(NettyConstant.hostName,NettyConstant.PORT);
            }
        }).start();
    }
}
