package org.java.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录成功后进入该类
 */
@Controller
public class MainController {
    @Autowired
    private A_UserService service;
    @RequestMapping("/main")
    public String main(HttpSession session){
        System.out.println("登录成功------------------------------------------------");
        //获得用户主体
        Subject subject = SecurityUtils.getSubject();
        //获得用户凭证
        String phoneNumber = (String) subject.getPrincipal();
        Map user = service.findByPhoneNumber(phoneNumber);
        session.setAttribute("user", user);
        return "/main";

    }
}
