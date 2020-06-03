package com.qyk;

import com.qyk.utils.RedisOperator;
import csdn.RefreshBlogThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
// 扫描所有包及其相关组件包
@ComponentScan(basePackages = {"com.qyk","csdn","org.n3r.idworker"})
public class ApplicationRefresh {


    public static void main(String[] args) {

        SpringApplication.run(ApplicationRefresh.class, args);

    }
}
