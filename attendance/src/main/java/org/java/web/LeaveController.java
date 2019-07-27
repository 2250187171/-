package org.java.web;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.java.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LeaveController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;

    //申请请假
    @RequestMapping("/addLeave")
    @ResponseBody
    public String addLeave(@RequestParam Map map) throws ParseException {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //获得开始时间
        Date d1=format.parse(map.get("startDate").toString());
        //获得结束时间
        Date d2=format.parse(map.get("endDate").toString());
        //获得请假天数
        long l = (d2.getTime() - d1.getTime()) / 24 / 60 / 60 / 1000;
        map.put("leaveDay", l);
        int i = leaveService.addLeave(map);
        if(i==1){
            return "申请成功!";
        }else{
            return "申请失败!";
        }
    }

    //显示个人待办理的请假任务
    @ResponseBody
    @RequestMapping("/leaveManage/showLeaveTask")
    public Map showLeaveTask(@RequestParam Map map,int start,int length){

        //获得redis存储的用户信息
        Map task = leaveService.showLeaveTask(map.get("userID").toString(),start,length);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", task.get("count"));
        m.put("recordsFiltered",task.get("count"));
        m.put("aaData", task.get("list"));
        return m;
    }

    //显示候选组的请假任务
    @ResponseBody
    @RequestMapping("/leaveManage/showGroupTask")
    public Map showGroupTask(@RequestParam Map map,int start,int length){
        //获得候选组任务
        Map task = leaveService.showGroupTask(map,start,length);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", task.get("count"));
        m.put("recordsFiltered",task.get("count"));
        m.put("aaData", task.get("gs"));
        return m;
    }



    //跳转到办理请求页面
    @RequestMapping("/leaveManage/transactionLeave")
    public String transactionLeave(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("user", user);
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

    //跳转到请假进度页面
    @RequestMapping("/leaveManage/LeavePlan")
    public String LeavePlan(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("userID", user.get("userID"));
        return "/LeavePlan";

    }
    //跳转到请假历史请求页面
    @RequestMapping("/leaveManage/leaveRecord")
    public String leaveRecord(@RequestParam("token") String token, Model model){
        //从redis中获的登录的用户信息
        Map user = (Map) redisTemplate.opsForHash().get("user", token);
        model.addAttribute("user", user);
        return "/leaveRecord";

    }

    //员工提交请假
    @RequestMapping("/leaveManage/submitLeave")
    @ResponseBody
    public int submitLeave(@RequestParam Map map){
        leaveService.commit(map.get("taskId").toString(),map);
        return 1;
    }

    //拾取任务
    @RequestMapping("/leaveManage/pickupTask")
    @ResponseBody
    public int pickupTask(String userID, String taskID){
        leaveService.pickupTask(userID, taskID);
        return 1;
    }


    //根据请假ID查询请假任务
    @RequestMapping("/leaveManage/findByLeaveID")
    @ResponseBody
    public Map findByLeaveID(String leaveID){
        return leaveService.findByLeaveID(leaveID);
    }


    //提交审核请假
    @RequestMapping("/leaveManage/auditLeave")
    @ResponseBody
    public int auditLeave(@RequestParam Map map){
        return leaveService.addAudit(map);
    }


    //根据流程实例ID删除请假
    @RequestMapping("/leaveManage/deleteLeave")
    @ResponseBody
    public int deleteLeave(String procInsId){
        return leaveService.deleteLeave(procInsId);
    }

    //修改请假任务
    @RequestMapping("/leaveManage/updateLeave")
    @ResponseBody
    public String updateLeave(@RequestParam Map map) throws ParseException {

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //获得开始时间
        Date d1=format.parse(map.get("startDate").toString());
        //获得结束时间
        Date d2=format.parse(map.get("endDate").toString());
        //获得请假天数
        long l = (d2.getTime() - d1.getTime()) / 24 / 60 / 60 / 1000;
        map.put("leaveDay", l);
        System.out.println(map+"#############");
        int i = leaveService.updateLeave(map);
        if(i==1){
            return "修改成功";
        }else{
            return "修改失败";
        }
    }

    //将个人任务归还成组任务
    @RequestMapping("/leaveManage/returnLeave")
    @ResponseBody
    public int returnLeave(@RequestParam Map map){
        leaveService.returnLeave(map);
        return 1;
    }


    //消假
    @RequestMapping("/leaveManage/eliminate")
    @ResponseBody
    public int eliminate(String taskId,String endDate,String leaveID) throws ParseException {
        System.out.println(taskId+"---"+endDate+"@@@@@@@@@@@@@@@@@@@@@");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String newDate=format.format(new Date());
        Date d1 = format.parse(endDate);
        Date d2=format.parse(newDate);
        Map map=new HashMap();
        map.put("endTime", new Date());
        map.put("leaveID", leaveID);
        leaveService.eliminate(taskId);
        int i=0;
        if(d2.getTime()>d1.getTime()){
            map.put("evaluate","超假");
            i=0;
        }else {
            map.put("evaluate","正常");
            i=1;
        }
        leaveService.eliminateLeave(map);
        return i;


    }


    //获得请假进度
    @RequestMapping("/leaveManage/leavePlan")
    @ResponseBody
    public Map showLeavePlan(@RequestParam Map map){
        System.out.println(map);
        List ls = leaveService.LeavePlan(map);
        Map m=new HashMap();
        m.put("draw",map.get("draw"));
        m.put("recordsTotal", ls.size());
        m.put("recordsFiltered",ls.size());
        m.put("aaData", ls);
        return m;
    }


    @RequestMapping("leaveManage/showSatgePng/{instanceId}/{userID}")
    public String showPng(@PathVariable("instanceId") String instanceId,@PathVariable("userID")String userID,Model model){
        //根据流程实例Id查询流程实例
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        //根据流程部署Id获得该流程定义
        String processDefinitionId = instance.getProcessDefinitionId();
        ProcessDefinitionEntity processDefinition =(ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //通过实例获得当前任务节点的Id
        String activityId=instance.getActivityId();
        //通过任务节点id从定义中获得任务节点
        ActivityImpl activity = processDefinition.findActivity(activityId);
        //分别获得任务节点的参数x,y,width,height
        model.addAttribute("x", activity.getX());
        model.addAttribute("y", activity.getY());
        model.addAttribute("width", activity.getWidth());
        model.addAttribute("height", activity.getHeight());
        //得到流程部署Id和png的名称
        String definitionId=processDefinition.getDeploymentId();
        String name=processDefinition.getDiagramResourceName();
        model.addAttribute("definitionId", definitionId);
        model.addAttribute("name", name);
        model.addAttribute("userID", userID);
        return "showActiveMap";
    }

    //显示bpmn和Png
    @RequestMapping("/leaveManage/showResource/{name}/{id}")
    public void showResource(@PathVariable("name")String name, @PathVariable("id")String id, HttpServletResponse res) throws IOException {
        System.out.println(id);
        InputStream in = repositoryService.getResourceAsStream(id, name);
        OutputStream out=res.getOutputStream();
        byte[] b=new byte[1024];
        int len=0;
        while ((len=in.read(b))!=-1) {
            out.write(b, 0, len);
        }
        out.close();
        in.close();
    }

    //获得历史任务阶段
    @RequestMapping("/leaveManage/showLeaveStage/{instanceId}/{userID}/{type}")
    public String showLeaveStage(@PathVariable("instanceId") String instanceId,@PathVariable("userID")String userID,@PathVariable("type")String type, Model model){
        List list=leaveService.showTaskStage(instanceId);
        model.addAttribute("list", list);
        model.addAttribute("userID", userID);
        model.addAttribute("type", type);
        return "/leaveStage";
    }

    //根据用户ID查询用户历史任务
    @RequestMapping("/leaveManage/userEndLeave")
    @ResponseBody
    public Map userEndLeave(@RequestParam Map map){
        map.put("start",Integer.parseInt(map.get("start").toString()));
        map.put("length", Integer.parseInt(map.get("length").toString()));
        Map leave = leaveService.userEndTask(map);
        Map m=new HashMap();
        m.put("draw",0);
        m.put("recordsTotal", leave.get("count"));
        m.put("recordsFiltered",leave.get("count"));
        m.put("aaData", leave.get("list"));
        return m;
    }


    //根据用户ID查询用户历史任务
    @RequestMapping("/leaveManage/endLeaveAll")
    @ResponseBody
    public Map endLeaveAll(@RequestParam Map map){
        map.put("start",Integer.parseInt(map.get("start").toString()));
        map.put("length", Integer.parseInt(map.get("length").toString()));
        Map leave = leaveService.endTaskAll(map);
        Map m=new HashMap();
        m.put("draw",0);
        m.put("recordsTotal", leave.get("count"));
        m.put("recordsFiltered",leave.get("count"));
        m.put("aaData", leave.get("list"));
        return m;
    }
}
