package com.ws.rpc.service.provider;

import com.ws.rpc.service.interfaces.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        if(message != null){
            return "你好，客户端，已收到你的消息["+message+"]";
        }
        return "未收到消息";
    }
}
