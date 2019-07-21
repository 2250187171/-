package org.java.web;

import org.java.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class AttendController {
    @Autowired
    private AttendService service;

    @Autowired
    private RedisTemplate redisTemplate;

    //进入打卡页面
    @RequestMapping("/clock")
    public String clock(Model model, @RequestParam("token")String token, HttpSession session){
        //通过token查询redis登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        session.setAttribute("user", user);
        String bol=null;
        String userID=user.get("userID").toString();
        //获得用户的今日打卡信息
        Map attend = service.findByUserID(userID);
        if(null==attend){
            bol="未签到";
        }else
        {
            bol=attend.get("Mark").toString();
            model.addAttribute("attendanceID", attend.get("attendanceID"));
        }

        //获得所有打卡记录
        List<Map> list=service.findByUserIDAll(userID);
        //用于存储打卡时间
        List<String> sty=new ArrayList<>();
        //遍历打卡记录
        for (Map t :list) {
            //获得上班时间
            String startTime = t.get("startWorkTime").toString();
            //替换格式
            String date = startTime.replace("-", "");
            //分割上班时间精确到年月日
            date = date.substring(0, 8);
            //将时间保存到集合中
            sty.add(date);
        }
        model.addAttribute("sty", sty);
        model.addAttribute("bol", bol);
        model.addAttribute("user", user);
        return "/clock";
    }


    //用户上班打卡
    @RequestMapping("/addClock/{Mark}")
    @ResponseBody
    public int addClock(@RequestParam Map map, @PathVariable("Mark")String Mark){
        System.out.println(map);
        //获得当前时间
        Date date=new Date();
        int msg=0;
        if("未签到".equals(Mark)){//表示上班打卡
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,8); //设置为每天早上8点钟
            cal.set(Calendar.MINUTE,0); //零分
            cal.set(Calendar.SECOND,59);//59秒
            map.put("startWorkTime", date);
            map.put("Mark", "上班签到");
            //判断早上签到时间是否大于8点0分59秒
            if(date.getTime()>cal.getTimeInMillis()){
                map.put("sstate", "旷工");
                //在设置签到时间的基础上加上2个小时
                long sendTime =  cal.getTimeInMillis()+(2*60*60*1000);
                //超过2小时表示矿工
                if(date.getTime()>sendTime){
                    map.put("sstate", "旷工");
                    msg=1;
                }else {//否则为迟到
                    map.put("sstate", "迟到");
                    msg=2;
                }
            }else {//正常打卡
                map.put("sstate", "正常");
                msg=0;
            }
            service.addClock(map);
            return msg;
        }else //下班打卡
        {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY,17); //设置为每天早上8点钟
            cal.set(Calendar.MINUTE,0); //零分
            cal.set(Calendar.SECOND,0);
            map.put("offWork", date);
            map.put("Mark", "下班签到");
            //判断下班签到时间是否小于17点0分0秒
            if(date.getTime()<cal.getTimeInMillis()){
                //在设置签到时间的基础上加上2个小时
                long sendTime =  cal.getTimeInMillis()-(2*60*60*1000);
                //超过2小时表示矿工
                if(date.getTime()<sendTime){
                    map.put("xstate", "旷工");
                    msg=3;
                }else {//否则为迟到
                    map.put("xstate", "早退");
                    msg=4;
                }
            }else {//正常打卡
                map.put("xstate", "正常");
                msg=0;
            }
            service.updateClock(map);
            return msg;
        }
    }

    //判断是否早退
    @RequestMapping("/pl")
    @ResponseBody
    public int pl(){

        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,17); //设置为每天早上8点钟
        cal.set(Calendar.MINUTE,0); //零分
        cal.set(Calendar.SECOND,0);
        if(date.getTime()<cal.getTimeInMillis()){
            return 1;
        }else {
            return 0;
        }
    }

    //查询用户的打卡记录返回json
    @ResponseBody
    @RequestMapping("/getList")
    public Map getList(@RequestParam Map map){
        map.put("start", Integer.parseInt(map.get("start").toString()));
        map.put("length", Integer.parseInt(map.get("length").toString()));
        List<Map> list = service.findByUserIDPage(map);
        Map m=new HashMap();
        int count=service.count(map.get("userID").toString());
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", count);
        m.put("recordsFiltered",count);
        m.put("aaData", list);
        System.out.println(m);
        return m;
    }

    //显示打卡详细信息
    @RequestMapping("/showClock")
    @ResponseBody
    public Map showClock(int attendanceID){
        Map clock = service.showClock(attendanceID);
        return clock;
    }

    //跳转到打卡信息页面
    @RequestMapping("/clockMessage")
    public String clockMessage(String token,Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("userID", user.get("userID"));
        return "/clockMessage";
    }
}
