package com.xe.mynowcoder.util;

import com.xe.mynowcoder.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息,用于代替session对象.
 */
@Component
public class HostHolder {

    //线程隔离 ， 用于多线程并发安全
    private ThreadLocal<User> users = new ThreadLocal<>();

    //存储登录用户的信息
    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    //清理内存
    public void clear() {
        users.remove();
    }

}
