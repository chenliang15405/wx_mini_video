package com.wx.mini.service.impl;

import com.wx.mini.idworker.Sid;
import com.wx.mini.mapper.UsersFansMapper;
import com.wx.mini.mapper.UsersLikeVideosMapper;
import com.wx.mini.mapper.UsersMapper;
import com.wx.mini.pojo.Users;
import com.wx.mini.pojo.UsersFans;
import com.wx.mini.pojo.UsersLikeVideos;
import com.wx.mini.service.UserService;
import com.wx.mini.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @auther alan.chen
 * @time 2019/9/4 10:20 PM
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;

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

    @Override
    public void update(Users user) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", user.getId());
        usersMapper.updateByExampleSelective(user, example);
    }

    @Override
    public Users findById(String userId) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", userId);
        return usersMapper.selectOneByExample(example);
    }

    @Override
    public boolean isLikeUserVideo(String loginUserId, String videoId) {
        Example example = new Example(UsersLikeVideos.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", loginUserId);
        criteria.andEqualTo("videoId", videoId);

        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
        if(list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 关注用户
     * @param publisherId
     * @param userId
     */
    @Override
    public void followUser(String publisherId, String userId) {
        //1.用户关注粉丝关系表新增数据
        UsersFans uf = new UsersFans();
        String id = sid.nextShort();
        uf.setId(id);
        uf.setUserId(publisherId);
        uf.setFanId(userId);
        usersFansMapper.insert(uf);
        //2. 视频发布者增加粉丝数
        usersMapper.addFansCount(publisherId);
        //3. 登录用户增加关注数
        usersMapper.addFollersCount(userId);
    }

    /**
     * 取关用户
     * @param publisherId
     * @param userId
     */
    @Override
    public void unfollowUser(String publisherId, String userId) {
        //删除关注关联表
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", publisherId);
        criteria.andEqualTo("fanId", userId);
        usersFansMapper.deleteByExample(example);
        //视频发布者减少粉丝数
        usersMapper.reduceFansCount(publisherId);
        //登录用户减少关注数
        usersMapper.reduceFollersCount(userId);
    }

    /**
     * 查询是否关注
     * @param userId
     * @param publisherId
     * @return
     */
    @Override
    public boolean isFollow(String userId, String publisherId) {
        Example example = new Example(UsersFans.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", publisherId);
        criteria.andEqualTo("fanId", userId);
        List<UsersFans> list = usersFansMapper.selectByExample(example);

        if(list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

}
