package com.qyk.controller;

import com.qyk.pojo.Users;
import com.qyk.pojo.vo.UserVo;
import com.qyk.service.UserService;
import com.qyk.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 用来测试的controller
 * 单点登录测试接口
 * 加上 @ApiIgnore 就不会被加到swagger2文档里了
 */
@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    public static final String SHOP_USER_TOKEN = "shop_user_token";
    public static final String SHOP_USER_TICKET = "shop_user_ticket";
    public static final String SHOP_USER_TEMP_TICKET = "shop_user_temp_ticket";
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";


    @GetMapping("/login")
    public Object hello(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        model.addAttribute("returnUrl", returnUrl);

        // 1.验证并获取userTicket
        String userTicket = getCookie(request, SHOP_USER_TICKET);
        if (verifyUserTicket(userTicket)) {
            String tempTicket = createTempTicket();
            return "redirect:" + returnUrl + "?tempTicket=" + tempTicket;
        }

        // 2.未登录，直接跳转至CAS统一登录页面
        return "login";
    }

    private boolean verifyUserTicket(String userTicket) {
        // 不能为空
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }
        // 获取用户信息
        String userId = redisOperator.get(SHOP_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return false;
        }

        // 校验门票对应的user会话是否存在
        String userVoJsonStr = redisOperator.get(SHOP_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userVoJsonStr)) {
            return false;
        }
        return true;
    }

    /**
     * CAS统一登录接口
     * 目的
     * 1、登录后创建用户的全局会话  uniqueToken
     * 2、创建用户全局门票  userTicket
     * 3、创建用户的临时票据，仅能用于1次，用于回传 tempTicket
     *
     * @param username
     * @param password
     * @param returnUrl
     */
    @PostMapping("/doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "用户名或者密码不能为空！");
            return "login";
        }

        // 1、实现登录
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            model.addAttribute("errmsg", "用户名或者密码不正确！");
            return "login";
        }

        // 2、生成用户token, 存入redis会话
        String uniqueToken = UUID.randomUUID().toString();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userResult, userVo);
        userVo.setUserUniqueToken(uniqueToken);
        redisOperator.set(SHOP_USER_TOKEN + ":" + userResult.getId(), JsonUtils.objectToJson(userVo));

        // 3、生成Ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket = UUID.randomUUID().toString().trim();
        // 3.1 用户的全局门票，需要放进cookie
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        // 4、userTicket关联用户ID，放入redis，
        redisOperator.set(SHOP_USER_TICKET + ":" + userTicket, userResult.getId());

        // 5、生成临时票据，回跳到调用端，由CAS签发的临时票据
        String tempTicket = createTempTicket();

        return "redirect:" + returnUrl + "?tempTicket=" + tempTicket;
    }


    @PostMapping("/verifyTmpTicket")
    @ResponseBody
    public JSONResult verifyTmpTicket(
            String tempTicket,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 使用一次性票据来验证用户是否登录，登录过的，把用户会话信息返回给站点
        String redisTempTicket = redisOperator.get(SHOP_USER_TEMP_TICKET + ":" + tempTicket);
        if (StringUtils.isBlank(redisTempTicket)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 如果凭据ok，需要销毁，并且拿到CAS端COOKIE的全局userTicket，以此获取用户信息
        if (StringUtils.isNotBlank(tempTicket) && tempTicket.equals(redisTempTicket)) {
            // 销毁
            redisOperator.del(SHOP_USER_TEMP_TICKET + ":" + tempTicket);
        } else {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 验证并获取userTicket
        String userTicket = getCookie(request, SHOP_USER_TICKET);
        String userId = redisOperator.get(SHOP_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 校验门票对应的user会话是否存在
        String userVoJsonStr = redisOperator.get(SHOP_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userVoJsonStr)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        return JSONResult.ok(JsonUtils.jsonToPojo(userVoJsonStr, UserVo.class));
    }

    @PostMapping("/logout")
    @ResponseBody
    public JSONResult logout(
            String userId,
            HttpServletRequest request,
            HttpServletResponse response) {
        // 获取CAS门票
        String userTicket = getCookie(request, COOKIE_USER_TICKET);
        // 清除userTicket
        delCookie(COOKIE_USER_TICKET, response);
        redisOperator.del(SHOP_USER_TICKET + ":" + userTicket);
        // 清理全局会话（分布式会话）
        redisOperator.del(SHOP_USER_TOKEN + ":" + userId);
        return JSONResult.ok();
    }


    private String createTempTicket() {
        String tempTicket = UUID.randomUUID().toString().trim();
        try {
            redisOperator.set(SHOP_USER_TEMP_TICKET + ":" + tempTicket,
                    MD5Utils.getMD5Str(tempTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempTicket;
    }

    private void setCookie(String key, String value,
                           HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void delCookie(String key,
                           HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    private String getCookie(HttpServletRequest request,
                             String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(key)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName())) {
                cookie.getValue();
                break;
            }
        }
        return null;
    }


}
