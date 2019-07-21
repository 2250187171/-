package org.java.service;

import java.util.List;
import java.util.Map;

public interface A_RoleService {

    //查询所有角色
    public List<Map> findRoleAll();

    //根据部门查询角色
    public List<Map> findRoleBySectionID(int sectionID);
}
