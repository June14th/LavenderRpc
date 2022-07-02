package com.ws.rpc.service.proxy;

import com.ws.rpc.common.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LavenderInvokeHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();
        method.getDeclaringClass().getName();
        return null;
    }
}
