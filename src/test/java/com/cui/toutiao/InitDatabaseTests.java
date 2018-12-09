package com.cui.toutiao;

import com.cui.toutiao.dao.CommentDAO;
import com.cui.toutiao.dao.LoginTicketDAO;
import com.cui.toutiao.dao.NewsDAO;
import com.cui.toutiao.dao.UserDAO;
import com.cui.toutiao.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Test
    public void initData() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            //向数据库添加用户数据
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);//测试添加用户

            //向数据库添加资讯数据
            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setUserId(i + 1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);//测试添加资讯

            //向数据库添加登录凭证数据
            LoginTicket ticket = new LoginTicket();
            ticket.setExpired(date);
            ticket.setStatus(0);
            ticket.setTicket(String.format("TICKET%d", i));
            ticket.setUserId(i + 1);
            loginTicketDAO.addLoginTicket(ticket);

            //向数据库添加评论数据
            for (int j = 0; j < 3; j++) {
                Comment comment = new Comment();
                comment.setEntityId(news.getUserId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setContent("评论"+(j+1));
                comment.setCreatedDate(new Date());
                comment.setUserId(j+2);
                comment.setStatus(0);
                commentDAO.addComment(comment);
            }
            /////***准备测试数据完毕***////

            user.setPassword("newpassword");
            userDAO.updatePassword(user);//测试更新用户密码
        }

        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());//测试查询
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));

        loginTicketDAO.updateStatus("TICKET1", 1);
        Assert.assertEquals(2, loginTicketDAO.selectByTicket("TICKET1").getUserId());
        Assert.assertEquals(1, loginTicketDAO.selectByTicket("TICKET1").getStatus());

        Assert.assertEquals(3,commentDAO.getCommentCount(1,EntityType.ENTITY_NEWS));
        Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }
}
