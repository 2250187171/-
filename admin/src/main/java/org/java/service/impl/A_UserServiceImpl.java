package org.java.service.impl;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.java.dao.A_UserMapper;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class A_UserServiceImpl implements A_UserService {

    @Autowired
    private A_UserMapper a_userMapper;

    @Autowired
    private IdentityService identityService;

    //根据手机号码查询用户
    public Map findByPhoneNumber(String phoneNumber){
        System.out.println("service-----------------------------------:"+phoneNumber);
        return a_userMapper.findByPhoneNumber(phoneNumber);
    }

    //新增用户
    //添加activiti的用户表和候选组表的关联
    @Override
    public void addUser(Map map) {
        //生成用户id作为唯一标识
        String userID = UUID.randomUUID().toString();
        map.put("userID", userID);
        a_userMapper.addUser(map);
        addUser_Role(map);
        //判断activiti中的用户表是否存在该用户
        if(identityService.createUserQuery().userId(userID).singleResult()==null){
            //不存在创建一个新的用户
            UserEntity userEntity=new UserEntity();
            userEntity.setId(userID);
            userEntity.setFirstName(map.get("username").toString());
            //往用户表添加用户
            identityService.saveUser(userEntity);
            //获得候选组ID
            String groupID=map.get("roleID").toString();
            //判断候选组是否存在，不存在就创建
            if(identityService.createGroupQuery().groupId(groupID).singleResult()==null){
                //创建候选组
                GroupEntity groupEntity=new GroupEntity();
                groupEntity.setId(groupID);
                groupEntity.setName(map.get("roleName").toString());
                //添加候选组
                identityService.saveGroup(groupEntity);
            }
            //配置用户和候选组的关联
            //如果存在就删除
            identityService.deleteMembership(userID, groupID);
            //创建关联
            identityService.createMembership(userID, groupID);
        }

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
    public int count(Map map) {
        return a_userMapper.count(map);
    }

    //新增关联表
    @Override
    public void addUser_Role(Map map) {
        a_userMapper.addUser_Role(map);
    }


    //修改用户信息
    @Override
    public void updateUser(Map map) {
        a_userMapper.updateUser(map);
        update_user_role(map);
        //获得用户ID
        String userID=map.get("userid").toString();
        //判断activiti中的用户表是否存在该用户
        if(identityService.createUserQuery().userId(userID).singleResult()==null){
            //不存在创建一个新的用户
            UserEntity userEntity=new UserEntity();
            userEntity.setId(userID);
            userEntity.setFirstName(map.get("username").toString());
            //往用户表添加用户
            identityService.saveUser(userEntity);
            //获得候选组ID
            String groupID=map.get("roleID").toString();
            //判断候选组是否存在，不存在就创建
            if(identityService.createGroupQuery().groupId(groupID).singleResult()==null){
                //创建候选组
                GroupEntity groupEntity=new GroupEntity();
                groupEntity.setId(groupID);
                groupEntity.setName(map.get("roleName").toString());
                //添加候选组
                identityService.saveGroup(groupEntity);
            }
            //配置用户和候选组的关联
            //如果存在就删除
            identityService.deleteMembership(userID, groupID);
            //创建关联
            identityService.createMembership(userID, groupID);
        }
        System.out.println(map+"aaaaaaaaaaaaaaaaaaaaa");
    }

    //查询不是该手机号的数量
    @Override
    public int findNotPhoneNumber(String phoneNumber,String userID) {
        return a_userMapper.findNotPhoneNumber(phoneNumber,userID);
    }

    //查询不是该身份证的数量
    @Override
    public int findNotIDNumber(String IDNumber,String userID) {
        return a_userMapper.findNotIDNumber(IDNumber,userID);
    }

    //修改角色权限
    @Override
    public void update_user_role(Map map) {
        a_userMapper.update_user_role(map);
    }

    //根据角色查询用户
    @Override
    public List<Map> findByRoleID(int roleID,int sectionID) {
        return a_userMapper.findByRoleID(roleID,sectionID);
    }

    @Override
    public List<String> findUserID(Map map) {
        return a_userMapper.findUserID(map);
    }

    @Override
    public String findByUserID(String userID) {
        return a_userMapper.findByUserID(userID);
    }
}
