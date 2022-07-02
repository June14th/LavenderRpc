package com.ws.rpc.consumer.processor;

import com.ws.rpc.consumer.anno.RpcReference;
import com.ws.rpc.consumer.proxy.RpcClientProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * bean的后置增强
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    RpcClientProxy rpcClientProxy;

    /**
     * 自定义注解的注入
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //查看bean的字段中有没有对应的注解
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields){
            RpcReference annotation = field.getAnnotation(RpcReference.class);
            if(annotation != null){
                //获取代理对象
                Object proxy = rpcClientProxy.getProxy(field.getType());
                field.setAccessible(true);
                try {
                    //属性注入
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
