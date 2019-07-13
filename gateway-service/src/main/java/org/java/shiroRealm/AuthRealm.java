package org.java.shiroRealm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.java.service.A_UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * shiro的认证授权类
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private A_UserService service;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获得用户凭证
        String phoneNumber = (String) authenticationToken.getPrincipal();
        System.out.println("realm的凭证："+phoneNumber);
        //查询数据库判断该用户是否存在
        String password= service.findByPassword(phoneNumber);
        if(password==null){
            return null;
        }
        //加密盐
        String salt="accp";
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(phoneNumber, password, ByteSource.Util.bytes(salt),"myrealm");
        return info;
    }
}
