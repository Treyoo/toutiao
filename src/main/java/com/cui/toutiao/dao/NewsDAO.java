package com.cui.toutiao.dao;

import com.cui.toutiao.model.News;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * author:CuiWJ
 * Date:2018/11/7
 * Created with IDEA
 */
@Mapper
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELDS = " title, link, image, like_count, comment_count, created_date,user_id ";
    String SELECT_FIELDS = " id, "+INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);


    //通过NewsDAO.xml方式执行SQL语句，变量注解作用是传递变量的值到xml中
    List<News> selectByUserIdAndOffset(@Param("userId") int userId,
                                       @Param("offset") int offset,
                                       @Param("limit") int limit);
    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    News getById(int id);

    @Update({"update ",TABLE_NAME," set comment_count=#{count} where id=#{entityId}"})
    void updateCommentCount(@Param("entityId") int entityId, @Param("count") int count);

    @Update({"update ",TABLE_NAME," set like_count=#{likeCount} where id=#{entityId}"})
    void updateLikeCount(@Param("entityId") int entityId, @Param("likeCount") int likeCount);
}
