package org.java.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
@Component
public class AjaxFilter extends ZuulFilter {
    //执行类型,之前执行还是之后
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    //过滤器的执行顺序
    @Override
    public int filterOrder() {
        return SERVLET_DETECTION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        //获得请求上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获得请求httpservletrequest
        HttpServletRequest request = context.getRequest();
        //获得请求的路径
        String uri = request.getRequestURI();
        if (uri.startsWith("/gateway/zjclient/personalCenter") || uri.startsWith("/gateway/zjclient/online")) {
            return true;
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("----------------------------进入zuul过滤器");
        //获得请求上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获得请求httpservletrequest
        HttpServletRequest request = context.getRequest();

        HttpServletResponse response = context.getResponse();

        try {      //跳转得到登录页面进行登录
            response.sendRedirect("http://localhost:9000/gateway/zjclient/customlogin");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
