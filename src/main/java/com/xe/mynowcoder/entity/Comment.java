package com.xe.mynowcoder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private int commentId;
    private int userId;
    //评论的实体。帖子还是评论
    private int entityType;
    private int entityId;
    //具体回复哪一个评论
    private int targetId;
    private String content;
    private int status;
    private Date createTime;


}
