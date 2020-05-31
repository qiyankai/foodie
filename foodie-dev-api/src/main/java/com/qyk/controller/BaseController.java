package com.qyk.controller;

import com.qyk.pojo.Orders;
import com.qyk.service.center.MyOrdersService;
import com.qyk.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {
    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    // 支付成功后-》支付中心-》本系统的执行的回调函数地址
    public String payRequestUrl = "http://47.94.138.137/orders/notifyMerchantOrderPaid";
    public String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";
    // 待更新


    // 用户上传头像位置
    public static final String IMAGE_USER_FILE_LOCATION = File.separator + "Users" +
            File.separator + "qiyankai" +
            File.separator + "Desktop" +
            File.separator + "project" +
            File.separator + "foodie-dev" +
            File.separator + "image";


    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @return
     */
    public JSONResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JSONResult.errorMsg("订单不存在！");
        }
        return JSONResult.ok(order);
    }
}
