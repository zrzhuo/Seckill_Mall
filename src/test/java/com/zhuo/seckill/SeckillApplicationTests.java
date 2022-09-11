package com.zhuo.seckill;

import com.zhuo.seckill.entity.User;
import com.zhuo.seckill.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void test(){
        User user = new User();
        user.setId("18514310000");
        user.setNickname("asadasd");
        System.out.println(userMapper.insertUser(user));
    }

}
