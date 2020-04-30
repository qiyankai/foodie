package com.qyk.controller;

import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class BaseController {
    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";

    // 支付成功后-》支付中心-》本系统的执行的回调函数地址
    public String payRequestUrl = "http://eqmnqs.natappfree.cc/orders/notifyMerchantOrderPaid";
    public String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    // 用户上传头像位置
    public static final String IMAGE_USER_FILE_LOCATION = File.separator + "Users" +
            File.separator + "qiyankai" +
            File.separator + "Desktop" +
            File.separator + "project" +
            File.separator + "foodie-dev" +
            File.separator + "image";
}
