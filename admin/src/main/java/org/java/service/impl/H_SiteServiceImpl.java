package org.java.service.impl;

import org.java.dao.H_SiteMapper;
import org.java.service.H_SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class H_SiteServiceImpl implements H_SiteService {
    @Autowired
    private H_SiteMapper mapper;
    //新增站点
    @Override
    public int addSite(Map map) {
        return mapper.addSite(map);
    }

    @Override
    public List findIFSite(Map map) {
        return mapper.findIFSite(map);
    }

    @Override
    public int findIFCount(Map map) {
        return mapper.findIFCount(map);
    }

    @Override
    public Map findBySiteID(int siteID) {
        return mapper.findBySiteID(siteID);
    }

    @Override
    public int updateSite(Map map) {
        return mapper.updateSite(map);
    }

    @Override
    public int deleteSite(int siteID) {
        return mapper.deleteSite(siteID);
    }
}
