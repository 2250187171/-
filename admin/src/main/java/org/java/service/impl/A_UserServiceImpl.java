package org.java.service.impl;

import org.java.dao.A_UserMapper;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class A_UserServiceImpl implements A_UserService {

    @Autowired
    private A_UserMapper a_userMapper;

    //根据手机号码查询用户
    public Map findByPhoneNumber(String phoneNumber){
        System.out.println("service-----------------------------------:"+phoneNumber);
        return a_userMapper.findByPhoneNumber(phoneNumber);
    }
}
