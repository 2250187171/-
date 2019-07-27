package org.java.service;

import java.util.List;
import java.util.Map;

public interface LeaveService {
    //申请请假
    public int addLeave(Map map);

    //根据流程实例ID查询请假单
    public Map findByProcessInstanceID(String instanceID);

    //获得个人待办理任务
    public Map showLeaveTask(String userID,int start,int length);

    //提交任务
    public void commit(String taskId,Map map);

    //消假
    public void eliminate(String taskId);

    //拾取任务
    public void pickupTask(String userID,String taskID);

    //根据用户名获得候选组任务
    public Map showGroupTask(Map map,int start,int length);


    //根据请假ID查询请假任务
    public Map findByLeaveID(String leaveID);


    //新增审核
    public int addAudit(Map map);

    //根据流程实例ID删除请假
    public int deleteLeave(String procInsId);

    //修改请假任务
    public int updateLeave(Map map);

    //将个人任务归还成组任务
    public void returnLeave(Map map);

    //显示任务进度
    public List LeavePlan(Map map);
    //获得任务阶段
    public List showTaskStage(String instanceId);
    //消假
    public int eliminateLeave(Map map);

    //查询个人历史任务
    public Map userEndTask(Map map);

    //查询所有人的历史任务
    public Map endTaskAll(Map map);
}
