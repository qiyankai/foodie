package com.qyk.controller;

import com.qyk.enums.PayMethod;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrderController {


    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return JSONResult.errorMsg("支付方式不支持！");
        }
        // 1. 创建订单

        // 2. 创建订单之后，移除购物车中已结算（已提交）的商品
        // 3. 向支付中心发送放前订单，用于保存支付中心的订单数据

        return JSONResult.ok();
    }
}
