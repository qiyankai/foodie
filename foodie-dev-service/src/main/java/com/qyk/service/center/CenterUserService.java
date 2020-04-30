package com.qyk.service.center;

import com.qyk.pojo.Users;
import com.qyk.pojo.bo.center.CenterUserBO;

public interface CenterUserService {

    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);

    /**
     * 修改用户信息
     * @param centerUserBO
     */
    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

}
