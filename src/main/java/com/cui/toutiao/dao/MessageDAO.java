package com.cui.toutiao.dao;

import com.cui.toutiao.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/9
 * Created with IDEA
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read,conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversation_id=#{conversationId} limit #{offset},#{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    /*
    //会话列表每个会话显示当前会话的最后一条消息
    //这是视频里的SQL语句，结果不对
    select * ,count(id) as cnt from (SELECT * FROM message where from_id=12 or to_id=12 order by id desc) tt group by conversation_id order by id desc;
    @Select({"select ", INSERT_FIELDS, ",count(id) as id from (select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where from_id=#{fromId} or to_id=#{fromId} order by id desc) tt group by conversation_id order by id desc"})
    List<Message> getCoversationList(@Param("fromId") int fromId, @Param("offset") int offset, @Param("limit") int limit);
    */

    /*这是我自己拼凑的SQL语句,结果是正确的，但是比较复杂，效率也比较低
    SELECT * ,msgCount as id from(select m.*,msgCount from message m,
    (SELECT max(created_date)as max_date,COUNT(*) as msgCount FROM message
    where from_id=12 or to_id=12 GROUP BY conversation_id)t
    where m.created_date=t.max_date)t2 GROUP BY created_date;*/
    @Select({"SELECT ",INSERT_FIELDS,",msgCount as id from(select m.*,msgCount from message m,",
            " (SELECT  max(created_date)as max_date,COUNT(*) as msgCount FROM message",
            " where from_id=#{fromId} or to_id=#{fromId} GROUP BY conversation_id)t",
            " where m.created_date=t.max_date)t2  GROUP BY conversation_id ORDER BY created_date DESC",
            " limit #{offset},#{limit}"})
    List<Message> getCoversationList(@Param("fromId") int fromId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and  conversation_id=#{conversationId}"})
    int getConversationUnReadMsgCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, " where to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationTotalMsgCount(@Param("userId") int userId, @Param("conversationId") String conversationId);
}
