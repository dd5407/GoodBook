package com.ddpzp.goodbook.mapper.user;

import com.ddpzp.goodbook.po.user.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by dd
 * Date 2019/10/29 1:56
 */
public interface UserMapper {
    User getUserByName(@Param("username") String username);

    void addUser(User user);

    User getUser(Integer id);
}
