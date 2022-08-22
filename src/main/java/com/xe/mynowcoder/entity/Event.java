package com.xe.mynowcoder.entity;

import com.xe.mynowcoder.util.MailClient;
import lombok.Builder;
import lombok.Data;
import org.aspectj.weaver.ast.HasAnnotation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xjx
 * @DATE 2022/8/19
 */

public class Event {
    private String topic;
    //触发事件的人
    private int userId;

    //实体类型 如 评论 帖子 私信 以及对应的人
    private int entityType;
    private int entityId;
    private int entityUserId;

    //其他不能预测的数据放在map中
    private Map<String,Object> data =  new HashMap<>();


    //将set方法的void 改为Event 用来链式调用

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;

    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;

    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;

    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}
