package org.java.conf;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.java.shiroRealm.AuthRealm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro的配置类，该类配置了shiro的过滤工厂
 */
@Configuration
public class ShiroConfig {

    /**
     * 安全管理器
     * @param securityManager
     * @return
     * 将工厂shiroFilterFactoryBean注入到容器中
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        //创建一个过滤工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //添加安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //设置用户没有登录或登录失败，跳转到那个页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //设置shiro拦截路径的规则，将这些规则放入Map中
        Map<String,String> shiroFileDefinitionmap=new HashMap<>();
        shiroFileDefinitionmap.put("/js/**", "anon");
        shiroFileDefinitionmap.put("/image/**", "anon");
        shiroFileDefinitionmap.put("/images/**", "anon");
        shiroFileDefinitionmap.put("/css/**", "anon");
        shiroFileDefinitionmap.put("/layer/**", "anon");
        shiroFileDefinitionmap.put("/logout", "logout");
        shiroFileDefinitionmap.put("/**", "authc");
        //把规则放入到工厂中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFileDefinitionmap);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建安全管理器
     * 将安全管理器注入到容器中
     * @return
     */
    @Bean
    public SecurityManager securityManager(){
        //声明安全管理器实例
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        //将shiro的认证授权类添加到安全管理器中
        securityManager.setRealm(authRealm());
        //将缓存添加到安全管理器中
        securityManager.setCacheManager(ehCacheManager());
        return securityManager;
    }

    /**
     * 将AuthRealm类注入到容器中
     */
    public AuthRealm authRealm(){
        AuthRealm authRealm=new AuthRealm();
        authRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return authRealm;
    }
    /**
     * 将凭证匹配器注入到容器中
     * 用于指定加密方式
     *
     */
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        //指定加密方式
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //指定加密次数
        hashedCredentialsMatcher.setHashIterations(3);
        return hashedCredentialsMatcher;
    }


    /**
     * 将缓存管理器注入到容器中
     */
    public EhCacheManager ehCacheManager(){
        //创建缓存管理器
        EhCacheManager ehCacheManager=new EhCacheManager();
        //指定缓存管理器配置文件的路径
        ehCacheManager.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
        return ehCacheManager;
    }
}
