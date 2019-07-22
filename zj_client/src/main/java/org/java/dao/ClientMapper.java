package org.java.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface ClientMapper {
    //客户登录
    public Map clientLogin(@Param("phoneNumber") String name,@Param("customPassword") String pwd);

    //验证客户注册的手机号码是否存在
    public Map checkPhone(@Param("phone") String phone);

    //客户注册
    public void insertClient(Map map);

    //找回密码,按照客户的手机号修改(重置)密码
    public int setNewPassword(@Param("phone") String phone,@Param("newPwd")String newPwd);

    //验证身份证号码是否存在
    public int checkCustomIDNumber(@Param("customID") String customID,@Param("customIDNumber") String customIDNumber);

    //验证要修改的手机号码是否存在
    public int checkPhoneUpdate(@Param("customID") String customID,@Param("phoneNumber") String phoneNumber);

    //按照客户的主键修改个体客户的基本信息
    public void updateClientIndividual(Map map);

    //按照客户的主键修改客户的密码
    public int updateClientPwd(Map map);

    //验证要修改的电话号码是否存在
    public int checkTelePhoneNumber(@Param("customID") String customID,@Param("telePhoneNumber") String telePhoneNumber);

    //验证注册号是否已经存在
    public int checkRegisterCard(@Param("customID") String customID,@Param("businesslicensenumber") String businesslicensenumber);

    //按照客户的主键修改企业客户的基本信息
    public void updateClientEnterprise(Map map);
}
