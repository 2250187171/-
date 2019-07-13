package org.java.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "ADMIN-SERVICE")
public interface AdminServiceClient {

    //调用用户管理服务
    @RequestMapping("/userManage/userMessage")
    public String userMessage(Model model);
}
