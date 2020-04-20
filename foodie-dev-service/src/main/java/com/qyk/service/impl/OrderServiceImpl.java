package com.qyk.service.impl;

import com.qyk.enums.YesOrNo;
import com.qyk.mapper.CarouselMapper;
import com.qyk.mapper.OrdersMapper;
import com.qyk.mapper.UserAddressMapper;
import com.qyk.pojo.*;
import com.qyk.pojo.bo.SubmitOrderBO;
import com.qyk.service.CarouselService;
import com.qyk.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void create(SubmitOrderBO submitOrderBO) {
        // 1。新订单保存
        Integer payMethod = submitOrderBO.getPayMethod();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        String leftMsg = submitOrderBO.getLeftMsg();
        String userId = submitOrderBO.getUserId();

        Integer postAmount = 0;

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        Orders newOrders = new Orders();

        newOrders.setId(sid.nextShort());
        newOrders.setUserId(userId);
        newOrders.setReceiverName(userAddress.getReceiver());
        newOrders.setReceiverAddress(userAddress.getProvince()
                + " " + userAddress.getCity()
                + " " + userAddress.getDistrict());
        newOrders.setReceiverMobile(userAddress.getMobile());

        // todo 计算价格
//        newOrders.setTotalAmount();
//        newOrders.setRealPayAmount();
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

            // 2。1根据规格获取价格
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;

            // 2。2 根据商品id，获取商品的信息与图片
            Items items = itemService.queryItemById(itemsSpec.getItemId());
            String mainImgUrl = itemService.queryItemMainImgById(itemsSpec.getItemId());
        }


    }
}
