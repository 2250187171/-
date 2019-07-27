package org.java.service.impl;

import org.java.dao.PublicMapper;
import org.java.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    private PublicMapper mapper;
    //查询用户今日是否打卡
    @Override
    public int findClock(String userID) {
        return mapper.findClock(userID);
    }
}
