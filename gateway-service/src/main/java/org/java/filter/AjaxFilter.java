package org.java.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
//@Component
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
        if (uri.startsWith("/gateway/admin")) {
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

        String requestBody = null;
//        try {
//            requestBody = StreamUtils.copyToString(context.getRequest().getInputStream(), Charsets.UTF_8);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        context.addOriginResponseHeader("content-type", "application/json;charset=utf-8");
        //设置可以跨域访问
        context.addZuulResponseHeader("Access-Control-Allow-Headers", "content-type,x-requested-with");
        context.addZuulResponseHeader("Access-Control-Allow-Origin", "*");
        context.addZuulResponseHeader("content-type", "application/json;charset=utf-8");
        // 如果为options请求则一定要返回200状态码
        if ("options".equals(context.getRequest().getMethod().toLowerCase())) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.OK.value());
        }
        return null;
    }
}
