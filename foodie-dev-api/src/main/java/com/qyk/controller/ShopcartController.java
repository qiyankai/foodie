package com.qyk.controller;

import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import com.qyk.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Api(value = "购物车接口controller", tags = {"购物车接口controller"})
@RequestMapping("shopcart")
@RestController
public class ShopcartController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestParam String userId,
                          @RequestBody ShopcartBO shopcartBO,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        // 前端用户在登录的情况下，添加商品到购物车， 会同时在后端同步购物车到redis缓存

        String shopCartJsonStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);

        List<ShopcartBO> shopcartBOList = new ArrayList<>();
        // 去redis中查，如果已经有购物车，就添加或增加数量，如果没有，就添加此用户购物车
        if (StringUtils.isNotBlank(shopCartJsonStr)) {
            shopcartBOList = JsonUtils.jsonToList(shopCartJsonStr, ShopcartBO.class);
            boolean isHaving = false;
            for (ShopcartBO shopcart : shopcartBOList) {
                if (shopcart.getSpecId().equals(shopcartBO.getSpecId())) {
                    isHaving = true;
                    int counts = shopcart.getBuyCounts() + shopcartBO.getBuyCounts();
                    shopcart.setBuyCounts(counts);
                    break;
                }
            }
            if (!isHaving) {
                shopcartBOList.add(shopcartBO);
            }
        } else {
            shopcartBOList.add(shopcartBO);
        }

        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartBOList));

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
        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除redis购物车中的商品
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {
            // redis中已经有购物车了
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            // 判断购物车中是否存在已有商品，如果有的话则删除
            for (ShopcartBO sc : shopcartList) {
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(itemSpecId)) {
                    shopcartList.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        }
        return JSONResult.ok();
    }


}
