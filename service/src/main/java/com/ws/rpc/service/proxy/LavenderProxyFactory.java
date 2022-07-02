package com.ws.rpc.service.proxy;

import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class LavenderProxyFactory {

    private static Map<Class,Object> SERVICE_PROXY = new HashMap<>();

    public static Object getProxy(Class<?> serviceClass) {
        Object proxy = SERVICE_PROXY.get(serviceClass);
        if(proxy == null){
            proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{serviceClass},
                    new LavenderInvokeHandler());
            SERVICE_PROXY.put(serviceClass,proxy);
        }
        return proxy;
    }
}
