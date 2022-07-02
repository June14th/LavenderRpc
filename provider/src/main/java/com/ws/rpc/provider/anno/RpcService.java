package com.ws.rpc.provider.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于暴露服务端口
 */
@Target(ElementType.TYPE) //用于类上的注解
@Retention(RetentionPolicy.RUNTIME) //在运行时可获取到
public @interface RpcService {
}
