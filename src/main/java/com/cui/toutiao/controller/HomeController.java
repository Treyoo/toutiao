package com.cui.toutiao.controller;

import com.cui.toutiao.model.EntityType;
import com.cui.toutiao.model.HostHolder;
import com.cui.toutiao.model.News;
import com.cui.toutiao.model.ViewObject;
import com.cui.toutiao.service.LikeService;
import com.cui.toutiao.service.NewsService;
import com.cui.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model, @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNewsList(0, 0, 10));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNewsList(userId, 0, 10));
        return "home";
    }

    private List<ViewObject> getNewsList(int userId, int offset, int limit) {
        List<News> newsList = newsService.getLatestNews(userId, offset, limit);//当userId为0表示全部用户
        List<ViewObject> vos = new ArrayList<>();//ViewObject实际是一个map,利用它方便传数据
        int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));
            if(localUserId!=0){
                vo.set("like",likeService.getLikeStatus(localUserId,news.getId(),EntityType.ENTITY_NEWS));
            }
            vos.add(vo);
        }
        return vos;
    }
}
