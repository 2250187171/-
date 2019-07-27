package org.java;

import org.activiti.engine.RepositoryService;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {


    @Autowired
    RepositoryService repositoryService;

    @Test
    public void contextLoads() throws JSONException, FileNotFoundException {
        String bpmnName="Logistics.bpmn";
        String pngName="Logistics.png";
        InputStream bpmn_in=new FileInputStream("D:/Logistics.bpmn");
        InputStream png_in=new FileInputStream("D:/Logistics.png");
        repositoryService.createDeployment()
                .addInputStream(bpmnName, bpmn_in)
                .addInputStream(pngName, png_in).deploy();
        System.out.println("部署成功");
    }

    @Test
    public void aa(){
        repositoryService.deleteDeployment("122501", true);
    }
}
