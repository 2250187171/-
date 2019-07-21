package org.java.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface A_RoleMapper {

    //查询所有角色
    public List<Map> findRoleAll();

    //根据部门查询角色
    public List<Map> findRoleBySectionID(@Param("sectionID")int sectionID);


}
