package com.cui.toutiao.service;

import com.cui.toutiao.dao.CommentDAO;
import com.cui.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/9
 * Created with IDEA
 */
@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public List<Comment> getComments(int entityId,int entityType){
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDAO.getCommentCount(entityId,entityType);
    }
}
