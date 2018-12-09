package com.cui.toutiao.controller;

import com.cui.toutiao.model.HostHolder;
import com.cui.toutiao.model.Message;
import com.cui.toutiao.model.User;
import com.cui.toutiao.model.ViewObject;
import com.cui.toutiao.service.MessageService;
import com.cui.toutiao.service.UserService;
import com.cui.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/9
 * Created with IDEA
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message message = new Message();
        message.setFromId(fromId);
        message.setToId(toId);
        message.setContent(content);
        message.setCreatedDate(new Date());
        message.setConversationId(fromId < toId ? fromId + "_" + toId : toId + "_" + fromId);
        messageService.addMessage(message);
        return ToutiaoUtil.getJSONString(message.getId());
    }

    @RequestMapping(value = "/msg/detail", method = RequestMethod.GET)
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> conversation = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message msg : conversation) {
                ViewObject vo = new ViewObject();
                vo.set("headUrl", userService.getUser(msg.getFromId()).getHeadUrl());
                vo.set("message", msg);
                vos.add(vo);
            }
            model.addAttribute("messages", vos);
        } catch (Exception e) {
            logger.error("获取conversation内容失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(value = "/msg/list", method = RequestMethod.GET)
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                vo.set("unread", messageService.getConversationUnReadMsgCount(localUserId, msg.getConversationId()));
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User targetUser = userService.getUser(targetId);
//                vo.set("userId",targetId);
//                vo.set("headUrl",targetUser.getHeadUrl());
//                vo.set("username",targetUser.getName());
                vo.set("user", targetUser);
                vos.add(vo);
            }
            model.addAttribute("conversations", vos);
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }
}
