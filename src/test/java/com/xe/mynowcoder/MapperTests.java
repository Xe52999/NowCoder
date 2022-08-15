package com.xe.mynowcoder;

import com.xe.mynowcoder.dao.DiscussPostMapper;
import com.xe.mynowcoder.dao.UserMapper;
import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author xjx
 * @DATE 2022/8/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MyNowCoderApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;


    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getUserId());
    }
    @Test
    public void testSelectById(){
        User user = userMapper.selectById(1);
        System.out.println("user = " + user);
    }

    @Test
    public void testSelectDis(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101, 0, 10, 0);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }

        int i = discussPostMapper.selectDiscussPostRows(101);
        System.out.println(i);

    }

}