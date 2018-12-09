package com.cui.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.cui.toutiao.util.JedisAdapter;
import com.cui.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * author:CuiWJ
 * date:2018/11/11
 */
@Service
public class Eventproducer {
    private static final Logger logger = LoggerFactory.getLogger(Eventproducer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            //序列化事件对象方便存入redis队列中
            String eventJSON = JSONObject.toJSONString(eventModel);
            String eventQueueKey = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(eventQueueKey, eventJSON);
            return true;
        } catch (Exception e) {
            logger.error("将事件加入队列失败" + e.getMessage());
            return false;
        }
    }
}
