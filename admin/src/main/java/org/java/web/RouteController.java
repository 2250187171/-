package org.java.web;

import org.java.service.H_RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RouteController {

    @Autowired
    private H_RouteService service;

    //新增路线
    @RequestMapping("/siteManage/addRoute")
    @ResponseBody
    public int addRoute(@RequestParam Map map){
        int count=service.addRoute(map);
        return count;
    }

    //多条件分页查询返回json
    @RequestMapping("/siteManage/findIFRoute")
    @ResponseBody
    public Map findIFRoute(@RequestParam Map map){
        map.put("start", Integer.parseInt(map.get("start").toString()));
        map.put("length", Integer.parseInt(map.get("length").toString()));
        List<Map> route = service.findIFRoute(map);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", service.count(map));
        m.put("recordsFiltered", service.count(map));
        m.put("aaData", route);
        return m;
    }

    //根据ID查询返回Json
    @RequestMapping("/siteManage/findById")
    @ResponseBody
    public Map findById(int RouteID){
        return service.findById(RouteID);
    }

    //修改路线信息
    @RequestMapping("/siteManage/updateRoute")
    @ResponseBody
    public int updateRoute(@RequestParam Map map){
        int count = service.updateRoute(map);
        return count;
    }

    //删除路线
    @RequestMapping("/siteManage/deleteRoute")
    @ResponseBody
    public int deleteRoute(int RouteID){
        return service.deleteRoute(RouteID);
    }

    @RequestMapping("/siteManage/ditu")
    public String ditu(){
        return "/siteManage/ditu";
    }

    @RequestMapping("/siteManage/findByRouteStart")
    @ResponseBody
    public List findByRouteStart(){
        return service.findByRouteStart();
    }

}
