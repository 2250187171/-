package org.java.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 登录成功后进入该类
 */
@Controller
public class MainController {

    @RequestMapping
    public String main(HttpSession session){

        //获得用户主体
        Subject subject = SecurityUtils.getSubject();
        //获得用户凭证
        Map user = (Map) subject.getPrincipal();
        session.setAttribute("user", user);
        return "/main";
    }
}
