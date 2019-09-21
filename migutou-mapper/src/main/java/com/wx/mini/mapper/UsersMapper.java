package com.wx.mini.mapper;


import com.wx.mini.pojo.Users;
import com.wx.mini.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * 增加点赞数
     * @param userId
     */
    void addReceiveLikeCount(@Param("userId") String userId);

    /**
     * 减少点赞数
     * @param userId
     */
    void reduceReceiveLikeCount(@Param("userId") String userId);

    /**
     * 增加粉丝数
     * @param userId
     */
    void addFansCount(@Param("userId") String userId);

    /**
     * 增加关注数
     * @param userId
     */
    void addFollersCount(@Param("userId") String userId);

    /**
     * 减少粉丝数
     * @param userId
     */
    void reduceFansCount(@Param("userId") String userId);

    /**
     * 减少关注数
     * @param userId
     */
    void reduceFollersCount(@Param("userId") String userId);
}