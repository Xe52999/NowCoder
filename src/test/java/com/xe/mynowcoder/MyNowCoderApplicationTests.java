package com.xe.mynowcoder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

/**
 * 哪个类想获得spring容器就去实现一个ApplicationContextAware接口
 */
@SpringBootTest
@ContextConfiguration(classes = MyNowCoderApplication.class)
class MyNowCoderApplicationTests implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Test
    public void testApplicationContext() {
        System.out.println("applicationContext = " + applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;

    }
}
