package org.java;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(value = "org.java.dao")
//开启session共享
@EnableRedisHttpSession
//开启Feign
@EnableFeignClients
//开启事务
@EnableTransactionManagement
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceApplication.class, args);
    }
}
