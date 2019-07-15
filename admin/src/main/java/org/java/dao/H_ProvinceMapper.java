package org.java.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface H_ProvinceMapper {
    //查询所有省份
    @Select("select*from h_province")
    public List<Map> findAll();

    //根据省份id查询所有城市
    @Select("select*from h_city where proID=#{proID}")
    public List<Map> findByProId(@Param("proID")int proID);

    //根据城市ID查询区县
    @Select("select*from h_district where cityID=#{cityID}")
    public List<Map> findByCityID(@Param("cityID")int cityID);
}
