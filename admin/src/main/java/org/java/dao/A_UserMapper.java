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
    public Map findByPhoneNumber(@Param("phoneNumber")String phoneNumber);

    //根据手机号码查询用户获得用户名和密码
    public String findByPassword(@Param("phoneNumber")String phoneNumber);

    //根据身份证查询用户
    public int findByIDNumber(@Param("IDNumber")String IDNumber);

    //新增用户
    public void addUser(Map map);

    //新增关联表
    public void addUser_Role(Map map);

    //查询所有用户
    public List<Map> findUserAll();

    //多条件分页查询用户
    public List<Map> findUserPaging(Map map);

    //查询用户总数
    public int count(Map map);

    //修改用户信息
    public void updateUser(Map map);

    //查询不是该手机号的数量
    public int findNotPhoneNumber(@Param("phoneNumber") String phoneNumber,@Param("userID")String userID);

    //查询不是该身份证的数量
    public int findNotIDNumber(@Param("IDNumber")String IDNumber,@Param("userID")String userID);

    //修改关联表
    public void update_user_role(Map map);

    //查询角色为运输部的部门经理的所有用户
    public List<Map> findByRoleID(@Param("roleID")int roleID,@Param("sectionID")int sectionID);
}
