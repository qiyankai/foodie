package com.qyk.service;

import com.qyk.pojo.Users;
import com.qyk.pojo.bo.UserBO;


public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 检索用户名与密码是否一致， 用于登录
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);

}
