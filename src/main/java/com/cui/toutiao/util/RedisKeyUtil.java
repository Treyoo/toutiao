package com.cui.toutiao.util;

/**
 * 本类用于同意保存到redis的key格式
 * author:CuiWJ
 * Date:2018/11/10
 * Created with IDEA
 */
public class RedisKeyUtil {
    private static String SPLIT=":";
    private static String BIZ_LIKE="LIKE";
    private static String BIZ_DISLIKE="DISLIKE";
    private static String BIZ_EVENT="EVENT";

    public static String getLikeKey(int entityId,int entityType){
        return BIZ_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

    public static String getDislikekey(int entityId,int entityType){
        return BIZ_DISLIKE+SPLIT+entityType+SPLIT+entityId;
    }
    public static String getEventQueueKey(){
        return BIZ_EVENT;
    }
}
