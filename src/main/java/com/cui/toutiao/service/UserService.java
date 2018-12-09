package com.cui.toutiao.service;

import com.cui.toutiao.dao.LoginTicketDAO;
import com.cui.toutiao.dao.UserDAO;
import com.cui.toutiao.model.LoginTicket;
import com.cui.toutiao.model.User;
import com.cui.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();//用于存放返回给客户端的提示消息
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        if (userDAO.selectByName(username) != null) {
            map.put("msgname", "用户名已经被注册");
            return map;
        }
        /*
        这里可以根据需求继续写其他业务，如检测密码强度等
         */

        //通过参数验证，进行注册（添加用户）
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password + user.getSalt()));//密码+盐MD5加密存储
        userDAO.addUser(user);

        //注册成功后自动登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();//用于存放返回给客户端的提示消息
        if (StringUtils.isBlank(username)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msgname", "用户名不存在");
            return map;
        }
        //验证密码是否正确
        if (!ToutiaoUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msgpwd", "密码错误");
            return map;
        }

        /*登录验证通过，下发token*/
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    private String addLoginTicket(int userId) {
        //构造一个token
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        ticket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);//设置24小时有效期
        ticket.setExpired(date);
        //将token存入数据库
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket,1);//将ticket状态置1表实该ticket已失效
    }
}
