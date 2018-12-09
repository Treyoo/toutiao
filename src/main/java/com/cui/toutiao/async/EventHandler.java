package com.cui.toutiao.async;

import java.util.List;

/**
 * creatd by
 * @author cui
 */
public interface EventHandler {
    /**
     * 处理事件
     * @param event
     */
    void doHandle(EventModel event);

    /**
     * 获取支持的事件类型
     * @return 支持的事件类型列表
     */
    List<EventType> getSupportEventType();

}
