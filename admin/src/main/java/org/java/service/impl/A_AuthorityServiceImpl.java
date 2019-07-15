package org.java.service.impl;

import org.java.dao.A_AuthorityMapper;
import org.java.service.A_AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class A_AuthorityServiceImpl implements A_AuthorityService {
    @Autowired
    private A_AuthorityMapper mapper;

    //查询所有权限
    @Override
    public List<Map> findAll() {
        return mapper.findAll();
    }

    //根据角色id和模块id查询
    @Override
    public int findByRoleIDAndPermID(int roleID, int permID) {
        return mapper.findByRoleIDAndPermID(roleID, permID);
    }

    //删除角色权限
    @Override
    public void deleteRole_Perm(int roleID) {
        mapper.deleteRole_Perm(roleID);
    }

    //新增角色权限
    @Override
    public void addRole_Perm(int roleID, int permID) {
        mapper.addRole_Perm(roleID, permID);
    }
}
