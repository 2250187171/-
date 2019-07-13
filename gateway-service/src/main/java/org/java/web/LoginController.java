package org.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录的控制器类
 * 当登录失败或者未登录进入该类
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(HttpServletRequest request) throws Exception {
        //获得错误信息
        String err = (String) request.getAttribute("shiroLoginFailure");

        //判断err是否为空
        if(err==null){
            System.out.println("没有登录用户");
        }else {
            if(err.equals("org.apache.shiro.authc.UnknownAccountException")){
                throw new Exception("用户名不存在");
            }else if(err.equals("org.apache.shiro.authc.IncorrectCredentialsException")){
                throw new Exception("密码错误");
            }
        }
        return "/login";
    }
}
