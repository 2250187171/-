package org.java.service.impl;

import org.java.dao.A_RoleMapper;
import org.java.service.A_RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class A_RoleServiceImpl implements A_RoleService {

    @Autowired
    private A_RoleMapper mapper;
    //查询所有角色
    @Override
    public List<Map> findRoleAll() {
        return mapper.findRoleAll();
    }


    //根据部门查询角色
    @Override
    public List<Map> findRoleBySectionID(int sectionID) {
        return mapper.findRoleBySectionID(sectionID);
    }
}
