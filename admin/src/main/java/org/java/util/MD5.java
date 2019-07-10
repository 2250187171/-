package org.java.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

@Component
public class MD5 {

    //对密码进行MD5加密
    public Object passwordMD5(String password){
        String hashAlgorithmName = "MD5";
        int hashIterations = 3;
        String salt="accp";
        Object obj = new SimpleHash(hashAlgorithmName, password, salt, hashIterations);
        return obj;
    }
}
