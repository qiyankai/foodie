package com.qyk.controller;

import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(value = "购物车接口controller",  tags = {"购物车接口controller"})
@RequestMapping("shopcart")
@RestController
public class ShopcartController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        // TODO 前端用户在登录的情况下，添加商品到购物车， 会同时在后端同步购物车到redis缓存

        System.out.println(shopcartBO);
        return JSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("del")
    public JSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        // TODO 用户在页面删除购物车中的商品数据， 如果此时用户已经登录，则需要同步删除后端购物车中的商品
        return JSONResult.ok();
    }


}
