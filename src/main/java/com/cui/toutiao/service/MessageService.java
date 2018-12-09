package com.cui.toutiao.service;

import com.cui.toutiao.dao.MessageDAO;
import com.cui.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/9
 * Created with IDEA
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }

    public List<Message>getConversationDetail(String ConversationId,int offset,int limit){
        return messageDAO.getConversationDetail(ConversationId,offset,limit);
    }

    public List<Message> getConversationList(int fromId,int offset,int limit){
        return messageDAO.getCoversationList(fromId,offset,limit);
    }

    public int getConversationUnReadMsgCount(int userId, String conversationId){
        return messageDAO.getConversationUnReadMsgCount(userId,conversationId);
    }
    public int getConversationTotalMsgCount(int userId, String conversationId){
        return messageDAO.getConversationTotalMsgCount(userId,conversationId);
    }
}
