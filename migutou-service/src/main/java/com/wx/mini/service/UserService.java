package com.wx.mini.service;

import com.wx.mini.pojo.Users;

/**
 * @auther alan.chen
 * @time 2019/9/4 10:20 PM
 */
public interface UserService {
    boolean queryUserNameIsExist(String username);

    void saveUserInfo(Users users);

    Users loginWithUsernamePassword(String username, String password);
}
