package com.wx.mini.mapper;

import com.wx.mini.pojo.Videos;
import com.wx.mini.utils.MyMapper;
import com.wx.mini.vo.VideoVo;

import java.util.List;


public interface VideosMapperCustomer extends MyMapper<Videos> {

    public List<VideoVo> queryAllVideos();
}