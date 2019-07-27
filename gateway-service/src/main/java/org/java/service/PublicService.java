package org.java.service;

public interface PublicService {

    //查询登录的用户今天是否打卡
    public int findClock(String userID);
}
