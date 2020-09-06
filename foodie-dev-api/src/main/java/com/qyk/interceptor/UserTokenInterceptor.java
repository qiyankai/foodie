package com.qyk.interceptor;

import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import com.qyk.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    public static final String SHOP_USER_TOKEN = "shop_user_token";

    /**
     * 拦截请求，在访问controller调用之前
     *
     * @param request
     * @param response
     * @param handler
     * @return false代表请求被拦截，验证出现问题
     * true 请求经过验证，校验通过，请求到达controller
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerUserId = request.getHeader("headerUserId");
        String headerUserToken = request.getHeader("headerUserToken");
        if (StringUtils.isNotBlank(headerUserId) && StringUtils.isNotBlank(headerUserToken)) {
            String redisToken = redisOperator.get(SHOP_USER_TOKEN + ":" + headerUserId);
            if (StringUtils.isBlank(redisToken)) {
//                System.out.println("请登录。。");
                returnErrorMsg(response, JSONResult.errorMsg("请登录。。"));
                return false;
            } else if (!redisToken.equals(headerUserToken)) {
//                System.out.println("Tonken无效，拒绝访问");
                returnErrorMsg(response, JSONResult.errorMsg("Tonken无效，拒绝访问"));
                return false;
            }
        } else {
//            System.out.println("请登录。。");
            returnErrorMsg(response, JSONResult.errorMsg("请登录。。"));
            return false;
        }
        return true;
    }

    public void returnErrorMsg(HttpServletResponse response, JSONResult result) {
        ServletOutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求访问controller之后，渲染视图之前
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求访问controller之后，渲染视图之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
