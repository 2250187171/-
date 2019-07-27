package org.java.service;

import java.util.List;
import java.util.Map;

public interface H_SiteService {

    //新增站点
    public int addSite(Map map);

    //条件查询站点信息
    public List findIFSite(Map map);


    //条件查询站点数量
    public int findIFCount(Map map);

    //根据站点ID查询站点
    public Map findBySiteID(int siteID);


    //修改站点
    public int updateSite(Map map);


    //删除站点啊
    public int deleteSite(int siteID);
}
