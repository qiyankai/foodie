package com.qyk.service;

import com.qyk.pojo.UserAddress;
import com.qyk.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     *根据用户ID查询用户的收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 根据用户ID 和滴地址id 删除对应的用户地址信息
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);

    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户ID和地址ID查询相应地址
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
