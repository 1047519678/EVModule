package cn.mvc.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    /*@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获得请求的URL
        String url = httpServletRequest.getRequestURI();
        if (url.contains("/checkLogin.do")||url.contains("/userLogin.do")||url.contains("/addUser.do")) {
            return true;
        }
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("user")!=null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                return true;
            }
            System.err.println("###############################################"+user);
            //不合条件的给提示信息，并转到登录页面
            httpServletRequest.setAttribute("msg", "您还没登录，请先登录！");
            httpServletRequest.getRequestDispatcher("/userLogin.jsp").forward(httpServletRequest, httpServletResponse);
            return false;
        }

        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        }
        httpServletRequest.setAttribute("msg", "您还没登录，请先登录！");
        httpServletRequest.getRequestDispatcher("/userLogin.jsp").forward(httpServletRequest, httpServletResponse);
        //httpServletRequest.getRequestDispatcher("/adminLogin.jsp").forward(httpServletRequest, httpServletResponse);
        //妥协对所有请求都分开拦截
        return false;

    }*/

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
