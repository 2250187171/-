package org.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/userManage/userMessage")
    public String userMessage(Model model){
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        model.addAttribute("activeUrl", "userMessage");
        return "/userManage/userMessage";
    }

    @RequestMapping("/tomain")
    public String main(Model model){
        model.addAttribute("activeUrl", "main");
        return "/main";
    }
}
