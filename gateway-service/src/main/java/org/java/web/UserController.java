package org.java.web;

import org.java.client.AdminServiceClient;
import org.java.service.A_UserService;
import org.java.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private A_UserService service;
    @Autowired
    private AdminServiceClient client;

    @Autowired
    private MD5 md5;

    //跳转进入主页
    @RequestMapping("/tomain")
    public String main(Model model){
        return "/main";
    }


    //根据手机号码查询用户
    @RequestMapping("/userManage/findByPhoneNumber/{phoneNumber}")
    @ResponseBody
    public Map findByPhoneNumber(@PathVariable("phoneNumber")String phoneNumber){
        return service.findByPhoneNumber(phoneNumber);
    }

//    //调用用户管理服务
//    @RequestMapping("/userManage/userMessage")
//    public String showUserModule(Model model){
//        return "redirect:http://localhost:8100/userManage/userMessage";
//    }
}
