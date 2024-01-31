package com.hjw.eoj;

import com.hjw.eoj.service.QuestionService;
import com.hjw.eoj.service.QuestionSubmitService;
import com.hjw.eoj.service.impl.QuestionServiceImpl;
import com.hjw.eoj.utils.SpringContextUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

/**
 * 主类（项目启动入口）
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication
@MapperScan("com.hjw.eoj.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication
{


    public static void main(String[] args)
    {
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);
        String[] beanDefinitionNames = run.getBeanDefinitionNames();

    }
}
