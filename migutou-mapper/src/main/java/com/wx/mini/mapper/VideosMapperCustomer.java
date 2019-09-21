package com.wx.mini.mapper;

import com.wx.mini.pojo.Videos;
import com.wx.mini.utils.MyMapper;
import com.wx.mini.vo.VideoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface VideosMapperCustomer extends MyMapper<Videos> {

    public List<VideoVo> queryAllVideos(@Param("videoDesc") String videoDesc, @Param("userId") String userId);

    List<VideoVo> getAllLikeVideoByPage(@Param("userId") String userId);

    List<VideoVo> getAllFollowVideoByPage(@Param("userId") String userId);
}