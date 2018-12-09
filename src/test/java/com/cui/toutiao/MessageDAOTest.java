package com.cui.toutiao;

import com.cui.toutiao.dao.MessageDAO;
import com.cui.toutiao.model.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/10
 * Created with IDEA
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class MessageDAOTest {
    @Autowired
    MessageDAO messageDAO;

    @Test
    public void messageDAOTest(){
        List<Message> conversationList=messageDAO.getCoversationList(12,0,10);
        Assert.assertEquals(3,conversationList.size());
    }

}
