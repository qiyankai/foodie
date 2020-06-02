package com.qyk.controller;

import com.qyk.pojo.Users;
import com.qyk.pojo.bo.UserBO;
import com.qyk.service.UserService;
import com.qyk.utils.CookieUtils;
import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import com.qyk.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录", tags = {"用户注册登录的接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    /**
     * 判断用户是否存在
     * @RequestParam: 表示是一种请求参数，而不是路径参数
     */
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username) {

        // 1. 判断用户不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        // 2. 查找注册用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户已经存在");
        }
        // 3. 请求成功，用户名没有重复
        return JSONResult.ok();
    }

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 前端代码要判空， 后端代码也需要
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPwd)) {
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        // 1. 查询用户是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户已经存在");
        }
        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6位");
        }
        // 3. 两次密码必须一样
        if (!password.equals(confirmPwd)) {
            return JSONResult.errorMsg("密码必须一样");
        }
        // 4. 实现注册
        Users userResult = userService.createUser(userBO);
        // 4.1 屏蔽隐私信息
        userResult = setNullProperty(userResult);
        // 4.2 设置浏览器的cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        return JSONResult.ok();
    }


    /**
     * 用户登录
     * @param userBO
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO 入参对象校验不能为 null
        String username = userBO.getUsername();
        String password = userBO.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或者密码不能为空！");
        }

        // 1. 实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return JSONResult.errorMsg("用户名或者密码不正确！");
        }

        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token, 存入redis会话
        // TODO 同步购物车数据

        return JSONResult.ok(userResult);
    }

    /**
     * 退出登录
     * @return
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        // 清楚cookie
        CookieUtils.deleteCookie(request, response, "user");
        // TODO 退出登录需要清空购物车
        // TODO 分布式会话中要清空用户数据
        return JSONResult.ok();
    }



    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setRealname(null);
        userResult.setEmail(null);
        userResult.setUpdatedTime(null);
        userResult.setCreatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }



}
