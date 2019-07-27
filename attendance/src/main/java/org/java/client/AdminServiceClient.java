package org.java.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Component
@FeignClient(value = "ADMIN-SERVICE")
public interface AdminServiceClient {

    //调用admin-service服务的接口
    @RequestMapping("/userManage/findUserID")
    public String findUserID(@RequestParam Map map);

    //调用admin-service服务的接口
    @RequestMapping("/userManage/findByUserID")
    public String findByUserID(@RequestParam("userID")String userID);


}
