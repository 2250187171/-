package org.java.err;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

//报异常时会通知该类处理异常
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    /**
     * 报出异常后拦截
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String handlerException(HttpServletRequest request, Exception ex){
        //获得异常信息
        String msg=ex.getMessage();
        request.setAttribute("msg", msg);
        return "/err";
    }
}
