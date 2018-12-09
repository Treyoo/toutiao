package com.cui.toutiao.controller;

import com.cui.toutiao.async.EventModel;
import com.cui.toutiao.async.EventType;
import com.cui.toutiao.async.Eventproducer;
import com.cui.toutiao.model.News;
import com.cui.toutiao.model.ViewObject;
import com.cui.toutiao.service.NewsService;
import com.cui.toutiao.service.UserService;
import com.cui.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private Eventproducer eventproducer;

    private static final Logger logger = LoggerFactory.getLogger(ToutiaoUtil.class);



    @RequestMapping(path = {"/reg/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String reg(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rember,
                      HttpServletResponse response){
        try{
            Map<String,Object> map=userService.register(username,password);
           if(map.containsKey("ticket")){
               Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
               cookie.setPath("/");//设置此cookie全部路径（即全站）有效
               if(rember>0){
                   cookie.setMaxAge(3600*24*5);//设置cookie有效期为5天
               }
               response.addCookie(cookie);
               return ToutiaoUtil.getJSONString(0,"注册成功");
           }else{
               return ToutiaoUtil.getJSONString(1,map);
           }
        }
        catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"注册异常");
        }
    }

    @RequestMapping(path = {"/login/"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember",defaultValue = "0") int rember,
                        HttpServletResponse response){
        try{
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket")){
               Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
               cookie.setPath("/");//设置此cookie全部路径（即全站）有效
               if(rember>0){
                   cookie.setMaxAge(3600*24*5);//设置cookie有效期为5天
               }
               response.addCookie(cookie);
               eventproducer.fireEvent(new EventModel(EventType.LOGIN).setExt("username",username)
               .setExt("to","443353439@qq.com"));
               return ToutiaoUtil.getJSONString(0,"登录成功");
            }else{
               return ToutiaoUtil.getJSONString(1,map);
            }
        }
        catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return ToutiaoUtil.getJSONString(1,"登录异常");
        }
    }

    @RequestMapping(path = {"/logout/"},method = {RequestMethod.GET,RequestMethod.POST})
    public String loginout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";//浏览器重定向到首页
    }

}
