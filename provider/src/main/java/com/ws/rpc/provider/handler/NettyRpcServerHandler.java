package com.ws.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.ws.rpc.common.RpcRequest;
import com.ws.rpc.common.RpcResponse;
import com.ws.rpc.common.protocol.LavenderProtocol;
import com.ws.rpc.common.util.CommonUtil;
import com.ws.rpc.pojo.User;
import com.ws.rpc.provider.anno.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@ChannelHandler.Sharable //设置通道共享
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<LavenderProtocol> implements ApplicationContextAware {

    static Map<String,Object> SERVICE_INSTANCE_MAP = new HashMap<>();

    /**
     * 将标有 @RpcService的注解 bean进行缓存
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        System.out.println("将标有 @RpcService的注解 bean进行缓存");
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        Set<Map.Entry<String, Object>> entries = serviceMap.entrySet();
        for(Map.Entry<String,Object> entry : entries){
            Object serviceBean = entry.getValue();
            if(serviceBean.getClass().getInterfaces().length == 0){
                throw new RuntimeException("对外暴露的服务必须实现接口");
            }
            String serviceName = serviceBean.getClass().getInterfaces()[0].getName();
            SERVICE_INSTANCE_MAP.put(serviceName,serviceBean);
        }
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, LavenderProtocol msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("服务端收到 message"+msg);
        RpcRequest rpcRequest = (RpcRequest) msg.getBody();
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResponseId(rpcRequest.getRequestId());
        //业务处理
        try {
            rpcResponse.setResult(handler(rpcRequest));//调用handler方法
            System.out.println("业务处理完毕，准备返回数据");
        }catch (Exception e){
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }
        LavenderProtocol protocol = new LavenderProtocol();
        protocol.setSessionId(msg.getSessionId());
        protocol.setType((byte) 1);
        byte[] bytes = CommonUtil.getByteArrayByObject(rpcResponse);
        protocol.setLength(bytes.length);
        protocol.setBody(rpcResponse);
        System.out.println("返回数据");
        System.out.println("结果为:"+rpcResponse.getResult());
        ctx.writeAndFlush(protocol);
    }

    private Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
        System.out.println("服务器开始执行方法");
        String className = rpcRequest.getClassName();
        Object serviceBean = SERVICE_INSTANCE_MAP.getOrDefault(className,null);
        if(serviceBean == null){
            throw new RuntimeException("服务端没有找到服务");
        }
        FastClass proxyClass = FastClass.create(serviceBean.getClass());
        FastMethod method = proxyClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(serviceBean, rpcRequest.getParameters());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
