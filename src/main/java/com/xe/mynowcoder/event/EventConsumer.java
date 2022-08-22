package com.xe.mynowcoder.event;

import com.alibaba.fastjson.JSONObject;
import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.Event;
import com.xe.mynowcoder.entity.Message;
import com.xe.mynowcoder.service.DiscussPostService;
import com.xe.mynowcoder.service.ElasticsearchService;
import com.xe.mynowcoder.service.MessageService;
import com.xe.mynowcoder.util.NowCoderConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class EventConsumer implements NowCoderConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticsearchService elasticsearchService;


    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        //恢复成Event类型
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            content.putAll(event.getData());
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }



    // 消费发帖事件
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }





}
