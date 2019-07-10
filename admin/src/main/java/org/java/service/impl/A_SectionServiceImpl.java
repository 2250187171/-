package org.java.service.impl;

import org.java.dao.A_SectionMapper;
import org.java.service.A_SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class A_SectionServiceImpl implements A_SectionService {
    @Autowired
    private A_SectionMapper mapper;

    //查询所有部门
    @Override
    public List<Map> findSectionAll() {
        return mapper.findSectionAll();
    }
}
