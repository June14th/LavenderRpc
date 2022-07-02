package com.ws.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.ws.rpc.common.RpcRequest;
import com.ws.rpc.common.RpcResponse;
import com.ws.rpc.common.protocol.LavenderProtocol;
import com.ws.rpc.common.util.CommonUtil;
import com.ws.rpc.consumer.client.NettyRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 客户端代理类
 */
@Component
public class RpcClientProxy {

    @Autowired
    NettyRpcClient nettyRpcClient;

    Map<Class,Object> SERVICE_PROXY = new HashMap<>();

    public Object getProxy(Class serviceClass){
        System.out.println("开始创建代理对象...");
        Object proxy = SERVICE_PROXY.getOrDefault(serviceClass,null);
        if(proxy == null){
            proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{serviceClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            //封装请求对象
                            System.out.println("开始封装请求对象");
                            LavenderProtocol protocol = new LavenderProtocol();
                            protocol.setSessionId(123456L);
                            protocol.setType((byte) 0);

                            RpcRequest rpcRequest = new RpcRequest();
                            rpcRequest.setRequestId(UUID.randomUUID().toString());
                            rpcRequest.setClassName(method.getDeclaringClass().getName());
                            rpcRequest.setMethodName(method.getName());
                            rpcRequest.setParameterTypes(method.getParameterTypes());
                            rpcRequest.setParameters(args);

                            /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(bos);
                            oos.writeObject(rpcRequest);
                            byte[] bytes = bos.toByteArray();
                            oos.close();
                            bos.close();*/

                            byte[] bytes = CommonUtil.getByteArrayByObject(rpcRequest);
                            protocol.setLength(bytes.length);
                            protocol.setBody(rpcRequest);

                            try {
                                Object msg = nettyRpcClient.send(protocol);//得到结果
                                RpcResponse rpcResponse = (RpcResponse) msg;
                                if(rpcResponse.getError()!=null){
                                    throw new RuntimeException(rpcResponse.getError());
                                }
                                if(rpcResponse.getResult()!=null){
                                    return rpcResponse.getResult();
                                }
                                return null;
                            }catch (Exception e){
                                e.printStackTrace();
                                throw e;
                            }
                        }
                    }
            );
            SERVICE_PROXY.put(serviceClass,proxy);
        }
        return proxy;
    }
}
