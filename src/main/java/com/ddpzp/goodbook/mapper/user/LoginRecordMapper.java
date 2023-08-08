package com.ddpzp.goodbook.mapper.user;

import com.ddpzp.goodbook.po.user.LoginRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dd
 * Date 2021/4/14 23:59
 */
public interface LoginRecordMapper {
    void addLoginRecord(LoginRecord loginRecord);

    List<LoginRecord> getLoginRecords(@Param("account") String account);
}
