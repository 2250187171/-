package org.java.service.impl;

import org.java.dao.AttendMapper;
import org.java.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AttendServiceImpl implements AttendService {

    @Autowired
    private AttendMapper mapper;

    //根据用户编号判断今天是否打卡
    @Override
    public Map findByUserID(String userID) {
        return mapper.findByUserID(userID);
    }

    //用户上班打卡
    @Override
    public int addClock(Map map) {
        return mapper.addClock(map);
    }

    //用户下班打卡
    @Override
    public int updateClock(Map map) {
        return mapper.updateClock(map);
    }

    //根据用户查询打卡记录
    @Override
    public List<Map> findByUserIDAll(String userID) {
        return mapper.findByUserIDAll(userID);
    }

    //分页查询用户打卡记录
    @Override
    public List<Map> findByUserIDPage(Map map) {
        return mapper.findByUserIDPage(map);
    }

    //查询总行数
    @Override
    public int count(String userID) {
        return mapper.count(userID);
    }


    //根据编号查询打卡详细信息
    @Override
    public Map showClock(int attendanceID) {
        return mapper.showClock(attendanceID);
    }
}
