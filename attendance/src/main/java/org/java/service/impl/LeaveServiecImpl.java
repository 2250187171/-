package org.java.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.java.client.AdminServiceClient;
import org.java.dao.LeaveMapper;
import org.java.service.LeaveService;
import org.java.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private AdminServiceClient adminServiceClient;

    //申请请假
    @Transactional
    @Override
    public int addLeave(Map map) {
        //创建请假表的主键
        String leaveID = UUID.randomUUID().toString();
        //指定流程定义的键
        String key= "leave";
        //设置流程实例的创建人
        String userID=map.get("userID").toString();
        map.put("roleID", 2);
        //调用admin的服务查询候选组userID
        String groupUserID = adminServiceClient.findUserID(map);
        List<String> list = JsonUtil.stringToList(groupUserID);
        identityService.setAuthenticatedUserId(userID);
        //动态指定候选人
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("branchs", list);
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
    public Map showLeaveTask(String userID,int start,int length){
        Map map=new HashMap();
        //任务总数
        long count = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userID).count();
        //根据负责人查询任务
        List<Task> ts = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userID).listPage(start, length);
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
//            m.put("createtime", task.getCreateTime());// 任务的开始时间
                m.put("taskDefKey", task.getTaskDefinitionKey());// 每一个任务对应的id的名称
                list.add(m);
        }
        map.put("count", count);
        map.put("list", list);
        return map;
    }

    //提交任务
    @Transactional
    @Override
    public void commit(String taskId,Map map) {
        taskService.complete(taskId,map);
    }

    //消假
    @Override
    public void eliminate(String taskId) {
        taskService.complete(taskId);
    }


    //拾取任务
    @Transactional
    @Override
    public void pickupTask(String userID, String taskID) {
        TaskQuery taskQuery = taskService.createTaskQuery().taskId(taskID).taskCandidateUser(userID);
        if(taskQuery.singleResult()!=null){
            taskService.claim(taskID, userID);
        }
    }


    //根据用户名获得候选组任务
    @Override
    public Map showGroupTask(Map map,int start,int length) {
        Map m1=new HashMap();
        //获得用户名
        String userID=map.get("userID").toString();
        //数据总数量
        long count = taskService.createTaskQuery().taskCandidateUser(userID).processDefinitionKey("leave").count();
        //根据用户查询候选组任务
        List<Task> ts=taskService.createTaskQuery().taskCandidateUser(userID).processDefinitionKey("leave").listPage(start, length);
        //创建集合用于存储候选组任务
        List<Map> gs=new ArrayList<>();
        for (Task t:ts) {
            //取得流程实例ID
            String processInstanceId = t.getProcessInstanceId();
            //根据流程实例ID来查询业务数据
            Map m = mapper.findByProcessInstanceID(processInstanceId);
            m.put("taskId", t.getId());
            m.put("taskName", t.getName());
//            m.put("createTime", t.getCreateTime());// 创建时间
            gs.add(m);
        }
        m1.put("count", count);
        m1.put("gs", gs);
        return m1;
    }


    //根据请假ID查询请假任务
    @Override
    public Map findByLeaveID(String leaveID) {
        return mapper.findByLeaveID(leaveID);
    }

    //审核请假
    @Transactional
    @Override
    public int addAudit(Map map) {
        //生成审核表的主键
        String id = UUID.randomUUID().toString();
        //向审核表添加数据
        map.put("id", id);
        map.put("createTime",new Date());
        //审核条件
        map.put(map.get("audit_type"),map.get("status"));
        //提交审核请假
        taskService.complete((String) map.get("taskId"), map);
        return mapper.addAudit(map);
    }


    //根据流程实例ID删除请假
    @Transactional
    @Override
    public int deleteLeave(String procInsId) {
        runtimeService.deleteProcessInstance(procInsId, "失败原因");
        return mapper.deleteLeave(procInsId);
    }

    //修改请假任务
    @Transactional
    @Override
    public int updateLeave(Map map) {
        return mapper.updateLeave(map);
    }


    //将个人任务归还成组任务
    @Override
    public void returnLeave(Map map) {
        System.out.println(map);
        //查询userID是否是taskId的负责人
        Task task = taskService.createTaskQuery().taskId(map.get("taskId").toString()).taskAssignee(map.get("userID").toString()).singleResult();
        //如果是该任务的负责人可以归还任务
        if(task!=null){
            //归还任务，该任务没有负责人
            taskService.setAssignee(map.get("taskId").toString(),null);
        }
    }


    //获得任务进度
    @Override
    public List LeavePlan(Map map) {
        //获得所有流程实例
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("leave").active().list();
        //用于存储查询出来的进度
        List ls=new ArrayList();
        for (ProcessInstance p:list) {
            //获得流程实例ID
            String processInstanceId = p.getProcessInstanceId();
            map.put("instanceID", processInstanceId);
            //根据流程实例ID和用户ID查询业务数据
            Map leave = mapper.findByProcessInstanceIDandUserID(map);
            if(leave!=null){
                // 把该业务数据，对应的工作流的信息，也封装到map中
                leave.put("defId", p.getProcessDefinitionId());
                leave.put("instanceId", p.getProcessInstanceId());
                leave.put("activityId", p.getActivityId());
                ls.add(leave);
            }
        }
        return ls;
    }

    //查询历史任务阶段
    @Override
    public List showTaskStage(String instanceId) {
        List ls=new ArrayList();
        //获得历史任务阶段
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).list();
        for (HistoricTaskInstance h:list) {
            Map m=new HashMap();
            String userID=h.getAssignee();
            if(userID==null||userID==""){
                System.out.println("进入没有用户ID"+userID);
                m.put("userName", "");
            }else{
                System.out.println("进入有用户ID"+userID);
                String userName = adminServiceClient.findByUserID(userID);
                m.put("userName", userName);

            }

            m.put("name", h.getName());
            m.put("startTime", h.getStartTime());
            m.put("endTime", h.getEndTime());
            ls.add(m);
        }
        return ls;

    }

    //消假
    @Override
    public int eliminateLeave(Map map) {
        return mapper.eliminateLeave(map);
    }

    //查询用户历史任务
    @Override
    public Map userEndTask(Map map) {
        //根据用户查询历史任务
        List list = mapper.userEndLeave(map);
        //获得数量
        int i = mapper.userEndLeaveCount(map);
        Map m=new HashMap();
        m.put("count", i);
        m.put("list", list);
        return m;
    }

    @Override
    public Map endTaskAll(Map map) {
        List list = mapper.endLeaveAll(map);
        int i = mapper.endLeaveCount(map);
        Map m=new HashMap();
        m.put("count", i);
        m.put("list", list);
        return m;
    }
}
