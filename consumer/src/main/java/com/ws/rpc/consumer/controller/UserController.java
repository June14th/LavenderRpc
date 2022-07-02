package com.ws.rpc.consumer.controller;

import com.ws.rpc.api.IUserService;
import com.ws.rpc.consumer.anno.RpcReference;
import com.ws.rpc.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    IUserService userService;

    @RequestMapping("/getUserById")
    public User getUserById(int id){
        System.out.println("用户在网页调用了指定方法");
        return userService.getById(id);
    }
}
