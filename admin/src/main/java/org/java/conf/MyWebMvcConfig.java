package org.java.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc自定义配置类
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    /**
     * 自定义视图映射器
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //当访问/时重定向到main.html页面
        registry.addViewController("/ditu").setViewName("ditu");
        registry.addRedirectViewController("/", "main");
    }
}
