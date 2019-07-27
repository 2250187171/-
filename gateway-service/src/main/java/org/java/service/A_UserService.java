package org.java.service;

import java.util.List;
import java.util.Map;

public interface A_UserService
{
    //根据手机号码查询用户
    public Map findByPhoneNumber(String phoneNumber);


    //根据手机号码查询用户获得密码
    public String findByPassword(String phoneNumber);

    //根据用户查询权限
    public List findPer(String phoneNumber);

}
