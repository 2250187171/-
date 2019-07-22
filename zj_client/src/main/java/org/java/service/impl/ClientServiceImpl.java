package org.java.service.impl;

import org.java.dao.ClientMapper;
import org.java.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientMapper mapper;

    @Override  //客户登录
    public Map clientLogin(String name, String pwd) {

        return mapper.clientLogin(name, pwd);
    }

    @Override //验证客户注册的手机号码是否存在
    public Map checkPhone(String phone) {
        return mapper.checkPhone(phone);
    }

    @Transactional
    @Override    //客户注册
    public void insertClient(Map map) {
        Date date = new Date(); //注册日期
        //判断是个体注册还是企业注册
        String customerTypeID = map.get("customerTypeID").toString(); //取出类型编号

        if(customerTypeID.equals("1")){ //个人注册

            map.put("registerDate", date);

            mapper.insertClient(map);

        }else{  //企业注册
            map.put("registerDate", date);

            String companyname = (String) map.get("companyname"); //把公司名称取出

            map.put("customName", companyname); //赋值给客户名称(真实姓名)

            map.put("sex", null);

            mapper.insertClient(map);
        }
    }

    @Transactional  //找回密码,按照客户的手机号修改(重置)密码
    @Override
    public int setNewPassword(String phone, String newPwd) {
       return mapper.setNewPassword(phone, newPwd);
    }

    @Override   //验证身份证号码是否存在
    public int checkCustomIDNumber(String customID,String customIDNumber) {
        return mapper.checkCustomIDNumber(customID, customIDNumber);
    }

    @Override   //验证要修改的手机号码是否存在
    public int checkPhoneUpdate(String customID, String phoneNumber) {
        return mapper.checkPhoneUpdate(customID, phoneNumber);
    }

    @Transactional  //按照客户的主键修改个体客户的基本信息
    @Override
    public void updateClientIndividual(Map map) {
        mapper.updateClientIndividual(map);
    }

    @Transactional
    @Override   //按照客户的主键修改客户的密码
    public int updateClientPwd(Map map) {
        return mapper.updateClientPwd(map);
    }

    @Override //验证要修改的电话号码是否存在
    public int checkTelePhoneNumber(String customID, String telePhoneNumber) {
        return mapper.checkTelePhoneNumber(customID, telePhoneNumber);
    }

    @Override //验证注册号是否已经存在
    public int checkRegisterCard(String customID, String businesslicensenumber) {
        return mapper.checkRegisterCard(customID, businesslicensenumber);
    }

    @Transactional
    @Override //按照客户的主键修改企业客户的基本信息
    public void updateClientEnterprise(Map map) {
        mapper.updateClientEnterprise(map);
    }


}
