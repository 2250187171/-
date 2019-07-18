package org.java.service.impl;

import org.java.dao.H_RouteMapper;
import org.java.service.H_RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class H_RouteServiceImpl implements H_RouteService {

    @Autowired
    private H_RouteMapper mapper;
    //新增路线
    @Override
    public int addRoute(Map map) {
       return mapper.addRoute(map);
    }

    //多条件分页查询线路
    @Override
    public List<Map> findIFRoute(Map map) {
        return mapper.findIFRoute(map);
    }

    //条件查询获得数量
    @Override
    public int count(Map map) {
        return mapper.count(map);
    }

    //根据ID查询获得信息
    @Override
    public Map findById(int RouteID) {
        return mapper.findById(RouteID);
    }

    //修改路线信息
    @Override
    public int updateRoute(Map map) {
        return mapper.updateRoute(map);
    }

    //删除路线
    @Override
    public int deleteRoute(int RouteID) {
        return mapper.deleteRoute(RouteID);
    }
}
