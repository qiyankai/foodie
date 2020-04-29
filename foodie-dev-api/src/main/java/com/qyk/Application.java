package com.qyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描 mybatis 通用mapper 所在的包
@MapperScan(basePackages="com.qyk.mapper")
// 扫描所有包及其相关组件包
@ComponentScan(basePackages = {"com.qyk", "org.n3r.idworker"})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
