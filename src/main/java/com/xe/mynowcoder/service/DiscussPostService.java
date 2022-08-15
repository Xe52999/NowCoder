package com.xe.mynowcoder.service;

import com.xe.mynowcoder.dao.DiscussPostMapper;
import com.xe.mynowcoder.entity.DiscussPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xjx
 * @DATE 2022/8/15
 */
@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    //分页查询帖子
    public List<DiscussPost> findDiscussPosts(int userId,int offset ,int limit,int orderMode){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit,orderMode);
    }

    //查询帖子数量
    public int findDiscussPostsRows(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }




}
