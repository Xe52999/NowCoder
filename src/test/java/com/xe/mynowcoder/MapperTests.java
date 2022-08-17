package com.xe.mynowcoder;

import com.xe.mynowcoder.dao.DiscussPostMapper;
import com.xe.mynowcoder.dao.LoginTicketMapper;
import com.xe.mynowcoder.dao.MessageMapper;
import com.xe.mynowcoder.dao.UserMapper;
import com.xe.mynowcoder.entity.DiscussPost;
import com.xe.mynowcoder.entity.LoginTicket;
import com.xe.mynowcoder.entity.Message;
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

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

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
    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();

        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        int i = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(i);
    }
    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testSelectLetters(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for (Message message : messages) {
            System.out.println(message);
        }

        int conversationCount = messageMapper.selectConversationCount(111);
        System.out.println("conversationCount = " + conversationCount);

        List<Message> messages1 = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : messages1) {
            System.out.println("message = " +message);
        }

        int i = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println("i = " + i);


    }

}
