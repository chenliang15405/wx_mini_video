package com.wx.mini.mapper;

import com.wx.mini.pojo.Videos;
import com.wx.mini.utils.MyMapper;
import org.apache.ibatis.annotations.Param;


public interface VideosMapper extends MyMapper<Videos> {

    void addLikeCount(@Param("videoId") String videoId);

    void reduceLikeCount(@Param("videoId") String videoId);
}