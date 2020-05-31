package com.qyk.controller;

import com.qyk.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用来测试的controller
 * 加上 @ApiIgnore 就不会被加到swagger2文档里了
 */
@ApiIgnore
@RestController
@RequestMapping("/redis")
public class RedisController {


//    @Autowired
//    private RedisTemplate redisTemplate;
//
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public Object set(String key, String value) {
        redisOperator.set(key, value);
        return "hello 憨批!";
    }

    @GetMapping("/get")
    public Object get(String key) {
        return (String) redisOperator.get(key);
    }

    @GetMapping("/delte")
    public Object delte(String key) {

        return "OK";
    }

}
