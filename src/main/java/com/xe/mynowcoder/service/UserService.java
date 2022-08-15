package com.xe.mynowcoder.service;

import com.xe.mynowcoder.dao.UserMapper;
import com.xe.mynowcoder.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xjx
 * @DATE 2022/8/15
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User findUserById(int userId){
        return userMapper.selectById(userId);
    }
}
