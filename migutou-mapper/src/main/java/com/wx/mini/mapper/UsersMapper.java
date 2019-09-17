package com.wx.mini.mapper;


import com.wx.mini.pojo.Users;
import com.wx.mini.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends MyMapper<Users> {

    void addReceiveLikeCount(@Param("userId") String userId);

    void reduceReceiveLikeCount(@Param("userId") String userId);
}