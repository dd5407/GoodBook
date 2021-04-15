package com.ddpzp.xiaogu_word.mapper.user;

import com.ddpzp.xiaogu_word.po.user.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by dd
 * Date 2019/10/29 1:56
 */
public interface UserMapper {
    User getUser(@Param("username") String username);

    void addUser(User user);
}
