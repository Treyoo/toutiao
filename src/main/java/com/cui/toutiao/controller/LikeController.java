package com.cui.toutiao.controller;

import com.cui.toutiao.async.EventModel;
import com.cui.toutiao.async.EventType;
import com.cui.toutiao.async.Eventproducer;
import com.cui.toutiao.model.EntityType;
import com.cui.toutiao.model.HostHolder;
import com.cui.toutiao.service.LikeService;
import com.cui.toutiao.service.NewsService;
import com.cui.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * author:CuiWJ
 * Date:2018/11/10
 * Created with IDEA
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;
    @Autowired
    Eventproducer eventproducer;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {

        int localUserId = hostHolder.getUser().getId();
        long likeCount = likeService.like(localUserId, newsId, EntityType.ENTITY_NEWS);
        //更新数据库news表like_count字段。以后异步化
        newsService.updateLikeCount(newsId,(int)likeCount);

        //产生like事件
        eventproducer.fireEvent(new EventModel(EventType.LIKE).setActorId(localUserId)
                .setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS)
                .setEntityOwnerId(newsService.getById(newsId).getUserId()));

        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId) {

        int localUserId = hostHolder.getUser().getId();
        long likeCount = likeService.dislike(localUserId, newsId, EntityType.ENTITY_NEWS);
        //更新数据库news表like_count字段。以后异步化
        newsService.updateLikeCount(newsId,(int)likeCount);

        return ToutiaoUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
