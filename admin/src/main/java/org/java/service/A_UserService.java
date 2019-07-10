package org.java.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface A_UserService
{
    //根据手机号码查询用户
    public Map findByPhoneNumber(String phoneNumber);

    //新增用户
    public void addUser(Map map);

    //根据身份证查询用户
    public int findByIDNumber(String IDNumber);

    //查询所有用户
    public List<Map> findUserAll();

    //根据手机号码查询用户获得用户名和密码
    public String findByPassword(String phoneNumber);

    //多条件分页查询用户
    public List<Map> findUserPaging(@Param("map")Map map);

    //查询用户总数
    public int count();

    //新增关联表
    public void addUser_Role(Map map);
}
