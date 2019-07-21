package org.java.service;

import java.util.List;
import java.util.Map;

public interface LeaveService {
    //申请请假
    public int addLeave(Map map);

    //根据流程实例ID查询请假单
    public Map findByProcessInstanceID(String instanceID);

    //获得个人待办理任务
    public List showLeaveTask(String userID);

    //提交任务
    public void commit(String taskId,Map map);

    //拾取任务
    public void pickupTask(String userID,String taskID);

    //根据用户名获得候选组任务
    public List<Map> showGroupTask(Map map);
}
