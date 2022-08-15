package com.xe.mynowcoder;

import com.xe.mynowcoder.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author xjx
 * @DATE 2022/8/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MyNowCoderApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSend(){
        mailClient.sendMail("specialw@shu.edu.cn","测试邮箱服务","hhhh");
    }


    @Test
    public void testSendHtml(){
        Context context = new Context();
        context.setVariable("username","XeBro");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println("content = " + content);
        mailClient.sendMail("1961409642@qq.com","HTML",content);
    }
}
