package com.xe.mynowcoder.controller;


import com.xe.mynowcoder.entity.Comment;
import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.Event;
import com.xe.mynowcoder.event.EventProducer;
import com.xe.mynowcoder.service.CommentService;
import com.xe.mynowcoder.service.DiscussPostService;
import com.xe.mynowcoder.util.HostHolder;
import com.xe.mynowcoder.util.NowCoderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements NowCoderConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private EventProducer eventProducer;

    //加上帖子id 是因为要回到这个帖子详情的页面
    @PostMapping(path = "/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getUserId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event()
                .setTopic(TOPIC_COMMENT)
                .setUserId(comment.getUserId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId",discussPostId);
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            //找到是谁发的帖子
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            //找到是谁发的评论
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // 触发发帖事件
            event = new Event()
                    .setTopic(TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.fireEvent(event);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }

}
