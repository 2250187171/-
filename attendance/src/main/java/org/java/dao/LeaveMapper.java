package org.java.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeaveMapper {

    //申请请假
    public int addLeave(Map map);

    //根据流程实例ID查询请假单
    public Map findByProcessInstanceID(@Param("instanceID")String instanceID);

    //根据请假ID查询请假任务
    public Map findByLeaveID(@Param("leaveID")String leaveID);

    //新增审核
    public int addAudit(Map map);

    //根据流程实例ID删除请假
    public int deleteLeave(@Param("procInsId")String procInsId);

    //修改请假任务
    public int updateLeave(Map map);


    //根据流程实例ID和用户ID查询任务
    public Map findByProcessInstanceIDandUserID(Map map);


    //消假
    public int eliminateLeave(Map map);


    //查询所有历史请假任务
    public List endLeaveAll(Map map);

    //查询所有历史任务的数量
    public int endLeaveCount(Map map);

    //根据用户查询历史任务
    public List userEndLeave(Map map);

    //根据用户查询历史任务数量
    public int userEndLeaveCount(Map map);
}