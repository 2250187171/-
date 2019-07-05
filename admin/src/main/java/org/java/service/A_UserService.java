package org.java.service;

import java.util.Map;

public interface A_UserService
{
    //根据手机号码查询用户
    public Map findByPhoneNumber(String phoneNumber);
}
