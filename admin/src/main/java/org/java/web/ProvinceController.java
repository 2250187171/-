package org.java.web;

import org.java.service.H_ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class ProvinceController {
    @Autowired
    private H_ProvinceService provinceService;

    //获得所有省份
    @RequestMapping("/siteManage/RouteShow")
    public String RouteShow(Model model){
        List<Map> ps = provinceService.findAll();
        model.addAttribute("ps", ps);
        return "/siteManage/circuitMessage";
    }

    //获得所有省份
    @RequestMapping("/siteManage/siteShow")
    public String siteShow(Model model){
        List<Map> ps = provinceService.findAll();
        model.addAttribute("ps", ps);
        return "/siteManage/siteMessage";
    }

    //根据省份id查询城市返回json
    @RequestMapping("/siteManage/findByProID")
    @ResponseBody
    public List<Map> findByProID(int proID){
        return provinceService.findByProId(proID);
    }

    //根据城市id查询区县返回json
    @ResponseBody
    @RequestMapping("/siteManage/findByCityID")
    public List<Map> findByCityID(int cityID){
        return provinceService.findByCityID(cityID);
    }
}
