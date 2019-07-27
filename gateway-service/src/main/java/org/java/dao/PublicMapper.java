package org.java.dao;

import org.apache.ibatis.annotations.Param;

public interface PublicMapper {
    //查询登录的用户今天是否打卡
    public int findClock(@Param("userID") String userID);
}
