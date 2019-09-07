package com.wx.mini.service.impl;

import com.wx.mini.idworker.Sid;
import com.wx.mini.mapper.UsersMapper;
import com.wx.mini.pojo.Users;
import com.wx.mini.service.UserService;
import com.wx.mini.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @auther alan.chen
 * @time 2019/9/4 10:20 PM
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;


    @Override
    public boolean queryUserNameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        return result != null;
    }

    @Override
    public void saveUserInfo(Users users) {
        String userId = sid.nextShort();
        users.setId(userId);
        try {
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setFansCounts(0);
            users.setReceiveLikeCounts(0);
            users.setFollowCounts(0);
            users.setNickname(users.getUsername());
            usersMapper.insert(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Users loginWithUsernamePassword(String username, String password) {
        try {
            Example example = new Example(Users.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("username", username);
            criteria.andEqualTo("password", MD5Utils.getMD5Str(password));
            Users users = usersMapper.selectOneByExample(example);
            return users;
        } catch (Exception e) {
        }
        return null;
    }

}
