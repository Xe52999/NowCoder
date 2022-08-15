package com.xe.mynowcoder.dao;

import com.xe.mynowcoder.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xjx
 * @DATE 2022/8/12
 */

@Mapper
public interface UserMapper {

    User selectById(int userId);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int userId, int status);

    int updateHeader(int userId, String headerUrl);

    int updatePassword(int userId, String password);
}
