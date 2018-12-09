package com.cui.toutiao.async.handler;

import com.cui.toutiao.async.EventHandler;
import com.cui.toutiao.async.EventModel;
import com.cui.toutiao.async.EventType;
import com.cui.toutiao.model.Message;
import com.cui.toutiao.model.News;
import com.cui.toutiao.service.MessageService;
import com.cui.toutiao.service.NewsService;
import com.cui.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author CuiWJ
 * @date 2018/11/11
 * @description
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    MessageService messageService;

    @Override
    public void doHandle(EventModel event) {
        System.out.println("处理like事件！");
        //向被赞用户发送站内信
        Message msg=new Message();
        int SystemUserId=3;//用3号用户代表系统
        msg.setFromId(SystemUserId);
        msg.setToId(event.getEntityOwnerId());
        msg.setContent("用户["+userService.getUser(event.getActorId()).getName()
                +"]赞了你的资讯<a href=\"http://127.0.0.1:8080/news/"+event.getEntityId()+"\">"
                +newsService.getById(event.getEntityId()).getTitle()+"</a>");
        msg.setCreatedDate(new Date());
        msg.setConversationId(SystemUserId+"_"+event.getEntityOwnerId());
        messageService.addMessage(msg);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
