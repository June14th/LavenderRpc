package com.ws.rpc.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 请求类名
     */
    private String className;
    /**
     * 请求方法名
     */
    private String methodName;
    /**
     * 请求参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 请求传入参数
     */
    private Object[] parameters;
}
