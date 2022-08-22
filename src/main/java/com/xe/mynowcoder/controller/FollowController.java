package com.xe.mynowcoder.controller;


import com.xe.mynowcoder.entity.Event;
import com.xe.mynowcoder.entity.Page;
import com.xe.mynowcoder.entity.User;
import com.xe.mynowcoder.event.EventProducer;
import com.xe.mynowcoder.service.FollowService;
import com.xe.mynowcoder.service.UserService;
import com.xe.mynowcoder.util.HostHolder;
import com.xe.mynowcoder.util.NowCoderConstant;
import com.xe.mynowcoder.util.NowCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements NowCoderConstant {

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private EventProducer eventProducer;


    @PostMapping(path = "/follow")
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        if(user == null)   return NowCoderUtil.getJSONString(1, "请先登录!");

        followService.follow(user.getUserId(), entityType, entityId);

        // 触发关注事件
        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(hostHolder.getUser().getUserId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return NowCoderUtil.getJSONString(0, "已关注!");
    }

    @PostMapping(path = "/unfollow")
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();
        if(user == null)   return NowCoderUtil.getJSONString(1, "请先登录!");


        followService.unfollow(user.getUserId(), entityType, entityId);

        return NowCoderUtil.getJSONString(0, "已取消关注!");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getUserId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId") int userId, Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFollowerCount(ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getUserId()));
            }
        }
        model.addAttribute("users", userList);

        return "/site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getUserId(), ENTITY_TYPE_USER, userId);
    }

}
