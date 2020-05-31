package com.qyk.service;

import com.qyk.pojo.OrderStatus;
import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.pojo.vo.OrderVO;

import java.util.List;

public interface OrderService {
    public OrderVO create(SubmitOrderBO submitOrderBO, List<ShopcartBO> shopcartBOList);
    public void updateOrderStatus(String orderId, Integer Status);

    OrderStatus queryStatusByOrderId(String orderId);
    void closeTimeOutOrder();
}
