package org.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttendanceApplicationTests {

    @Test
    public void contextLoads() throws ParseException {
        Date date=new Date();
        String x="12:00:00";
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");

        Date parse = format.parse(x);
        System.out.println(parse.getHours()+parse.getMinutes()+parse.getSeconds()+"--------------------------------");
    }

}
