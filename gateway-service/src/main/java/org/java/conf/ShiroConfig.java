package org.java.conf;


import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.java.shiroRealm.AuthRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro的配置类
 */
@Configuration
public class ShiroConfig {


    /**
     * 将ShiroFilterFactoryBean注入到容器中
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //添加安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置当前请求还没有认证或者认证失败，发出什么请求跳转什么页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //设置shiro拦截路径的规则，把这些规则放到一个map集合
        Map<String,String> shiroFilterDefinitionMap = new LinkedHashMap<>();
        shiroFilterDefinitionMap.put("/assets/**","anon" );
        shiroFilterDefinitionMap.put("/Content/**","anon" );
        shiroFilterDefinitionMap.put("/ditu","anon" );
        shiroFilterDefinitionMap.put("/logout","logout" );//退出
        shiroFilterDefinitionMap.put("/**","authc" );//剩余的所有请求，都需要认证访问
        //把规程放入shiroFilterFactoryBean中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterDefinitionMap);
        return shiroFilterFactoryBean;
    }


    //将SecurityManager安全管理器注入到容器中
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //添加shiro认证授权的类
        securityManager.setRealm(authcRealm());
        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    //将authcRealm注入到容器中
    @Bean
    public AuthRealm authcRealm(){
        AuthRealm authcRealm = new AuthRealm();
        authcRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return authcRealm;
    }

    //凭证匹配器用于指定加密方式
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //加密次数
        hashedCredentialsMatcher.setHashIterations(3);
        return hashedCredentialsMatcher;
    }

    //开启授权
    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    @Bean
    public EhCacheManager ehCacheManager(){
        //创建缓存管理器
        EhCacheManager ehCacheManager = new EhCacheManager();
        //指定缓存管理器配置文件的路径
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }
}
