package org.java;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableTransactionManagement

@SpringBootApplication
@EnableEurekaClient
public class ZjClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(ZjClientApplication.class, args);
    }

}
