package com.qyk.service;

import com.qyk.pojo.OrderStatus;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.pojo.vo.OrderVO;

public interface OrderService {
    public OrderVO create(SubmitOrderBO submitOrderBO);
    public void updateOrderStatus(String orderId, Integer Status);

    OrderStatus queryStatusByOrderId(String orderId);
}
