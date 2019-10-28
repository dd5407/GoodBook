package com.ddpzp.xiaogu_word.mapper.user;

import com.ddpzp.xiaogu_word.po.user.LoginRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dd
 * Date 2019/10/29 1:56
 */
public interface UserMapper {
    void addLoginRecord(LoginRecord loginRecord);

    List<LoginRecord> getLoginRecords(@Param("account") String account);
}
