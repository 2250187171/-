package org.java.web;

import org.java.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LeaveController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LeaveService leaveService;

    //申请请假
    @RequestMapping("/addLeave")
    @ResponseBody
    public int addLeave(@RequestParam Map map){
        int i = leaveService.addLeave(map);
        return i;
    }

    //显示个人待办理的请假任务
    @ResponseBody
    @RequestMapping("/leaveManage/showLeaveTask")
    public Map showLeaveTask(@RequestParam Map map){

        //获得redis存储的用户信息
        List tasks = leaveService.showLeaveTask(map.get("userID").toString());
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", 1);
        m.put("recordsFiltered",1);
        m.put("aaData", tasks);
        return m;
    }

    //显示候选组的请假任务
    @ResponseBody
    @RequestMapping("/leaveManage/showGroupTask")
    public Map showGroupTask(@RequestParam Map map){
        //获得候选组任务
        List tasks = leaveService.showGroupTask(map);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", 1);
        m.put("recordsFiltered",1);
        m.put("aaData", tasks);
        return m;
    }



    //跳转到办理请求页面
    @RequestMapping("/leaveManage/transactionLeave")
    public String transactionLeave(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("userID", user.get("userID"));
        return "/transactionLeave";

    }

    //跳转到待拾请求页面
    @RequestMapping("/leaveManage/backlogLeave")
    public String backlogLeave(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("userID", user.get("userID"));
        return "/backlogLeave";

    }

    //跳转到请假历史请求页面
    @RequestMapping("/leaveManage/leaveRecord")
    public String leaveRecord(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("userID", user.get("userID"));
        return "/leaveRecord";

    }

    //员工提交任务
    @RequestMapping("/leaveManage/submitLeave")
    @ResponseBody
    public int submitLeave(@RequestParam Map map){
        leaveService.commit(map.get("taskId").toString(),map);
        return 1;
    }



}
