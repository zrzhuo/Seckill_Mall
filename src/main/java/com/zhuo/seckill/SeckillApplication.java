package com.zhuo.seckill;

import com.zhuo.seckill.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.zhuo.seckill.mapper") // 指定mapper接口的路径
public class SeckillApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SeckillApplication.class, args);
        UserMapper userMapper = context.getBean(UserMapper.class);
        System.out.println(userMapper.selectUserById("18514311234"));
        System.out.println("Application启动");
    }

}
