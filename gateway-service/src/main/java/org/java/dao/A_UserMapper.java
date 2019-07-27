package org.java.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户的持久层
 */
@Component
public interface A_UserMapper {

    //根据手机号码查询用户
    public Map findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    //根据手机号码查询用户获得密码
    public String findByPassword(@Param("phoneNumber") String phoneNumber);

    //根据用户查询权限
    public List findPer(@Param("phoneNumber")String phoneNumber);
}
