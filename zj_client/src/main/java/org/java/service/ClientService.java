package org.java.service;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ClientService {
    //客户登录
    public Map clientLogin(String name,String pwd);
    //验证客户注册的手机号码是否存在
    public Map checkPhone(String phone);
    //客户注册
    public void insertClient(Map map);
    //找回密码,按照客户的手机号修改(重置)密码
    public int setNewPassword(String phone,String newPwd);

    //验证身份证号码是否存在
    public int checkCustomIDNumber(String customID,String customIDNumber);

    //验证要修改的手机号码是否存在
    public int checkPhoneUpdate(String customID,String phoneNumber);

    //按照客户的主键修改个体客户的基本信息
    public void updateClientIndividual(Map map);

    //按照客户的主键修改客户的密码
    public int updateClientPwd(Map map);

    //验证要修改的电话号码是否存在
    public int checkTelePhoneNumber(String customID,String telePhoneNumber);

    //验证注册号是否已经存在
    public int checkRegisterCard(String customID,String businesslicensenumber);

    //按照客户的主键修改企业客户的基本信息
    public void updateClientEnterprise(Map map);
}
