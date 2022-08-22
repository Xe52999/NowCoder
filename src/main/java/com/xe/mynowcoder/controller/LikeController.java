package com.xe.mynowcoder.controller;


import com.xe.mynowcoder.entity.Event;
import com.xe.mynowcoder.entity.User;
import com.xe.mynowcoder.event.EventProducer;
import com.xe.mynowcoder.service.LikeService;
import com.xe.mynowcoder.util.HostHolder;
import com.xe.mynowcoder.util.NowCoderConstant;
import com.xe.mynowcoder.util.NowCoderUtil;
import com.xe.mynowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.util.KeyUtil;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements NowCoderConstant {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;


    @Autowired
    private EventProducer eventProducer;


    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping(path = "/like")
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId,int postId) {
        User user = hostHolder.getUser();

        if(user==null) return NowCoderUtil.getJSONString(1,"您还未登录");


        // 点赞
        likeService.like(user.getUserId(), entityType, entityId,entityUserId);

        // 数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getUserId(), entityType, entityId);
        // 返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getUserId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId", postId);
            //postId 可以让我们连接到帖子详情页面
            eventProducer.fireEvent(event);
        }




        //异步请求，返回的是Json格式字符串，所以不用model
        return NowCoderUtil.getJSONString(0, null, map);
    }

}
