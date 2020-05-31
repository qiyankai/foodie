package com.qyk.pojo.vo;

import com.qyk.pojo.bo.ShopcartBO;

import java.util.List;

public class OrderVO {

    private String orderId;         // 订单id
    private MerchantOrdersVO merchantOrdersVO;          // 商户订单
    private List<ShopcartBO> needToDeleteShopCartList;          // 商户订单

    public List<ShopcartBO> getNeedToDeleteShopCartList() {
        return needToDeleteShopCartList;
    }

    public void setNeedToDeleteShopCartList(List<ShopcartBO> needToDeleteShopCartList) {
        this.needToDeleteShopCartList = needToDeleteShopCartList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}