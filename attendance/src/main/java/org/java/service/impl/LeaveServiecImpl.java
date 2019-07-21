package org.java.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.java.dao.LeaveMapper;
import org.java.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class LeaveServiecImpl implements LeaveService {
    @Autowired
    private LeaveMapper mapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    //申请请假
    @Override
    public int addLeave(Map map) {
        //创建请假表的主键
        String leaveID = UUID.randomUUID().toString();
        //指定流程定义的键
        String key= "leave";
        //设置流程实例的创建人
        String userID=map.get("userID").toString();
        identityService.setAuthenticatedUserId(userID);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("userIDs", "1f712064-a31c-11e9-8168-00163e04ed9f");
        variables.put("userID",userID);
        //启动流程实例的同时添加业务主键
        ProcessInstance instance=runtimeService.startProcessInstanceByKey(key,leaveID,variables);
        //在业务表中添加leaveID,createTime,processinstance_ID
        map.put("leaveID",leaveID );
        map.put("createTime",new Date());
        map.put("processinstance_ID", instance.getProcessInstanceId());
        return mapper.addLeave(map);
    }

    //根据流程实例ID查询请假单
    @Override
    public Map findByProcessInstanceID(String instanceID) {
        return mapper.findByProcessInstanceID(instanceID);
    }

    //获得个人待办理任务
    public List showLeaveTask(String userID){
        //根据负责人查询任务
        List<Task> ts = taskService.createTaskQuery().taskAssignee(userID).list();
        //用于存储个人任务
        List<Map> list=new ArrayList<>();
        for (Task task:ts) {
            // 获得所有的流程实例id
            String processInstanceId = task.getProcessInstanceId();
            // 根据流程实例id查询业务数据
            Map<String, Object> m = mapper.findByProcessInstanceID(processInstanceId);
            // 把该业务数据，对应的工作流的信息，也封装到map中
            m.put("taskId", task.getId());// 任务id
            m.put("taskName", task.getName());// 任务名称
            m.put("assignee", task.getAssignee());// 任务办理人
            m.put("createtime", task.getCreateTime());// 任务的开始时间
            m.put("taskDefKey", task.getTaskDefinitionKey());// 每一个任务对应的id的名称
            list.add(m);
        }
        return list;
    }

    //提交任务
    @Override
    public void commit(String taskId,Map map) {
        taskService.complete(taskId,map);
    }


    //拾取任务
    @Override
    public void pickupTask(String userID, String taskID) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskId(taskID).taskCandidateUser(userID);
        if(taskQuery.singleResult()!=null){
            taskService.claim(taskID, userID);
        }

    }


    //根据用户名获得候选组任务
    @Override
    public List<Map> showGroupTask(Map map) {
        //获得用户名
        String userID=map.get("userID").toString();
        //根据用户查询候选组任务
        List<Task> ts=taskService.createTaskQuery().taskCandidateUser(userID).processDefinitionKey("leave").list();
        //创建集合用于存储候选组任务
        List<Map> gs=new ArrayList<>();
        for (Task t:ts) {
            //取得流程实例ID
            String processInstanceId = t.getProcessInstanceId();
            //根据流程实例ID来查询业务数据
            Map m = mapper.findByProcessInstanceID(processInstanceId);
            m.put("taskId", t.getId());
            m.put("taskName", t.getName());
            m.put("createTime", t.getCreateTime());// 创建时间
            gs.add(m);
        }
        return gs;
    }


}
