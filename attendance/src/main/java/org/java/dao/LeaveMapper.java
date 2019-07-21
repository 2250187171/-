package org.java.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface LeaveMapper {

    //申请请假
    public int addLeave(Map map);

    //根据流程实例ID查询请假单
    public Map findByProcessInstanceID(@Param("instanceID")String instanceID);
}
