package org.java.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 设置cookie
     *
     * @param cookieName
     *            cookie的名字
     * @param cookieValue
     *            cookie的值
     * @param request
     * @param response
     * @param youXiaoTime
     *            cookie的有效时间
     */
    public static void setCookie(String cookieName, String cookieValue,
                                 HttpServletRequest request, HttpServletResponse response,
                                 int youXiaoTime) {
        Cookie cookie = new Cookie(cookieName, cookieValue); // 保存用户名到Cookie
        String host = request.getServerName();
        cookie.setPath("/");
        cookie.setDomain(host);
        cookie.setMaxAge(youXiaoTime);
        response.addCookie(cookie);
    }

    /**
     * 销毁cookie
     *
     * @param CookieName
     *            cookie的名字
     *
     * @param response
     */
    public static void destroyCookie(String CookieName,
                                     HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(CookieName)) {
                    Cookie cookie = new Cookie(CookieName, "ww");// 这边得用"",不能用null
                    cookie.setPath("/");// 设置成跟写入cookies一样的
                    // cookie.setDomain(".wangwz.com");//设置成跟写入cookies一样的
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

    }


    /**
     * 获取cookie
     *
     * @param CookieName
     *            cookie的名称
     * @param request
     * @return
     */
    public static String getCookie(String CookieName, HttpServletRequest request) {
        String CookieValue = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CookieName.equals(cookie.getName())) {
                    CookieValue = cookie.getValue(); // 得到cookie的用户名
                }

            }

        }
        return CookieValue;
    }
}
