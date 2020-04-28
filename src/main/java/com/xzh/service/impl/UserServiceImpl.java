package com.xzh.service.impl;

import com.xzh.dao.UserRepository;
import com.xzh.po.User;
import com.xzh.service.UserService;
import com.xzh.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    @Transactional
    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findAllByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
