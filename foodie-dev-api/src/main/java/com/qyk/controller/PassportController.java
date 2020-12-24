package com.qyk.controller;

import com.qyk.pojo.Users;
import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.pojo.bo.UserBO;
import com.qyk.pojo.vo.UserVo;
import com.qyk.service.UserService;
import com.qyk.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(value = "注册登录", tags = {"用户注册登录的接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    /**
     * 判断用户是否存在
     *
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
     *
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
//        userResult = setNullProperty(userResult);

        // 4.1 分布式会话，向redis中存储用户token
        UserVo userVo = convertUserVo(userResult);
        // 4.2 设置浏览器的cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVo), true);

        return JSONResult.ok();
    }

    /**
     * 用户登录
     *
     * @param userBO
     * @return
     */
    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception {
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

        // 生成用户token, 存入redis会话
        UserVo userVo = convertUserVo(userResult);

//        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVo), true);

        // 同步购物车数据
        synchShopCartData(userResult.getId(), request, response);

        return JSONResult.ok(userResult);
    }

    private void synchShopCartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * redis为空，
         *      1、cookie为空，无操作
         *      2、cookie不为空，redis直接放数据进cookie
         * redis不为空，
         *      1、cookie为空，直接将值放进cookie
         *      2、cookie不为空
         *          相同商品：cookie覆盖redis数量
         *          不同商品合并，保持cookie于redis数据一致
         */
        // 从redis中获取购物车
        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        // 从cookie中获取购物车
        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartJsonRedis)) {
            if (StringUtils.isNotBlank(shopcartStrCookie)) {
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
            }
        } else {
            if (StringUtils.isBlank(shopcartStrCookie)) {
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis);
            } else {
                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);

                // 找出cookie中的所有物品id
                // 定义一个待删除list
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();

                for (ShopcartBO redisShopcart : shopcartListRedis) {
                    String redisSpecId = redisShopcart.getSpecId();

                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        if (redisSpecId.equals(cookieSpecId)) {
                            // 覆盖购买数量，不累加，参考京东
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShopcart);
                        }

                    }
                }

                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartListCookie.removeAll(pendingDeleteList);

                // 合并两个list
                shopcartListRedis.addAll(shopcartListCookie);
                // 更新到redis和cookie
                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));

            }
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        // 清楚cookie
        CookieUtils.deleteCookie(request, response, "user");
        // 退出登录需要清空购物车
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        // 分布式会话中要清空用户数据
        redisOperator.del(SHOP_USER_TOKEN + ":" + userId);
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
