package com.qyk.controller;

import org.springframework.web.bind.annotation.GetMapping;
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
public class HelloController {


    @GetMapping("/hello")
    public Object hello() {
        return "hello world!";
    }


    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        // 过期时间
        session.setMaxInactiveInterval(3600);
        return "OK";
    }

}
