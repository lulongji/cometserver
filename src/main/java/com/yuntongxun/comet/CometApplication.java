package com.yuntongxun.comet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.yuntongxun.comet.dao")
public class CometApplication {
    public static void main(String[] args) {
        SpringApplication.run(CometApplication.class, args);
    }

}
