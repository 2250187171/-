package org.java.service.impl;

import org.java.dao.A_UserMapper;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    //新增用户
    @Override
    public void addUser(Map map) {
        a_userMapper.addUser(map);
        addUser_Role(map);
    }

    //根据身份证号码查询用户
    @Override
    public int findByIDNumber(String IDNumber) {
        return a_userMapper.findByIDNumber(IDNumber);
    }

    //查询所有用户
    @Override
    public List<Map> findUserAll() {
        return a_userMapper.findUserAll();
    }


    //查询用户获得密码
    @Override
    public String findByPassword(String phoneNumber) {
        return a_userMapper.findByPassword(phoneNumber);
    }
    //多条件分页查询
    @Override
    public List<Map> findUserPaging(Map map) {
        return a_userMapper.findUserPaging(map);
    }
    //查询总数据
    @Override
    public int count() {
        return a_userMapper.count();
    }

    //新增关联表
    @Override
    public void addUser_Role(Map map) {
        a_userMapper.addUser_Role(map);
    }
}
