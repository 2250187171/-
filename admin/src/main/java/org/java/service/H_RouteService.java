package org.java.service;

import java.util.List;
import java.util.Map;

public interface H_RouteService {

    //新增线路
    public int addRoute(Map map);

    //根据条件分页查询路线
    public List<Map> findIFRoute(Map map);

    //根据条件查询数据数量
    public int count(Map map);

    //根据路线ID查询路线信息
    public Map findById(int RouteID);

    //修改路线信息
    public int updateRoute(Map map);

    //删除路线
    public int deleteRoute(int RouteID);

    //查询路线ID和路线
    public List findByRouteStart();
}
