package com.ws.rpc.service.customer;

import com.ws.rpc.service.interfaces.HelloService;

public class ClientBootStrap {

    //public static final String providerName = "LavenderService#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        //获取代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class);
        //通过代理对象调用服务提供者
        for(int i=0;i<1;i++){
            String res = service.hello("你好");
            System.out.println("res="+ res);
        }
    }
}
