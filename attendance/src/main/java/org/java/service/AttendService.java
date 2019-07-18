package org.java.service;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AttendService {

    //根据用户编号判断今天是否打卡
    public Map findByUserID(String userID);

    //用户打卡
    public int addClock(Map map);

    //用户下班打卡
    public int updateClock(Map map);

    //查询用户的打卡记录
    public List<Map> findByUserIDAll(@Param("userID")String userID);

    //分页查询用户的打卡记录
    public List<Map> findByUserIDPage(Map map);

    //查询总行数
    public int count(String userID);
}
