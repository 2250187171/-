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
}
