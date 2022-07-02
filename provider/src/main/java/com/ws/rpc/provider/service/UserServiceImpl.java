package com.ws.rpc.provider.service;

import com.ws.rpc.api.IUserService;
import com.ws.rpc.pojo.User;
import com.ws.rpc.provider.anno.RpcService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RpcService
@Service
public class UserServiceImpl implements IUserService {
    Map<Object, User> userMap = new HashMap<>();

    @Override
    public User getById(int id) {
        if(userMap.size()==0){
            User u1 = new User(1,"张三");
            User u2 = new User(2,"李四");
            userMap.put(u1.getId(),u1);
            userMap.put(u2.getId(),u2);
        }
        return userMap.get(id);
    }
}
