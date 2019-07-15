package org.java.service.impl;

import org.java.dao.H_ProvinceMapper;
import org.java.service.H_ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class H_ProvinceServiceImpl implements H_ProvinceService {
    @Autowired
    private H_ProvinceMapper mapper;

    //查询所有省份
    @Override
    public List<Map> findAll() {
        return mapper.findAll();
    }

    //根据省份查询城市
    @Override
    public List<Map> findByProId(int proID) {
        return mapper.findByProId(proID);
    }

    //根据城市查询区县
    @Override
    public List<Map> findByCityID(int cityID) {
        return mapper.findByCityID(cityID);
    }
}
