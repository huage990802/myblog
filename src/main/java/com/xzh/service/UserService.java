package com.xzh.service;

import com.xzh.po.User;

public interface UserService {
    User checkUser(String username,String password);
}
