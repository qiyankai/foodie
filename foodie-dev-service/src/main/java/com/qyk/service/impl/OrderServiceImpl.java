package com.qyk.service.impl;

import com.qyk.enums.OrderStatusEnum;
import com.qyk.enums.YesOrNo;
import com.qyk.mapper.OrderItemsMapper;
import com.qyk.mapper.OrderStatusMapper;
import com.qyk.mapper.OrdersMapper;
import com.qyk.pojo.*;
import com.qyk.pojo.bo.ShopcartBO;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.pojo.vo.MerchantOrdersVO;
import com.qyk.pojo.vo.OrderVO;
import com.qyk.service.OrderService;
import com.qyk.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public OrderVO create(SubmitOrderBO submitOrderBO, List<ShopcartBO> shopcartBOList) {
        // 1。新订单保存
        Integer payMethod = submitOrderBO.getPayMethod();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        String userId = submitOrderBO.getUserId();

        //运费，暂时设置为0
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
        List<ShopcartBO> needToDeleteShopCartList = new ArrayList<>();
        for (String itemSpecId : itemSpecIdArr) {
            // 现在从redis中找
            ShopcartBO shopcartBO = getCartByRedisList(itemSpecId, shopcartBOList);
            needToDeleteShopCartList.add(shopcartBO);
            // 整合redis后，商品购买数量由redis中获取
            int buyCounts = shopcartBO.getBuyCounts();

            // 2。1 根据规格获取价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpecId);
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
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        newOrders.setTotalAmount(totalAmount);
        newOrders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrders);
        // 3保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4构建商户订单，用于传送给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        int amount = realPayAmount + postAmount;
//          暂时将金额设置为0。01元，方便测试，
//        amount = 1;
        merchantOrdersVO.setAmount(amount);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setPayMethod(payMethod);

        // 5构建自定义订单VO
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setNeedToDeleteShopCartList(needToDeleteShopCartList);

        return orderVO;
    }

    private ShopcartBO getCartByRedisList(String itemSpecId, List<ShopcartBO> shopcartBOList) {
        for (ShopcartBO shopcartBO : shopcartBOList) {
            if (shopcartBO.getSpecId().equals(itemSpecId)) {
                return shopcartBO;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer status) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(status);
        orderStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryStatusByOrderId(String orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        return orderStatus;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeTimeOutOrder() {
        //查询所有未付款订单查询是否超时（目前为1day）
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatusList = orderStatusMapper.select(orderStatus);
        for (OrderStatus status : orderStatusList) {
            //对比时间，判断其是否超时
            Date createdTime = status.getCreatedTime();
            int daysBetween = DateUtil.daysBetween(createdTime, new Date());
            if (daysBetween >= 1) {
                //已超时，关闭订单

            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus closeOrderStatus = new OrderStatus();
        closeOrderStatus.setOrderId(orderId);
        closeOrderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        closeOrderStatus.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(closeOrderStatus);
    }
}
