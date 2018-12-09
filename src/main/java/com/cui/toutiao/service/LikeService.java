package com.cui.toutiao.service;

import com.cui.toutiao.util.JedisAdapter;
import com.cui.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:CuiWJ
 * Date:2018/11/10
 * Created with IDEA
 */
@Service
public class LikeService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 获取指定用户对指定实体的赞/踩状态
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return 整型状态码，-1表示踩，0表示无，1表示赞
     */
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeSetKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if (jedisAdapter.sismember(likeSetKey, String.valueOf(userId))) {
            return 1;
        }
        String dislikeSetKey = RedisKeyUtil.getDislikekey(entityId, entityType);
        return jedisAdapter.sismember(dislikeSetKey, String.valueOf(userId)) ? -1 : 0;
    }

    /**
     * 指定用户对指定实体点赞
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return 点赞后该实体被赞的数量
     */
    public long like(int userId, int entityId, int entityType) {
        String likeSetKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeSetKey, String.valueOf(userId));
        //赞与踩是互斥的，点了赞需要将相应的userId从踩set里移除（有的话）
        String dislikeSetKey = RedisKeyUtil.getDislikekey(entityId, entityType);
        jedisAdapter.srem(dislikeSetKey, String.valueOf(userId));
        return jedisAdapter.scard(likeSetKey);
    }

    /**
     * 指定用户对指定实体点踩
     *
     * @param userId
     * @param entityId
     * @param entityType
     * @return 点踩后该实体被赞的数量
     */
    public long dislike(int userId, int entityId, int entityType) {
        String dislikeSetKey = RedisKeyUtil.getDislikekey(entityId, entityType);
        jedisAdapter.sadd(dislikeSetKey, String.valueOf(userId));
        //赞与踩是互斥的，点了赞需要将相应的userId从踩set里移除（有的话）
        String likeSetKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeSetKey, String.valueOf(userId));
        return jedisAdapter.scard(likeSetKey);
    }
}
