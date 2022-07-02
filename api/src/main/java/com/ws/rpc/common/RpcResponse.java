package com.ws.rpc.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {
    /**
     * 响应id
     */
    private String responseId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 返回结果
     */
    private Object result;
}
