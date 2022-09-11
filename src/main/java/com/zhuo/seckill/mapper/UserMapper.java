package com.zhuo.seckill.mapper;

import com.zhuo.seckill.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    /**
     * 插入用户
     */
    boolean insertUser(User user);


    /**
     *  根据id查找用户
     */
    User selectUserById(@Param("id") String id);


}
