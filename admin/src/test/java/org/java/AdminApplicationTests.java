package org.java;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.java.service.A_UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

    @Autowired
    private A_UserService service;
    @Test
    public void contextLoads() {
//        Map byPhoneNumber = service.findByPhoneNumber("13323998955");
//        System.out.println(byPhoneNumber);
        String name="md5";
        String pwd="123";
        int count=3;
        String sale="accp";
        SimpleHash simpleHash = new SimpleHash(name, pwd, sale, count);

        System.out.println(simpleHash);
    }

}
