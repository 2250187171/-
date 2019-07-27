package org.java.web;

import org.java.service.H_SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SiteControlle {

    @Autowired
    private H_SiteService siteService;


    //新增站点
    @RequestMapping("/siteManage/addSite")
    @ResponseBody
    public int addSite(@RequestParam Map map){
        return siteService.addSite(map);
    }

    //查询站点信息
    @RequestMapping("/siteManage/findSite")
    @ResponseBody
    public Map findSite(@RequestParam Map map){
        map.put("start", Integer.parseInt(map.get("start").toString()));
        map.put("length", Integer.parseInt(map.get("length").toString()));
        List list = siteService.findIFSite(map);
        int count=siteService.findIFCount(map);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", count);
        m.put("recordsFiltered", count);
        m.put("aaData", list);
        System.out.println(m);
        return m;
    }

    //根据站点ID查询站点信息
    @RequestMapping("/siteManage/findBySiteID")
    @ResponseBody
    public Map findBySiteID(int siteID){
        return siteService.findBySiteID(siteID);
    }

    @RequestMapping("/siteManage/updateSite")
    @ResponseBody
    public int updateSite(@RequestParam Map map){
        return siteService.updateSite(map);
    }

    @RequestMapping("/siteManage/deleteSite")
    @ResponseBody
    public int deleteSite(int siteID){
        return siteService.deleteSite(siteID);
    }
}
