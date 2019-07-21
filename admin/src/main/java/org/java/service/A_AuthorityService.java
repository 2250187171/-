package org.java.service;

import java.util.List;
import java.util.Map;

public interface A_AuthorityService {
    //查询所有权限
    public List<Map> findAll();

    //根据角色id和权限id查询
    public int findByRoleIDAndPermID(int roleID,int permID,int sectionID);

    //删除该角色的所有权限
    public void deleteRole_Perm(int roleID,int sectionID);

    //新增角色权限
    public void addRole_Perm(int roleID,int permID,int sectionID);

}
