package com.xe.mynowcoder.controller;

import com.alibaba.fastjson.JSONObject;
import com.xe.mynowcoder.entity.Message;
import com.xe.mynowcoder.entity.Page;
import com.xe.mynowcoder.entity.User;
import com.xe.mynowcoder.service.MessageService;
import com.xe.mynowcoder.service.UserService;
import com.xe.mynowcoder.util.HostHolder;
import com.xe.mynowcoder.util.NowCoderConstant;
import com.xe.mynowcoder.util.NowCoderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

@Controller
public class MessageController implements NowCoderConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    // 私信列表
    @GetMapping(path = "/letter/list")
    public String getLetterList(Model model, Page page) {
        //人为造错，观察统一异常处理是否生效（普通请求）
//        Integer.valueOf("abc");

        User user = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        //查询用户参与的会话数量
        page.setRows(messageService.findConversationCount(user.getUserId()));

        // 会话列表 (每个会话的最新的一条私信）
        List<Message> conversationList = messageService.findConversations(
                user.getUserId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                //私信
                map.put("conversation", message);
                //私信数量
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                //未读数量
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getUserId(), message.getConversationId()));
                //找到对话的那个人的id
                int targetId = user.getUserId() == message.getFromId() ? message.getToId() : message.getFromId();
                //找到那个人
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getUserId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "/site/letter";
    }

    @GetMapping(path = "/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        // 私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);

        // 私信目标(当前登录用户对话的目标）
        model.addAttribute("target", getLetterTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getUserId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getUserId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getMessageId());
                }
            }
        }

        return ids;
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content) {
        //人为造错，观察统一异常处理是否生效（异步请求）
//        Integer.valueOf("abc");


        User target = userService.findUserByName(toName);
        if (target == null) {
            return NowCoderUtil.getJSONString(1, "目标用户不存在!");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getUserId());
        message.setToId(target.getUserId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);


        return NowCoderUtil.getJSONString(0);
    }
}
