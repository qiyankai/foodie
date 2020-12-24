package com.qyk.controller;

import com.qyk.enums.OrderStatusEnum;
import com.qyk.enums.PayMethod;
import com.qyk.pojo.OrderStatus;
import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.pojo.vo.MerchantOrdersVO;
import com.qyk.pojo.vo.OrderVO;
import com.qyk.service.impl.OrderServiceImpl;
import com.qyk.utils.CookieUtils;
import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import com.qyk.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrderController extends BaseController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(@RequestBody SubmitOrderBO submitOrderBO
            , HttpServletRequest request
            , HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return JSONResult.errorMsg("支付方式不支持！");
        }
        // 1. 创建订单
        String shopCartJsonStr = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopCartJsonStr)) {
            return JSONResult.errorMsg("购物数据异常！");
        }
        List<ShopcartBO> shopcartBOList = JsonUtils.jsonToList(shopCartJsonStr, ShopcartBO.class);
        OrderVO orderVO = orderService.create(submitOrderBO, shopcartBOList);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单之后，移除购物车中已结算（已提交）的商品
        // 从redis中取出该清除的购物车物品，现在直接清空
        //        CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"");
        List<ShopcartBO> needToDeleteShopCartList = orderVO.getNeedToDeleteShopCartList();
        shopcartBOList.removeAll(needToDeleteShopCartList);
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartBOList));
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartBOList), true);

        // 3. 向支付中心发送放前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payRequestUrl);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId", "imooc");
        httpHeaders.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> objectHttpEntity = new HttpEntity<>(merchantOrdersVO, httpHeaders);

        ResponseEntity<JSONResult> jsonResultResponseEntity
                = restTemplate.postForEntity(paymentUrl, objectHttpEntity, JSONResult.class);
        JSONResult paymentResult = jsonResultResponseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return JSONResult.errorMsg("订单创建失败，请联系管理员！");
        }

        return JSONResult.ok(orderId);
    }

    private void deleteShopCartToList(List<ShopcartBO> needToDeleteShopCartList, List<ShopcartBO> shopcartBOList) {
        shopcartBOList.removeAll(needToDeleteShopCartList);
    }


    @ApiOperation(value = "订单回调", notes = "订单回调", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "订单状态", notes = "订单状态", httpMethod = "POST")
    @PostMapping("/getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryStatusByOrderId(orderId);
        return JSONResult.ok(orderStatus);
    }

}
