package org.java.service;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface H_ProvinceService {

    //查询所有省份
    public List<Map> findAll();

    //根据省份id查询所有城市
    public List<Map> findByProId(int proID);

    //根据城市ID查询区县
    public List<Map> findByCityID(int cityID);
}
