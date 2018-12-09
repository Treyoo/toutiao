package com.cui.toutiao.async.handler;

import com.cui.toutiao.async.EventHandler;
import com.cui.toutiao.async.EventModel;
import com.cui.toutiao.async.EventType;
import com.cui.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:CuiWJ
 * date:2018/11/11
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandle(EventModel event) {
        Map<String,Object> map=new HashMap<>();
        map.put("username",event.getExt("username"));
        mailSender.sendWithHTMLTemplate(event.getExt("to"),
                "登录异常","mails/welcome.html",map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
