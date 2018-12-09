package com.cui.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:CuiWJ
 * @Date:2018/11/11
 * @Description: 事件类对象
 */
public class EventModel {
    private EventType type;//事件类型
    private int actorId;//事件发起者id
    private int entityType;//事件操作实体类型
    private int entityId;//事件操作实体id
    private int entityOwnerId;//实体拥有者id
    private Map<String, String> exts = new HashMap<>();//扩展,用于保存事件现场信息

    //Constructor
    public EventModel() {
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    //一些helper方法
    public String getExt(String key){
        return exts.get(key);
    }

    public EventModel setExt(String key,String value){
        exts.put(key,value);
        return this;
    }

    //getter and setter
    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
