package org.java.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface H_SiteMapper {

    //新增站点
    public int addSite(Map map);

    //条件查询站点信息
    public List findIFSite(Map map);


    //条件查询站点数量
    public int findIFCount(Map map);

    //根据站点ID查询站点
    public Map findBySiteID(@Param("siteID") int siteID);

    //修改站点
    public int updateSite(Map map);

    //删除站点啊
    public int deleteSite(@Param("siteID")int siteID);
}
