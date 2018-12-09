package com.cui.toutiao.async;

/**
 * @Author: CuiWJ
 * @Date: 2018/11/11
 * @Description: 事件类型枚举
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
