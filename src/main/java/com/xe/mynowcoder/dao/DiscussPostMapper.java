package com.xe.mynowcoder.dao;


import com.xe.mynowcoder.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //分页 userId=0 查全部 不为0 则根据userId查(个人主页）  offset 起始行号   limit 每页最多显示多少数据
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    //查询帖子总数 userId=0 查全部 不为0 则根据userId查  用于分页
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int discussPostId);

    int updateCommentCount(int discussPostId, int commentCount);

    int updateType(int discussPostId, int type);

    int updateStatus(int discussPostId, int status);

    int updateScore(int discussPostId, double score);

}
