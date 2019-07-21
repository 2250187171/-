package org.java.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface A_AuthorityMapper {

    //查询所有权限
    public List<Map> findAll();

    //根据部门ID,角色id和权限id查询
    public int findByRoleIDAndPermID(@Param("roleID")int roleID,@Param("permID")int permID,@Param("sectionID")int sectionID);


    //删除该部门角色的所有权限
    public void deleteRole_Perm(@Param("roleID")int roleID,@Param("sectionID")int sectionID);

    //新增角色权限
    public void addRole_Perm(@Param("roleID")int roleID,@Param("permID")int permID,@Param("sectionID")int sectionID);
}
