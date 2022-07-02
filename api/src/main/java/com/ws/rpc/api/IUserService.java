package com.ws.rpc.api;

import com.ws.rpc.pojo.User;

/**
  *  用户服务
  */
public interface IUserService {
    User getById(int id);
}
