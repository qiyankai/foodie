package com.qyk.service.impl;

import com.qyk.enums.OrderStatusEnum;
import com.qyk.enums.YesOrNo;
import com.qyk.mapper.OrderItemsMapper;
import com.qyk.mapper.OrderStatusMapper;
import com.qyk.mapper.OrdersMapper;
import com.qyk.pojo.*;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String create(SubmitOrderBO submitOrderBO) {
        // 1。新订单保存
        Integer payMethod = submitOrderBO.getPayMethod();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        String userId = submitOrderBO.getUserId();

        Integer postAmount = 0;

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        Orders newOrders = new Orders();
        String orderId = sid.nextShort();
        newOrders.setId(orderId);
        newOrders.setUserId(userId);
        newOrders.setReceiverName(userAddress.getReceiver());
        newOrders.setReceiverAddress(userAddress.getProvince()
                + " " + userAddress.getCity()
                + " " + userAddress.getDistrict());
        newOrders.setReceiverMobile(userAddress.getMobile());

        // todo 计算价格
        newOrders.setPostAmount(postAmount);

        newOrders.setPayMethod(payMethod);
        newOrders.setLeftMsg(leftMsg);
        newOrders.setIsComment(YesOrNo.NO.type);
        newOrders.setIsDelete(YesOrNo.NO.type);
        newOrders.setCreatedTime(new Date());
        newOrders.setUpdatedTime(new Date());

        // 2。循环保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String itemSpecId : itemSpecIdArr) {
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
            // todo 整合redis后，商品购买数量由redis中获取
            int buyCounts = 1;

            // 2。1 根据规格获取价格
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2。2 根据商品id，获取商品的信息与图片
            String itemId = itemsSpec.getItemId();
            Items items = itemService.queryItemById(itemId);
            String mainImgUrl = itemService.queryItemMainImgById(itemId);

            // 2。3 保存子订单
            String subOrderId = sid.nextShort();
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemImg(mainImgUrl);
            subOrderItems.setItemName(items.getItemName());
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(itemSpecId);
            subOrderItems.setItemSpecName(itemsSpec.getName());
            subOrderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItems);

            // 2。4提交订单后，规格表中，需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId,buyCounts);
        }

        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrders);
        // 3保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_DELIVER.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        return orderId;
    }
}
