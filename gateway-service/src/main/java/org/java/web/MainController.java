package org.java.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

/**
 * 登录成功后进入该类
 */
@Controller
public class MainController {
    @Autowired
    private A_UserService service;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/main")
    public String main(HttpSession session, Model model){
        System.out.println("登录成功------------------------------------------------");
        //获得用户主体
        Subject subject = SecurityUtils.getSubject();
        //获得用户凭证
        String phoneNumber = (String) subject.getPrincipal();
        Map user = service.findByPhoneNumber(phoneNumber);
        session.setAttribute("user", user);
        model.addAttribute("activeUrl", "main");
        //设置一个token
        String token= UUID.randomUUID().toString();
        model.addAttribute("token", token);
        //将用户和token存储到redis中用于单点登录
        redisTemplate.opsForHash().put("user", token, user);
        return "/main";

    }
}
