package com.cui.toutiao.interceptor;

import com.cui.toutiao.dao.LoginTicketDAO;
import com.cui.toutiao.dao.UserDAO;
import com.cui.toutiao.model.HostHolder;
import com.cui.toutiao.model.LoginTicket;
import com.cui.toutiao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器，通过验证token判断用户是谁
 * author:CuiWJ
 * Date:2018/11/8
 * Created with IDEA
 */
@Component
public class PassportInterceptor implements HandlerInterceptor {

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取客户端中的cookie
        String ticket = null;
        Cookie[] cookies= httpServletRequest.getCookies();
        if(cookies!=null){
            for (Cookie cookie :cookies) {
                if ("ticket".equals(cookie.getName())) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }
        if (ticket != null) {//浏览器中有相应的token(注意：ticket只是变量名，和token是同一个意思)
            LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
            if (loginTicket == null
                    || loginTicket.getExpired().before(new Date())
                    || loginTicket.getStatus() != 0) {//数据库中没有相应有效的token
                //返回true代表通过拦截，这里返回true是因为没登录的用户也可以访问系统首页，只是首页不显示用户名
                return true;
            }

            //验证了用户是登录状态后，获取用户信息方便后面业务
            User user = userDAO.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }


        return true;
    }

    //请求处理完毕，渲染页面前执行本方法
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(modelAndView!=null&&hostHolder.getUser()!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
