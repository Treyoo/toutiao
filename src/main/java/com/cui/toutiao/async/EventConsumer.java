package com.cui.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.cui.toutiao.util.JedisAdapter;
import com.cui.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:CuiWJ
 * date:2018/11/11
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**构建EventHandler路由表**/
        //1.把所有实现了EventHandler接口的类找出来
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                //2.获取每个EventHandler类支持的事件类型
                List<EventType> eventTypes = entry.getValue().getSupportEventType();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    //3.根据支持的事件类型将EventHandler类放入路由表相应list中
                    config.get(eventType).add(entry.getValue());
                }
            }
        }

        /**新建一个线程消费事件**/
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){// 从队列一直消费
                    String eventQueueKey = RedisKeyUtil.getEventQueueKey();
                    List<String> messages=jedisAdapter.brpop(0,eventQueueKey);
                    for(String message:messages){
                        if(message.equals(eventQueueKey)){//brpop一次取出两个值，第一个值是元素所属队列的名字
                            continue;
                        }
                        EventModel event= JSON.parseObject(message,EventModel.class);//反序列化出事件对象
                        //根据EventHandler路由表把事件传给关注此事件的handler
                        if(!config.containsKey(event.getType())){
                            logger.error("不能识别的事件类型:"+event.getEntityType());
                            continue;
                        }
                        List<EventHandler> eventHandlers=config.get(event.getType());
                        for(EventHandler eventHandler:eventHandlers){
                            eventHandler.doHandle(event);
                        }
                    }
                }
            }
        });
        thread.start();
    }
}
